package Mathematical_Engine;

public class M3 {
    double a11, a12, a13;       // first index is row no,
    double a21, a22, a23;       // second index is column no.
    double a31, a32, a33;

    public M3(double a11, double a12, double a13, double a21, double a22, double a23, double a31, double a32, double a33){
        this.a11=a11;
        this.a12=a12;
        this.a13=a13;
        this.a21=a21;
        this.a22=a22;
        this.a23=a23;
        this.a31=a31;
        this.a32=a32;
        this.a33=a33;
    }

    public M3(V3 r1, V3 r2, V3 r3) {     // matrix from 3 row vectors
        a11=r1.x; a12=r1.y; a13=r1.z;
        a21=r2.x; a22=r2.y; a23=r2.z;
        a31=r3.x; a32=r3.y; a33=r3.z;
    }

    public M3 add(M3 m){       // matrix addition
        return new M3(  a11+m.a11, a12+m.a12, a13+m.a13,
                        a21+m.a21, a22+m.a22, a23+m.a23,
                        a31+m.a31, a32+m.a32, a33+m.a33);
    }

    public M3 sub(M3 m){       // matrix subtraction
        return new M3(  a11-m.a11, a12-m.a12, a13-m.a13,
                        a21-m.a21, a22-m.a22, a23-m.a23,
                        a31-m.a31, a32-m.a32, a33-m.a33);
    }

    public M3 mul(double d){    // scalar multiplication
        return new M3(  d*a11, d*a12, d*a13,
                        d*a21, d*a22, d*a23,
                        d*a31, d*a32, d*a33);
    }

    public V3 mul(V3 v){        // matrix * vector multiplication
        return new V3(  a11*v.x+a12*v.y+a13*v.z,
                        a21*v.x+a22*v.y+a23*v.z,
                        a31*v.x+a32*v.y+a33*v.z);
    }

    public M3 mul(M3 m){        // matrix multiplication
        return new M3(  a11*m.a11+a12*m.a21+a13*m.a31, a11*m.a12+a12*m.a22+a13*m.a32, a11*m.a13+a12*m.a23+a13*m.a33,
                        a21*m.a11+a22*m.a21+a23*m.a31, a21*m.a12+a22*m.a22+a23*m.a32, a21*m.a13+a22*m.a23+a23*m.a33,
                        a31*m.a11+a32*m.a21+a33*m.a31, a31*m.a12+a32*m.a22+a33*m.a32, a31*m.a13+a32*m.a23+a33*m.a33);
    }

    public String toString() {
        return "[("+a11+","+a12+","+a13+"),("+a21+","+a22+","+a23+"),("+a31+","+a32+","+a33+")]";
    }
}