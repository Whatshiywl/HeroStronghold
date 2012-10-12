package multitallented.redcastlemedia.bukkit.herostronghold.region;

import org.bukkit.Location;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;

public class ChunkVector2D {

    public static ChunkVector2D fromBlock(int x, int y, int z) {
        return new ChunkVector2D(x >> 4, z >> 4);
    }

    public static ChunkVector2D fromVector(Vector pt) {
        return new ChunkVector2D(pt.getBlockX() >> 4, pt.getBlockZ() >> 4);
    }

    public static ChunkVector2D fromLocation(Location loc) {
        return new ChunkVector2D(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    public static ChunkVector2D fromVector2D(Vector2D pt) {
        return new ChunkVector2D(pt.getBlockX() >> 4, pt.getBlockZ() >> 4);
    }

    private final int x,z ;

    public ChunkVector2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ChunkVector toChunkVector(int y) {
        return new ChunkVector((int) this.x, y, (int) this.z);
    }

    public ChunkVector toChunkVector(double y) {
        return toChunkVector((int) y);
    }

    public boolean containedWithin(ChunkVector2D min, ChunkVector2D max) {
        return x >= min.getX() && x <= max.getX() && z >= min.getZ() && z <= max.getZ();
    }

    public boolean containedWithin(Vector2D min, Vector2D max) {
        int minX = this.x << 4;
        int minZ = this.z << 4;
        int maxX = (this.x << 4) + 15;
        int maxZ = (this.z << 4) + 15;
        return minX >= min.getBlockX() && minZ >= min.getBlockZ() && maxX <= max.getBlockX() && maxZ <= max.getBlockZ();
    }

    public boolean containedWithinBlock(Vector2D min, Vector2D max) {
        return containedWithin(min, max);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ChunkVector2D) {
            ChunkVector2D cv = (ChunkVector2D) obj;
            return this.x == cv.x && this.z == cv.z;
        }
        return false;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }


    /**
     * Set X.
     *
     * @param x
     * @return new vector
     */
    public ChunkVector2D setX(int x) {
        return new ChunkVector2D(x, z);
    }


    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * Set Z.
     *
     * @param z
     * @return new vector
     */
    public ChunkVector2D setZ(int z) {
        return new ChunkVector2D(x, z);
    }

    /**
     * Adds two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector2D add(ChunkVector2D other) {
        return new ChunkVector2D(x + other.x, z + other.z);
    }

    /**
     * Adds two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector2D add(int x, int z) {
        return new ChunkVector2D(this.x + x, this.z + z);
    }


    /**
     * Adds points.
     *
     * @param others
     * @return New point
     */
    public ChunkVector2D add(ChunkVector2D... others) {
        int newX = x, newZ = z;

        for (int i = 0; i < others.length; ++i) {
            newX += others[i].x;
            newZ += others[i].z;
        }
        return new ChunkVector2D(newX, newZ);
    }

    /**
     * Subtracts two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector2D subtract(ChunkVector2D other) {
        return new ChunkVector2D(x - other.x, z - other.z);
    }

    /**
     * Subtract two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector2D subtract(int x, int z) {
        return new ChunkVector2D(this.x - x, this.z - z);
    }

    /**
     * Subtract points.
     *
     * @param others
     * @return New point
     */
    public ChunkVector2D subtract(ChunkVector2D... others) {
        int newX = x, newZ = z;

        for (int i = 0; i < others.length; ++i) {
            newX -= others[i].x;
            newZ -= others[i].z;
        }
        return new ChunkVector2D(newX, newZ);
    }

    /**
     * Multiplies two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector2D multiply(ChunkVector2D other) {
        return new ChunkVector2D(x * other.x, z * other.z);
    }

    /**
     * Multiply two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector2D multiply(int x, int z) {
        return new ChunkVector2D(this.x * x, this.z * z);
    }

    /**
     * Multiply points.
     *
     * @param others
     * @return New point
     */
    public ChunkVector2D multiply(ChunkVector2D... others) {
        int newX = x, newZ = z;

        for (int i = 0; i < others.length; ++i) {
            newX *= others[i].x;
            newZ *= others[i].z;
        }
        return new ChunkVector2D(newX, newZ);
    }

    /**
     * Scalar multiplication.
     *
     * @param n
     * @return New point
     */
    public ChunkVector2D multiply(int n) {
        return new ChunkVector2D(this.x * n, this.z * n);
    }

    /**
     * Divide two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector2D divide(ChunkVector2D other) {
        return new ChunkVector2D(x / other.x, z / other.z);
    }

    /**
     * Divide two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector2D divide(int x, int z) {
        return new ChunkVector2D(this.x / x, this.z / z);
    }

    /**
     * Scalar division.
     *
     * @param n
     * @return new point
     */
    public ChunkVector2D divide(int n) {
        return new ChunkVector2D(x / n, z / n);
    }

    /**
     * Scalar division.
     *
     * @param n
     * @return new point
     */
    public ChunkVector2D divide(double n) {
        return new ChunkVector2D((int) (x / n) ,(int) (z / n));
    }

    /**
     * Get the length of the vector.
     *
     * @return length
     */
    public double length() {
        return Math.sqrt(x * x + z * z);
    }

    /**
     * Get the length^2 of the vector.
     *
     * @return length^2
     */
    public double lengthSq() {
        return x * x + z * z;
    }

    /**
     * Get the distance away from a point.
     *
     * @param pt
     * @return distance
     */
    public double distance(ChunkVector2D pt) {
        return Math.sqrt(Math.pow(pt.x - x, 2) + Math.pow(pt.z - z, 2));
    }

    /**
     * Get the distance away from a point, squared.
     *
     * @param pt
     * @return distance
     */
    public double distanceSq(ChunkVector2D pt) {
        return Math.pow(pt.x - x, 2) + Math.pow(pt.z - z, 2);
    }

    /**
     * Get the normalized vector.
     *
     * @return vector
     */
    public ChunkVector2D normalize() {
        return divide(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other
     * @return the dot product of this and the other vector
     */
    public double dot(ChunkVector2D other) {
        return x * other.x + z * other.z;
    }

    /**
     * Gets the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return ((new Integer(x)).hashCode() >> 13) ^
                (new Integer(z)).hashCode();
    }

    /**
     * Returns string representation "(x, y, z)".
     *
     * @return string
     */
    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }

    /**
     * Creates a 3D vector by adding a zero Y component to this vector.
     *
     * @return Vector
     */
    public ChunkVector toVector() {
        return new ChunkVector(x, 0, z);
    }

    /**
     * Creates a 3D vector by adding the specified Y component to this vector.
     *
     * @return Vector
     */
    public ChunkVector toVector(int y) {
        return new ChunkVector(x, y, z);
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v1
     * @param v2
     * @return minimum
     */
    public static ChunkVector2D getMinimum(ChunkVector2D v1, ChunkVector2D v2) {
        return new ChunkVector2D(Math.min(v1.x, v2.x), Math.min(v1.z, v2.z));
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v1
     * @param v2
     * @return maximum
     */
    public static ChunkVector2D getMaximum(ChunkVector2D v1, ChunkVector2D v2) {
        return new ChunkVector2D(Math.max(v1.x, v2.x), Math.max(v1.z, v2.z));
    }
}