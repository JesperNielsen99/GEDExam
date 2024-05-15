package Mathematical_Engine;

public class V2 {
    public double x, y;

    public V2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public V2(V2[] vectors) {
        double sumX = 0, sumY = 0;
        for(V2 v: vectors) {
            sumX += v.x;
            sumY += v.y;
        }
        this.x = sumX / vectors.length;
        this.y = sumY / vectors.length;
    }

    public double length() {
        return Math.sqrt(x*x+y*y);
    }

    public V2 add(V2 v) {
        return new V2((x+v.x), (y+v.y));
    }

    public V2 sub(V2 v) {
        return new V2((x-v.x), (y-v.y));
    }

    public V2 mul(double s) {
        return new V2((x*s), (y*s));
    }

    public V2 div(double s) { return new V2((x/s), (y/s)); }

    public V2 cross() {
        return new V2(y*-1, x);
    }

    public double distance(V2 v) {
        return Math.sqrt((Math.pow((v.x-x), 2))+(Math.pow((v.y-y), 2)));
    }

    public double dot(V2 v) {
        return (x*v.x+y*v.y);
    }

    public V2 unit() {
        if (length() != 0) {
            return new V2(x/length(), y/length());
        }
        return this;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
