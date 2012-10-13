package multitallented.redcastlemedia.bukkit.herostronghold.region;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.UnsupportedIntersectionException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;

public class ProtectedChunkRegion extends ProtectedRegion {

    Set<ChunkVector2D> chunks;
    Set<ChunkVector> partialChunks;
    private static int BLOCKS_PER_CHUNK = 32768;
    private int maxY = 127;

    public ProtectedChunkRegion(String id, List<ChunkVector2D> chunks, int maxY) {
        super(id);
        this.chunks = new HashSet<>(chunks);
        this.maxY = maxY;
        this.partialChunks = new HashSet<>();
        for (ChunkVector2D cv : chunks) {
            for (int i = 0; i < this.maxY >> 4; i ++) {
                partialChunks.add(cv.toChunkVector(i));
            }
        }
        setMinMax(chunks);
    }

    public void addChunk(ChunkVector2D cv) {
        chunks.add(cv);
        for (int i = 0; i < this.maxY >> 4; i ++) {
            partialChunks.add(cv.toChunkVector(i));
        }
        setMinMax(chunks);
    }

    public void addChunks(Collection<ChunkVector2D> chunks) {
        this.chunks.addAll(chunks);
        for (ChunkVector2D cv : chunks) {
            for (int i = 0; i < this.maxY >> 4; i ++) {
                partialChunks.add(cv.toChunkVector(i));
            }
        }
        setMinMax(this.chunks);
    }
    /**
     * Given a list of chunkvectors, sets the minimum and maximum points
     * @param chunks
     */
    private void setMinMax(Collection<ChunkVector2D> chunks) {
        int minX = 0;
        int minY = 0; 
        int minZ = 0; 
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;

        boolean first = true;
        for (ChunkVector2D v : chunks) {
            if (first) {
                minX = v.getX();
                minY = 0;
                minZ = v.getZ();
                maxX = minX + 15;
                maxY = this.maxY;
                maxZ = minZ + 15;
                first = false;
            }
            int x = v.getX() << 4;
            int z = v.getZ() << 4;

            if (x < minX) minX = x;
            if (z < minZ) minZ = z;

            if (x + 15 > maxX) maxX = x + 15;
            if (z + 15 > maxZ) maxZ = z + 15;
        }

        min = new BlockVector(minX, minY, minZ);
        max = new BlockVector(maxX, maxY, maxZ);
    }

    public List<ChunkVector2D> getChunks() {
        return new ArrayList<>(chunks);
    }

    @Override
    public boolean contains(Vector pt) {
        return contains(ChunkVector2D.fromVector(pt));
    }

    public boolean contains(ChunkVector pt) {
        return contains(pt.to2D());
    }

    public boolean contains(ChunkVector2D pt) {
        return chunks.contains(pt);
    }

    /**
     * Clears all chunks in the region - should only be used if you are going to re-add new chunks
     */
    public void clearChunks() {
        this.chunks.clear();
        this.partialChunks.clear();
    }

    @Override
    public boolean contains(BlockVector2D pt) {
        return contains(ChunkVector2D.fromVector2D(pt));
    }

    @Override
    public boolean contains(int x, int y, int z) {
        return chunks.contains(ChunkVector2D.fromBlock(x, y, z));
    }

    @Override
    public List<ProtectedRegion> getIntersectingRegions(List<ProtectedRegion> regions) throws UnsupportedIntersectionException {
        List<ProtectedRegion> intersecting = new ArrayList<ProtectedRegion>();
        for (ProtectedRegion pr : regions) {
            if (!intersectsBoundingBox(pr)) {
                continue;
            }

            if (pr instanceof ProtectedChunkoidRegion && this.intersectsWith(((ProtectedChunkoidRegion) pr).getChunks())) {
                intersecting.add(pr);
            } else if (pr instanceof ProtectedChunkRegion && this.intersectsWith(((ProtectedChunkRegion) pr).getChunks())) {
                intersecting.add(pr);
            } else if ((pr instanceof ProtectedCuboidRegion || pr instanceof ProtectedPolygonalRegion) && containsAny(pr.getPoints())) {
                intersecting.add(pr);
            } else if (pr instanceof ProtectedPolygonalRegion && containsAny(pr.getPoints())) {
                intersecting.add(pr);
            } else {
                throw new UnsupportedIntersectionException();
            }
        }
        return intersecting;
    }

    public boolean intersectsWith(List<ChunkVector2D> chunks) {
        for (ChunkVector2D chunk : chunks) {
            if (contains(chunk)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BlockVector2D> getPoints() {
        throw new UnsupportedOperationException("chunkoid regions do not support point lists");
    }

    @Override
    public String getTypeName() {
        return "chunk";
    }

    @Override
    public int volume() {
        return chunks.size() * BLOCKS_PER_CHUNK;
    }

    public List<ChunkVector> getPartialChunks() {
        return new ArrayList<>(partialChunks);
    }
}