package Mathematical_Engine;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class M2 {
    double a, b;
    double c, d;

    public M2(double radians){
        this.a = cos(radians);
        this.b = sin(radians);
        this.c = -sin(radians);
        this.d = cos(radians);
    }

    public M2(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public M2 mul(double k) { return new M2(a*k, b*k, c*k, d*k); }
    public V2 mul(V2 v) { return new V2(a *v.x+ b *v.y, c *v.x+ d *v.y); }

    public M2 mul(M2 m) { return new M2(a*m.a + b*m.c, a*m.b + b*m.d, c*m.a + d*m.c, c*m.b + d*m.d); }

    public V2 rotate(V2 P, V2 C) { return this.mul(P.sub(C)).add(C); }

    public M2 add(M2 m) { return new M2(a+m.a, b+m.b, c+m.c, d+m.d); }
    public M2 sub(M2 m) { return new M2(a-m.a, b-m.b, c-m.c, d-m.d); }

    public double det() { return a*d-b*c; }
    public M2 adj() { return new M2(d, -b, -c, a); }
    public M2 inv() { return adj().mul(1/det()); }

    public String toString() {
        return  "[(" + a + ", " + b + ")]" + "\n" +
                "[(" + c + ", " + d + ")]";
    }
}
