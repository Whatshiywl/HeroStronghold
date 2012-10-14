package multitallented.redcastlemedia.bukkit.herostronghold.region;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

/**
 *
 * @author Multitallented
 */
public class BuildRegion extends ProtectedCuboidRegion{
    private int id;
    
    public BuildRegion(int id, BlockVector v1, BlockVector v2, String name) {
        super(name, v1, v2);
        this.id = id;
    }
    
    public int getID() {
        return id;
    }
}

