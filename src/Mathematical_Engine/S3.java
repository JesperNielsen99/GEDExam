package Mathematical_Engine;

import java.awt.*;

public class S3 {
    S2 s2;

    public S3(int Sx, int Sy, int Ox, int Oy) {
        s2 = new S2(Sx, Sy, Ox, Oy);
    }

    public V2 project(V3 p) {
        return new V2(p.y, p.z);
    }

    public void drawAxis(Graphics g) {
        s2.drawAxis(g);
    }

    public void drawAxis(Graphics g, int xLength, int yLength, boolean details , Color color) {
        s2.drawAxis(g, xLength, yLength, details, color);
    }

    public void drawPoint(Graphics g, V3 p) {
        s2.drawPoint(g, project(p));
    }

    public void drawPoint(Graphics g, V3 p, Color color) {
        s2.drawPoint(g, project(p), color);
    }

    public void drawPoint(Graphics g, V3 p, Color color, int diameter) {
        s2.drawPoint(g, project(p), color, diameter);
    }

    public void drawLine(Graphics g, V3 p1, V3 p2) {
        s2.drawLine(g, project(p1), project(p2));
    }

    public void drawLine(Graphics g, V3 p1, V3 p2, Color color) {
        s2.drawLine(g, project(p1), project(p2), color);
    }

    public void drawLine(Graphics g, V3 p1, V3 p2, Color color, float weight) {
        s2.drawLine(g, project(p1), project(p2), color, weight);
    }

}
