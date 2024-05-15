package Mathematical_Engine;

public class Plane {
    private V3 normal;   // Normal vector of the plane
    private double distance;  // Distance from the origin along the normal vector

    // Constructor to create a plane from a normal vector and distance
    public Plane(V3 normal, double distance) {
        this.normal = normal;
        this.distance = distance;
    }

    // Method to test if a point is on the positive side of the plane
    public boolean isInside(V3 point) {
        double distanceToPlane = normal.dot(point) + distance;
        return distanceToPlane >= 0;
    }

}
