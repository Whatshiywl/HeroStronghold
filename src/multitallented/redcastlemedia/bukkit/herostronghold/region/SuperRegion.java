package multitallented.redcastlemedia.bukkit.herostronghold.region;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.UnsupportedIntersectionException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;

/**
 *
 * @author Multitallented
 */
public class SuperRegion extends ProtectedRegion {
    private String name;
    private final Location l;
    private final String type;
    private final Map<String, List<String>> members;
    private final List<String> owners;
    private int power;
    private double taxes = 0;
    private double balance = 0;
    private LinkedList<Double> taxRevenue;
    private Set<ChunkVector> partialChunks;
    private Set<ChunkVector2D> intersectingChunks;
    private static int BLOCKS_PER_PARTIAL_CHUNK = 4096;
    
    public SuperRegion(String name, Location l, String type, List<String> owner, Map<String, List<String>> members, int power, double taxes, double balance, LinkedList<Double> taxRevenue, List<ChunkVector> partialChunks) {
        super(name);
        this.name = name;
        this.l = l;
        this.type=type;
        this.owners = owner;
        this.members = members;
        this.power = power;
        this.taxes = taxes;
        this.balance = balance;
        this.taxRevenue = taxRevenue;
        this.partialChunks = new HashSet<>(partialChunks);
    }
    
    public LinkedList<Double> getTaxRevenue() {
        return taxRevenue;
    }
    
    public void addTaxRevenue(double input) {
        taxRevenue.addFirst(input);
        if (taxRevenue.size() > 5) {
            taxRevenue.removeLast();
        }
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public double getTaxes() {
        return taxes;
    }
    
    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Location getLocation() {
        return l;
    }
    
    public String getType() {
        return type;
    }
    
    public boolean hasMember(String name) {
        return members.containsKey(name);
    }
    
    public boolean addMember(String name, List<String> perms) {
        return members.put(name, perms) != null;
    }
    
    public List<String> getMember(String name) {
        return members.get(name);
    }
    
    public Map<String, List<String>> getAllMembers() {
        return members;
    }
    
    public boolean togglePerm(String name, String perm) {
        boolean removed = false;
        try {
            if (!members.get(name).remove(perm)) {
                members.get(name).add(perm);
            } else {
                removed = true;
            }
        } catch (NullPointerException npe) {
            
        }
        return removed;
    }
    
    public boolean hasOwner(String name) {
        return owners.contains(name);
    }
    
    public boolean addOwner(String name) {
        return owners.add(name);
    }
    
    public List<String> getAllOwners() {
        return owners;
    }
    
    public boolean remove(String name) {
        if (!owners.remove(name)) {
            return members.remove(name) != null;
        } else {
            return true;
        }
    }
    
    public int getPower() {
        return power;
    }
    
    public void setPower(int i) {
        power = i;
    }
    
    public int getPopulation() {
        int membersSize = 0;
        for (String s : members.keySet()) {
            if (members.get(s).contains("member")) {
                membersSize += 1;
            }
        }
        return owners.size() + membersSize;
    }

    @Override
    public List<BlockVector2D> getPoints() {
        throw new UnsupportedOperationException("chunkoid regions do not support point lists");
    }

    @Override
    public int volume() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(Vector vector) {
        return partialChunks.contains(ChunkVector.fromVector(vector));
    }

    @Override
    public String getTypeName() {
        return "chunkoid";
    }
    
    public Set<ChunkVector> getChunkoids() {
        return partialChunks;
    }
    
    public boolean containsAny(Set<ChunkVector> chunkoids) {
        for (ChunkVector bv : chunkoids) {
            if (partialChunks.contains(bv)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ProtectedRegion> getIntersectingRegions(List<ProtectedRegion> regions) throws UnsupportedIntersectionException {
        List<ProtectedRegion> intersecting = new ArrayList<>();
        for (ProtectedRegion region : regions) {
            if (!intersectsBoundingBox(region)) {
                continue;
            }

            if (region instanceof SuperRegion && this.containsAny(((SuperRegion) region).getChunkoids())) {
                intersecting.add(region);
            } else if ((region instanceof SuperRegion || (region instanceof Region) && containsAny(region.getPoints()))) {
                //TODO implement this when I get Regions extended from ProtectedRegion
                //TODO: Polygonol intersection may not work for wholly contained regions
                intersecting.add(region);
            } else {
                throw new UnsupportedIntersectionException();
            }
        }
        return intersecting;
    }
    
    @Override
    protected boolean intersectsEdges(ProtectedRegion region) {
        if (region instanceof SuperRegion) {
            return containsAny(((SuperRegion) region).getChunkoids());
        }
        return (containsAny(region.getPoints()));
    }
}
