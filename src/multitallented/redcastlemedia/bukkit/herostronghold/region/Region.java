package multitallented.redcastlemedia.bukkit.herostronghold.region;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import java.util.ArrayList;
import org.bukkit.Location;

/**
 *
 * @author Multitallented
 */
public class Region extends ProtectedCuboidRegion{
    private int id;
    private Location loc;
    private String type;
    private ArrayList<String> owners;
    private ArrayList<String> members;
    
    public Region(int id, Location loc, String type, ArrayList<String> owners, ArrayList<String> members, BlockVector v1, BlockVector v2) {
        super(id + "", v1, v2);
        this.id = id;
        this.loc = loc;
        this.type = type;
        this.owners = owners;
        this.members = members;
    }
    
    public int getID() {
        return id;
    }
    
    public Location getLocation() {
        return loc;
    }
    
    public String getType() {
        return type;
    }
    
    public ArrayList<String> getAllOwners() {
        return owners;
    }
    
    public ArrayList<String> getAllMembers() {
        return members;
    }
    
    public void addOwner(String name) {
        owners.add(name);
    }
    
    public void addMember(String name) {
        members.add(name);
    }
    
    public boolean remove(String name) {
        if (owners.contains(name)) {
            owners.remove(name);
            return true;
        } else if (members.contains(name)) {
            members.remove(name);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isPrimaryOwner(String name) {
        return owners.get(0).equals(name);
    }
    
    @Override
    public boolean isOwner(String name) {
        return owners.contains(name);
    }
    
    @Override
    public boolean isMember(String name) {
        return members.contains(name);
    }
}

