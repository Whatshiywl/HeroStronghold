package multitallented.redcastlemedia.bukkit.herostronghold.region;

import org.bukkit.Location;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;

public class ChunkVector {

    public static ChunkVector fromBlock(int x, int y, int z) {
        return new ChunkVector(x >> 4, y >> 4, z >> 4);
    }
    
    public static ChunkVector fromVector(Vector pt) {
        return new ChunkVector(pt.getBlockX() >> 4, pt.getBlockY() >> 4, pt.getBlockZ() >> 4);
    }
    
    public static ChunkVector fromLocation(Location loc) {
        return new ChunkVector(loc.getBlockX() >> 4, loc.getBlockY() >> 4, loc.getBlockZ() >> 4);
    }
    
    private final int x, y, z;
    
    public ChunkVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public ChunkVector2D to2D() {
        return new ChunkVector2D(x, y);
    }
    
    public boolean containedWithin(ChunkVector min, ChunkVector max) {
        return x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY() && z >= min.getZ() && z <= max.getZ();
    }

    public boolean containedWithin(Vector2D min, Vector2D max) {
        int minX = this.x << 4;
        int minZ = this.z << 4;
        int maxX = (this.x << 4) + 15;
        int maxZ = (this.z << 4) + 15;
        return minX >= min.getBlockX() && minZ >= min.getBlockZ() && maxX <= max.getBlockX() && maxZ <= max.getBlockZ();
    }
    
    public boolean containedWithin(Vector min, Vector max) {
        int minX = this.x << 4;
        int minY = this.y << 4;
        int minZ = this.z << 4;
        int maxX = (this.x << 4) + 15;
        int maxY = (this.y << 4) + 15;
        int maxZ = (this.z << 4) + 15;
        return minX >= min.getBlockX() && minY >= min.getBlockY() && minZ >= min.getBlockZ() && maxX <= max.getBlockX() && maxY <= max.getBlockY() && maxZ <= max.getBlockZ();
    }

    public boolean containedWithinBlock(Vector min, Vector max) {
        return containedWithin(min, max);
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
    public ChunkVector setX(int x) {
        return new ChunkVector(x, y, z);
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Set Y.
     *
     * @param y
     * @return new vector
     */
    public ChunkVector setY(int y) {
        return new ChunkVector(x, y, z);
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
    public ChunkVector setZ(int z) {
        return new ChunkVector(x, y, z);
    }

    /**
     * Adds two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector add(ChunkVector other) {
        return new ChunkVector(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Adds two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector add(int x, int y, int z) {
        return new ChunkVector(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Adds points.
     *
     * @param others
     * @return New point
     */
    public ChunkVector add(ChunkVector... others) {
        int newX = x, newY = y, newZ = z;

        for (int i = 0; i < others.length; ++i) {
            newX += others[i].x;
            newY += others[i].y;
            newZ += others[i].z;
        }
        return new ChunkVector(newX, newY, newZ);
    }

    /**
     * Subtracts two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector subtract(ChunkVector other) {
        return new ChunkVector(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Subtract two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public Vector subtract(double x, double y, double z) {
        return new Vector(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public Vector subtract(int x, int y, int z) {
        return new Vector(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract points.
     *
     * @param others
     * @return New point
     */
    public ChunkVector subtract(ChunkVector... others) {
        int newX = x, newY = y, newZ = z;

        for (int i = 0; i < others.length; ++i) {
            newX -= others[i].x;
            newY -= others[i].y;
            newZ -= others[i].z;
        }
        return new ChunkVector(newX, newY, newZ);
    }

    /**
     * Multiplies two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector multiply(ChunkVector other) {
        return new ChunkVector(x * other.x, y * other.y, z * other.z);
    }

    /**
     * Multiply two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector multiply(int x, int y, int z) {
        return new ChunkVector(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiply points.
     *
     * @param others
     * @return New point
     */
    public ChunkVector multiply(ChunkVector... others) {
        int newX = x, newY = y, newZ = z;

        for (int i = 0; i < others.length; ++i) {
            newX *= others[i].x;
            newY *= others[i].y;
            newZ *= others[i].z;
        }
        return new ChunkVector(newX, newY, newZ);
    }

    /**
     * Scalar multiplication.
     *
     * @param n
     * @return New point
     */
    public ChunkVector multiply(double n) {
        return new ChunkVector((int) (this.x * n), (int) (this.y * n), (int) (this.z * n));
    }
    
    /**
     * Scalar multiplication.
     *
     * @param n
     * @return New point
     */
    public ChunkVector multiply(int n) {
        return new ChunkVector(this.x * n, this.y * n, this.z * n);
    }

    /**
     * Divide two points.
     *
     * @param other
     * @return New point
     */
    public ChunkVector divide(ChunkVector other) {
        return new ChunkVector(x / other.x, y / other.y, z / other.z);
    }

    /**
     * Divide two points.
     *
     * @param x
     * @param y
     * @param z
     * @return New point
     */
    public ChunkVector divide(int x, int y, int z) {
        return new ChunkVector(this.x / x, this.y / y, this.z / z);
    }

    /**
     * Scalar division.
     *
     * @param n
     * @return new point
     */
    public ChunkVector divide(int n) {
        return new ChunkVector(x / n, y / n, z / n);
    }

    /**
     * Scalar division.
     *
     * @param n
     * @return new point
     */
    public ChunkVector divide(double n) {
        return new ChunkVector((int) (x / n), (int) (y / n), (int) (z / n));
    }

    /**
     * Scalar division.
     *
     * @param n
     * @return new point
     */
    public Vector divide(float n) {
        return new Vector(x / n, y / n, z / n);
    }

    /**
     * Get the length of the vector.
     *
     * @return length
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Get the length^2 of the vector.
     *
     * @return length^2
     */
    public double lengthSq() {
        return x * x + y * y + z * z;
    }

    /**
     * Get the distance away from a point.
     *
     * @param pt
     * @return distance
     */
    public double distance(ChunkVector pt) {
        return Math.sqrt(Math.pow(pt.x - x, 2) + Math.pow(pt.y - y, 2) + Math.pow(pt.z - z, 2));
    }

    /**
     * Get the distance away from a point, squared.
     *
     * @param pt
     * @return distance
     */
    public double distanceSq(ChunkVector pt) {
        return Math.pow(pt.x - x, 2) + Math.pow(pt.y - y, 2) + Math.pow(pt.z - z, 2);
    }

    /**
     * Get the normalized vector.
     *
     * @return vector
     */
    public ChunkVector normalize() {
        return divide(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other
     * @return the dot product of this and the other vector
     */
    public double dot(ChunkVector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other
     * @return the dot product of this and the other vector
     */
    public ChunkVector cross(ChunkVector other) {
        return new ChunkVector(
            y * other.z + z * other.y,
            z * other.x + x * other.z,
            x * other.y + y * other.x
        );
    }

    /**
     * Clamp the Y component.
     *
     * @param min
     * @param max
     * @return
     */
    public ChunkVector clampY(int min, int max) {
        return new ChunkVector(x, Math.max(min, Math.min(max, y)), z);
    }


    /**
     * 2D transformation.
     *
     * @param angle in degrees
     * @param aboutX
     * @param aboutZ
     * @param translateX
     * @param translateZ
     * @return
     */
    public Vector transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ) {
        angle = Math.toRadians(angle);
        double x = this.x;
        double z = this.z;
        double x2 = x * Math.cos(angle) - z * Math.sin(angle);
        double z2 = x * Math.sin(angle) + z * Math.cos(angle);
        return new Vector(x2 + aboutX + translateX, y, z2 + aboutZ + translateZ);
    }


    /**
     * Checks if another object is equivalent.
     *
     * @param obj
     * @return whether the other object is equivalent
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChunkVector)) {
            return false;
        }
        ChunkVector other = (ChunkVector) obj;
        return other.getX() == this.x && other.getY() == this.y && other.getZ() == this.z;
    }

    /**
     * Gets the hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
       return (Integer.valueOf(x).hashCode() << 19) ^(Integer.valueOf(y).hashCode() << 12) ^ Integer.valueOf(z).hashCode();
    }

    /**
     * Returns string representation "(x, y, z)".
     *
     * @return string
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v1
     * @param v2
     * @return minimum
     */
    public static ChunkVector getMinimum(ChunkVector v1, ChunkVector v2) {
        return new ChunkVector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v1
     * @param v2
     * @return maximum
     */
    public static ChunkVector getMaximum(ChunkVector v1, ChunkVector v2) {
        return new ChunkVector( Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }
}