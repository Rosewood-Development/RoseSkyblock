package dev.rosewood.roseskyblock.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import dev.rosewood.roseskyblock.database.migration._1_CreateInitialTables;
import dev.rosewood.roseskyblock.island.Island;
import dev.rosewood.roseskyblock.island.IslandGroup;
import dev.rosewood.roseskyblock.island.IslandMemberLevel;
import dev.rosewood.roseskyblock.island.IslandSettings;
import dev.rosewood.roseskyblock.world.IslandWorld;
import dev.rosewood.roseskyblock.world.IslandWorldGroup;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DataManager extends AbstractDataManager {

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    /**
     * Save a new island world group into SQL
     *
     * @param worldGroup The worldgroup
     * @param uuid       The island group's owner ID
     * @param locationId The IslandGroup location
     * @return The owner's new IslandGroup.
     */
    public IslandGroup saveNewIslandGroup(IslandWorldGroup worldGroup, UUID uuid, int locationId) {
        // i dont know what else to do outside of atomic reference without using a hashmap
        AtomicReference<IslandGroup> group = new AtomicReference<>();

        this.databaseConnector.connect(connection -> {
            String insertGroup = "INSERT INTO " + this.getTablePrefix() + "island_group (group_name, owner_uuid, location_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertGroup, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, worldGroup.name());
                statement.setString(2, uuid.toString());
                statement.setInt(3, locationId);
                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                int groupId = keys.getInt(1);
                group.set(new IslandGroup(
                        worldGroup,
                        groupId,
                        uuid,
                        locationId,
                        new ArrayList<>(),
                        new HashMap<>() {{
                            this.put(uuid, IslandMemberLevel.OWNER);
                        }}
                ));
            }

            String insertMember = "INSERT INTO " + this.getTablePrefix() + "island_member (group_id, player_uuid, member_label) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertMember)) {
                statement.setInt(1, group.get().groupId());
                statement.setString(2, uuid.toString());
                statement.setString(3, IslandMemberLevel.OWNER.name());
                statement.executeUpdate();
            }
        });

        return group.get();
    }

    /**
     * Save a new island into the DB
     *
     * @param islandGroup   The island's group
     * @param islandWorld   The island's world
     * @param spawnLocation The island's spawn location
     * @return The island
     */
    public Island saveNewIsland(IslandGroup islandGroup, IslandWorld islandWorld, Location spawnLocation) {
        AtomicReference<Island> island = new AtomicReference<>();
        this.getDatabaseConnector().connect(connection -> {
            String insertGroup = "INSERT INTO " + this.getTablePrefix() + "island (id, world, spawn_x, spawn_y, spawn_z, spawn_pitch, spawn_yaw) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertGroup, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, islandGroup.groupId());
                statement.setString(2, islandWorld.worldName());
                statement.setDouble(3, spawnLocation.getX());
                statement.setDouble(4, spawnLocation.getY());
                statement.setDouble(5, spawnLocation.getZ());
                statement.setDouble(6, spawnLocation.getYaw());
                statement.setDouble(7, spawnLocation.getPitch());
                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                int islandId = keys.getInt(1);
                island.set(new Island(islandGroup, islandId, islandWorld, spawnLocation));
            }
        });

        return island.get();
    }

    /**
     * Save a new set of island settings into the DB
     *
     * @param settings The island settings.
     */
    public void saveNewIslandSettings(IslandSettings settings) {
        this.databaseConnector.connect(connection -> {
            String insertSettings = "INSERT INTO " + this.getTablePrefix() + "settings (border) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSettings)) {
                statement.setString(1, settings.border().name());
                statement.executeUpdate();
            }
        });
    }

    public IslandGroup getIslandGroup(OfflinePlayer player, IslandWorldGroup worldGroup) {
        AtomicReference<IslandGroup> islandGroup = new AtomicReference<>();
        this.getDatabaseConnector().connect(connection -> {
            String selectGroup = "SELECT id, location_id FROM " + this.getTablePrefix() + "island_group WHERE group_name = ? AND owner_uuid = ?";

            int groupId = -1;
            List<Island> islands = new ArrayList<>();
            Map<UUID, IslandMemberLevel> members = new HashMap<>();
            try (PreparedStatement statement = connection.prepareStatement(selectGroup)) {
                statement.setString(1, worldGroup.name());
                statement.setString(2, player.getUniqueId().toString());
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    groupId = result.getInt("id");
                    islandGroup.set(new IslandGroup(
                            worldGroup,
                            groupId,
                            player.getUniqueId(),
                            result.getInt("location_id"),
                            islands,
                            members
                    ));
                }
            }

            if (islandGroup.get() == null)
                return;

            String selectIslands = "SELECT * FROM " + this.getTablePrefix() + "island WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectIslands)) {
                statement.setInt(1, groupId);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    Optional<IslandWorld> world = worldGroup.worlds().stream().filter(islandWorld -> {
                        try {
                            return islandWorld.worldName().equalsIgnoreCase(result.getString("world"));
                        } catch (SQLException e) { // i hate this language
                            e.printStackTrace();
                        }
                        return false;
                    }).findFirst();

                    if (world.isEmpty())
                        continue;

                    islands.add(new Island(islandGroup.get(), result.getInt("id"), world.get(), new Location(
                            world.get().getWorld(),
                            result.getDouble("spawn_x"),
                            result.getDouble("spawn_y"),
                            result.getDouble("spawn_z"),
                            result.getFloat("spawn_yaw"),
                            result.getFloat("spawn_pitch")
                    )));


                }
            }

            String selectMembers = "SELECT * FROM " + this.getTablePrefix() + "island_member WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectMembers)) {
                statement.setInt(1, groupId);
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    UUID uuid = UUID.fromString(result.getString("player_uuid"));
                    IslandMemberLevel memberLevel = IslandMemberLevel.valueOf(result.getString("member_level"));
                    members.put(uuid, memberLevel);
                }
            }
        });

        return islandGroup.get();
    }

    @Override
    public List<Class<? extends DataMigration>> getDataMigrations() {
        return Collections.singletonList(_1_CreateInitialTables.class);
    }
}
