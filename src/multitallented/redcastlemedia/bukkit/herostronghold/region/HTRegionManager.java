package multitallented.redcastlemedia.bukkit.herostronghold.region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.UnsupportedIntersectionException;
import com.sk89q.worldguard.protection.databases.ProtectionDatabase;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class HTRegionManager extends RegionManager {

    /**
     * List of protected regions.
     */
    private Map<String, ProtectedRegion> regions;
    private Map<ChunkVector, Set<ProtectedRegion>> chunkToRegions = new HashMap<>();

    public HTRegionManager(ProtectionDatabase loader) throws IOException {
        super(loader);
        regions = new HashMap<>();
    }

    @Override
    public Map<String, ProtectedRegion> getRegions() {
        return regions;
    }

    @Override
    public void setRegions(Map<String, ProtectedRegion> regions) {
        this.regions = new HashMap<>(regions);
    }

    @Override
    public void addRegion(ProtectedRegion region) {
        if (region instanceof SuperRegion) {
            this.regions.put(region.getId().toLowerCase(), region);
            for (ChunkVector cv : ((SuperRegion) region).getChunkoids()) {
                Set<ProtectedRegion> ctr = chunkToRegions.get(cv);
                if (ctr == null) {
                    ctr = chunkToRegions.put(cv, new HashSet<ProtectedRegion>());
                }
                ctr.add(region);
            }
        } else if (region instanceof SuperRegion) {
            this.regions.put(region.getId().toLowerCase(), region);
            for (ChunkVector cv : ((SuperRegion) region).getPartialChunks()) {
                Set<ProtectedRegion> ctr = chunkToRegions.get(cv);
                if (ctr == null) {
                    ctr = new HashSet<>();
                    chunkToRegions.put(cv, ctr);
                }
                ctr.add(region);
            }
        } else if (region instanceof GlobalProtectedRegion) {
            this.regions.put(region.getId().toLowerCase(), region);
        } else {
            return;
        }
        try {
            this.save();
        } catch (ProtectionDatabaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasRegion(String id) {
        return regions.containsKey(id.toLowerCase());
    }

    @Override
    public ProtectedRegion getRegion(String id) {
        return regions.get(id.toLowerCase());
    }

    @Override
    public void removeRegion(String id) {
        ProtectedRegion region = regions.remove(id.toLowerCase());

        if (region instanceof ProtectedChunkoidRegion) {
            List<String> removeRegions = new ArrayList<String>();
            Iterator<ProtectedRegion> iter = regions.values().iterator();
            while (iter.hasNext()) {
                ProtectedRegion curRegion = iter.next();
                if (curRegion.getParent() == region) {
                    removeRegions.add(curRegion.getId().toLowerCase());
                }
            }

            for (ChunkVector cv : ((ProtectedChunkoidRegion) region).getChunkoids()) {
                Set<ProtectedRegion> regions = chunkToRegions.get(cv);
                if (regions == null) {
                    continue;
                }

                regions.remove(region);
                if (regions.isEmpty()) {
                    chunkToRegions.remove(cv);
                }
            }

            for (String remId : removeRegions) {
                removeRegion(remId);
            }

        } else if (region instanceof ProtectedChunkRegion) {
            List<String> removeRegions = new ArrayList<>();
            Iterator<ProtectedRegion> iter = regions.values().iterator();
            while (iter.hasNext()) {
                ProtectedRegion curRegion = iter.next();
                if (curRegion.getParent() == region) {
                    removeRegions.add(curRegion.getId().toLowerCase());
                }
            }
            for (ChunkVector cv : ((ProtectedChunkRegion) region).getPartialChunks()) {
                Set<ProtectedRegion> regions = chunkToRegions.get(cv);
                if (regions == null) {
                    continue;
                }

                regions.remove(region);
                if (regions.isEmpty()) {
                    chunkToRegions.remove(cv);
                }
                
            }
            for (String remId : removeRegions) {
                removeRegion(remId);
            }
        }
        try {
            this.save();
        } catch (ProtectionDatabaseException e) {
            e.printStackTrace();
        }
    }

    public void reAddRegion(ProtectedRegion region) {
        if (region instanceof ProtectedChunkoidRegion) {
            for (ChunkVector cv : ((ProtectedChunkoidRegion) region).getChunkoids()) {
                Set<ProtectedRegion> regions = chunkToRegions.get(cv);
                if (regions == null) {
                    continue;
                }

                regions.remove(region);
                if (regions.isEmpty()) {
                    chunkToRegions.remove(cv);
                }
            }

        } else if (region instanceof ProtectedChunkRegion) {
            for (ChunkVector cv : ((ProtectedChunkRegion) region).getPartialChunks()) {
                Set<ProtectedRegion> regions = chunkToRegions.get(cv);
                if (regions == null) {
                    continue;
                }

                regions.remove(region);
                if (regions.isEmpty()) {
                    chunkToRegions.remove(cv);
                }
                
            }
        }
        // Re-Add the region
        addRegion(region);
    }

    @Override
    public ApplicableRegionSet getApplicableRegions(Vector pt) {
        Collection<ProtectedRegion> appRegions = this.chunkToRegions.get(ChunkVector.fromVector(pt));
        if (appRegions == null) {
            appRegions = new ArrayList<>();
        }
        return new ApplicableRegionSet(appRegions, regions.get("__global__"));
    }

    @Override
    public ApplicableRegionSet getApplicableRegions(ProtectedRegion region) {
        List<ProtectedRegion> appRegions = new ArrayList<>(regions.values());
        List<ProtectedRegion> intersecting;
        try {
            intersecting = region.getIntersectingRegions(appRegions);
        } catch (UnsupportedIntersectionException e) {
            intersecting = new ArrayList<>();
        }
        return new ApplicableRegionSet(intersecting, regions.get("__global__"));
    }

    @Override
    public List<String> getApplicableRegionsIDs(Vector pt) {
        List<String> ids = new ArrayList<>();
        for (ProtectedRegion pr : chunkToRegions.get(ChunkVector.fromVector(pt))) {
            ids.add(pr.getId());
        }
        return ids;
    }

    @Override
    public boolean overlapsUnownedRegion(ProtectedRegion region, LocalPlayer player) {
        List<ProtectedRegion> appRegions = new ArrayList<>();

        for (ProtectedRegion other : regions.values()) {
            if (other.isOwner(player)) {
                continue;
            }

            appRegions.add(other);
        }

        List<ProtectedRegion> intersectRegions;
        try {
            intersectRegions = region.getIntersectingRegions(appRegions);
        } catch (Exception e) {
            intersectRegions = new ArrayList<>();
        }

        return intersectRegions.size() > 0;
    }

    @Override
    public int size() {
        return regions.size();
    }

    @Override
    public int getRegionCountOfPlayer(LocalPlayer player) {
        int count = 0;

        for (Map.Entry<String, ProtectedRegion> entry : regions.entrySet()) {
            if (entry.getValue().isOwner(player)) {
                count++;
            }
        }

        return count;
    }
    
    @Override
    public void load() throws ProtectionDatabaseException {
        super.load();
        for (ProtectedRegion region : regions.values()) {
            if (region instanceof ProtectedChunkoidRegion) {
                for (ChunkVector cv : ((ProtectedChunkoidRegion) region).getChunkoids()) {
                    Set<ProtectedRegion> ctr = chunkToRegions.get(cv);
                    if (ctr == null) {
                        ctr = chunkToRegions.put(cv, new HashSet<ProtectedRegion>());
                    }
                    ctr.add(region);
                }
            } else if (region instanceof ProtectedChunkRegion) {
                for (ChunkVector cv : ((ProtectedChunkRegion) region).getPartialChunks()) {
                    Set<ProtectedRegion> ctr = chunkToRegions.get(cv);
                    if (ctr == null) {
                        ctr = new HashSet<>();
                        chunkToRegions.put(cv, ctr);
                    }
                    ctr.add(region);
                }
            }
        }
    }
}