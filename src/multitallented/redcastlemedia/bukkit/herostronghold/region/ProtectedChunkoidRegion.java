package multitallented.redcastlemedia.bukkit.herostronghold.region;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.UnsupportedIntersectionException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProtectedChunkoidRegion extends ProtectedRegion {

    private Set<ChunkVector> partialChunks;
    private Set<ChunkVector2D> intersectingChunks;
    private static int BLOCKS_PER_PARTIAL_CHUNK = 4096;
    private int maxY = 127;

    public ProtectedChunkoidRegion(String id, List<ChunkVector> partialChunks, int maxY) {
        super(id);
        this.maxY = maxY;
        this.partialChunks = new HashSet<>(partialChunks);
        this.intersectingChunks = new HashSet<>();
        for (ChunkVector cv : partialChunks) {
            intersectingChunks.add(cv.to2D());
        }
        setMinMax(new ArrayList<>(partialChunks));
    }

    private void setMinMax(List<ChunkVector> pts) {
        int minX = pts.get(0).getX();
        int minY = pts.get(0).getY();
        int minZ = pts.get(0).getZ();
        int maxX = minX + 15;
        int maxY = minY + 15;
        int maxZ = minZ + 15;

        for (ChunkVector v : pts) {
            int x = v.getX() << 4;
            int y = v.getY() << 4;
            int z = v.getZ() << 4;

            if (x < minX) {
                minX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (z < minZ) {
                minZ = z;
            }

            if (x + 15 > maxX) {
                maxX = x + 15;
            }
            if (y + 15 > maxY) {
                maxY = y + 15;
            }
            if (z + 15 > maxZ) {
                maxZ = z + 15;
            }
        }

        min = new BlockVector(minX, minY, minZ);
        max = new BlockVector(maxX, maxY, maxZ);
    }

    @Override
    public List<BlockVector2D> getPoints() {
        throw new UnsupportedOperationException("chunkoid regions do not support point lists");
    }

    public Set<ChunkVector> getChunkoids() {
        return partialChunks;
    }

    public void addPartialChunk(ChunkVector cv) {
        partialChunks.add(cv);
        intersectingChunks.add(cv.to2D());
    }

    public void addWholeChunk(ChunkVector2D cv) {
        for (int y = 0; y < maxY >> 4 ; y++) {
            partialChunks.add(new ChunkVector(cv.getX(), y, cv.getZ()));
        }
        intersectingChunks.add(cv);
    }

    public Set<ChunkVector2D> getIntersectingChunks() {
        return intersectingChunks;
    }

    public boolean containsAny(Set<ChunkVector> chunkoids) {
        for (ChunkVector bv : chunkoids) {
            if (partialChunks.contains(bv)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of intersecting chunks 
     * @return
     */
    public List<ChunkVector2D> getChunks() {
        return new ArrayList<ChunkVector2D>(intersectingChunks);
    }

    @Override
    public int volume() {
        return BLOCKS_PER_PARTIAL_CHUNK * partialChunks.size();
    }

    @Override
    public boolean contains(BlockVector2D pt) {
        return pt.containedWithin(min.toVector2D(), max.toVector2D());
    }

    @Override
    public boolean contains(int x, int y, int z) {
        return contains(ChunkVector.fromBlock(x, y, z));
    }

    public boolean contains(ChunkVector pt) {
        return partialChunks.contains(pt);
    }

    @Override
    public boolean contains(Vector pt) {
        return partialChunks.contains(ChunkVector.fromVector(pt));
    }

    @Override
    public String getTypeName() {
        return "chunkoid";
    }

    @Override
    protected boolean intersectsEdges(ProtectedRegion region) {
        if (region instanceof ProtectedChunkoidRegion) {
            return containsAny(((ProtectedChunkoidRegion) region).getChunkoids());
        } else if (region instanceof ProtectedChunkRegion) {
            return ((ProtectedChunkRegion) region).intersectsWith(this.getChunks());
        }
        return (containsAny(region.getPoints()));
    }

    @Override
    public List<ProtectedRegion> getIntersectingRegions(List<ProtectedRegion> regions) throws UnsupportedIntersectionException {
        List<ProtectedRegion> intersecting = new ArrayList<ProtectedRegion>();
        for (ProtectedRegion region : regions) {
            if (!intersectsBoundingBox(region)) {
                continue;
            }

            if (region instanceof ProtectedChunkoidRegion && this.containsAny(((ProtectedChunkoidRegion) region).getChunkoids())) {
                intersecting.add(region);
            } else if (region instanceof ProtectedChunkRegion && ((ProtectedChunkRegion) region).intersectsWith(this.getChunks())) {
                intersecting.add(region);
            } else if ((region instanceof ProtectedCuboidRegion || (region instanceof ProtectedPolygonalRegion) && containsAny(region.getPoints()))) {
                //TODO: Polygonol intersection may not work for wholly contained regions
                intersecting.add(region);
            } else {
                throw new UnsupportedIntersectionException();
            }
        }
        return intersecting;
    }
}