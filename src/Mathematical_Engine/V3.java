package Mathematical_Engine;

public class V3 {
    public double x, y, z;
    public V3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public V3(V3[] vectors) {
        double sumX = 0, sumY = 0, sumZ = 0;
        for(V3 v: vectors) {
            sumX += v.x;
            sumY += v.y;
            sumZ += v.z;
        }
        this.x = sumX / vectors.length;
        this.y = sumY / vectors.length;
        this.z = sumZ / vectors.length;
    }

    public double length() {
        return Math.sqrt(x*x+y*y+z*z);
    }

    public V3 add(V3 v) {
        return new V3((x+v.x), (y+v.y), (z+v.z));
    }

    public V3 sub(V3 v) {
        return new V3((x-v.x), (y-v.y), (z-v.z));
    }

    public V3 mul(double s) {
        return new V3((x*s), (y*s), (z*s));
    }

    public V3 div(double s) { return new V3((x/s), (y/s), (z/s)); }

    public V3 cross(V3 v) {
        return new V3(y * v.z - z * v.y,
                      z * v.x - x * v.z,
                      x * v.y - y * v.x);
    }

    public double distance(V3 v) {
        return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2) + Math.pow(v.z - z, 2));
    }

    public double dot(V3 v) {
        return (x*v.x+y*v.y+z*v.z);
    }

    public V3 unit() {
        if (length() != 0) {
            return new V3(x/length(), y/length(), z/length());
        }
        return this;
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
