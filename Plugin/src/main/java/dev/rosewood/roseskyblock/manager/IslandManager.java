package dev.rosewood.roseskyblock.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.roseskyblock.island.IslandGroup;
import dev.rosewood.roseskyblock.world.IslandWorldGroup;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class IslandManager extends Manager {

    private final Map<UUID, Map<IslandWorldGroup, IslandGroup>> islandGroups = Collections.synchronizedMap(new HashMap<>());

    public IslandManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
    }

    @Override
    public void disable() {

    }

    /**
     * Gets the smallest positive integer greater than 0 from a list
     *
     * @param existingIds The list containing non-available ids
     * @return The smallest positive integer not in the given list
     */
    public int getNextIslandId(Collection<Integer> existingIds) {
        List<Integer> copy = new ArrayList<>(existingIds);
        for (int i = 0; i < copy.size(); i++) {
            while (copy.get(i) != i + 1) {
                if (copy.get(i) <= 0 || copy.get(i) > copy.size() || copy.get(i).equals(copy.get(copy.get(i) - 1)))
                    break;

                int temp = copy.get(i);
                copy.set(i, copy.get(temp - 1));
                copy.set(temp - 1, temp);
            }
        }

        for (int i = 0; i < copy.size(); i++)
            if (copy.get(i) != i + 1)
                return i + 1;

        return copy.size() + 1;
    }

    /**
     * Attempts to load islands for a player from the database, checks the cache first
     *
     * @param owner The owner of the islands to load
     * @return the islands that were loaded or the islands from cache
     */
    public CompletableFuture<Map<IslandWorldGroup, IslandGroup>> tryLoadIslands(OfflinePlayer owner) {
        if (this.islandGroups.containsKey(owner.getUniqueId()))
            return CompletableFuture.completedFuture(this.islandGroups.get(owner.getUniqueId()));

        return CompletableFuture.supplyAsync(() -> {
            DataManager data = this.rosePlugin.getManager(DataManager.class);
            return this.rosePlugin.getManager(WorldManager.class).getWorldGroups().stream()
                    .map(worldGroup -> data.getIslandGroup(owner, worldGroup))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(IslandGroup::worldGroup, islandGroup -> islandGroup));
        });
    }

    /**
     * Attempts to unload islands for a player
     *
     * @param owner The player
     */
    public void tryUnloadIslands(OfflinePlayer owner) {
        // TODO: Check if island is still active, if so, don't unload it.
        this.islandGroups.remove(owner.getUniqueId());
    }

    public Map<UUID, Map<IslandWorldGroup, IslandGroup>> getIslandGroups() {
        return islandGroups;
    }

}
