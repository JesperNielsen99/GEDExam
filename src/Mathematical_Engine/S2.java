package Mathematical_Engine;

import java.awt.*;

public class S2 {
    V2 origo;
    V2 O;
    M2 T;

    public S2(int Sx, int Sy, int Ox, int Oy) {
        origo = new V2(0,0);
        O = new V2(Ox, Oy);
        M2 S = new M2(Sx, 0 , 0, Sy);
        M2 F = new M2(1, 0, 0,-1);
        T = F.mul(S);
    }

    public S2(int Sx, int Sy, int Ox, int Oy, int origoX, int origoY) {
        origo = new V2(0, 0);
        O = new V2(Ox, Oy);
        M2 S = new M2(Sx, 0, 0, Sy);
        M2 F = new M2(1, 0, 0,-1);
        T = F.mul(S);
        moveTo(new V2(origoX, origoY));
    }

    public V2 transform(V2 v) {
        return T.mul(v).add(O);
    }

    public void drawLine(Graphics g, V2 p1, V2 p2) {
        V2 pixelValuep1 = transform(p1);
        V2 pixelValuep2 = transform(p2);
        g.setColor(Color.BLACK);
        g.drawLine((int) pixelValuep1.x, (int) pixelValuep1.y, (int) pixelValuep2.x, (int) pixelValuep2.y);
    }

    public void drawLine(Graphics g, V2 p1, V2 p2, Color color) {
        V2 pixelValuep1 = transform(p1);
        V2 pixelValuep2 = transform(p2);
        g.setColor(color);
        g.drawLine((int) pixelValuep1.x, (int) pixelValuep1.y, (int) pixelValuep2.x, (int) pixelValuep2.y);
    }

    public void drawLine(Graphics g, V2 p1, V2 p2, Color color, float weight) {
        V2 pixelValuep1 = transform(p1);
        V2 pixelValuep2 = transform(p2);
        g.setColor(color);
        for (float i = -weight/2; i <= weight/2; i++) {
            g.drawLine((int) pixelValuep1.x, (int) (pixelValuep1.y+i), (int) pixelValuep2.x, (int) (pixelValuep2.y+i));
        }
    }

    public void drawString(Graphics g, String text, V2 p1) {
        V2 pixelValuep1 = transform(p1);
        g.setColor(Color.BLACK);
        g.drawString(text, (int) pixelValuep1.x, (int) pixelValuep1.y);
    }

    public void drawString(Graphics g, String text, V2 p1, Color color) {
        V2 pixelValuep1 = transform(p1);
        g.setColor(color);
        g.drawString(text, (int) pixelValuep1.x, (int) pixelValuep1.y);
    }

    public void drawString(Graphics g, String text, V2 p1, Color color, Font font) {
        V2 pixelValuep1 = transform(p1);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, (int) pixelValuep1.x, (int) pixelValuep1.y);
    }

    public void drawPoint(Graphics g, V2 p1) {
        V2 pixelValuep1 = transform(p1);
        g.setColor(Color.BLACK);
        int radius = 10;
        double x = (pixelValuep1.x - radius / 2.0);
        double y = (pixelValuep1.y - radius / 2.0);
        g.fillOval((int) x, (int) y, radius, radius);
    }

    public void drawPoint(Graphics g, V2 p1, Color color) {
        V2 pixelValuep1 = transform(p1);
        g.setColor(color);
        int radius = 10;
        double x = (pixelValuep1.x - radius / 2.0);
        double y = (pixelValuep1.y - radius / 2.0);
        g.fillOval((int) x, (int) y, radius, radius);
    }

    public void drawPoint(Graphics g, V2 p1, Color color, int diameter) {
        V2 pixelValuep1 = transform(p1);
        g.setColor(color);
        g.fillOval((int) (pixelValuep1.x-0.5*diameter), (int) (pixelValuep1.y-0.5*diameter), diameter, diameter);
    }

    public void drawGrid(Graphics g, int xLength, int yLength, Color color) {
        g.setColor(Color.lightGray);
        for (int i = 1; i < xLength+1; i++) {
            drawLine(g, new V2(origo.x+i, origo.y), new V2(origo.x+i, origo.y+yLength), color);
        }
        for (int i = 1; i < yLength+1; i++) {
            drawLine(g, new V2(origo.x, origo.y+i), new V2(origo.x+xLength, origo.y+i), color);
        }
    }

    public void drawRect(Graphics g, V2 p1, V2 p2, Color color) {
        V2 pixelValuep1 = transform(p1);
        V2 pixelValuep2 = transform(p2);
        g.setColor(color);
        g.drawRect((int) pixelValuep1.x, (int) pixelValuep1.y, (int) (pixelValuep2.x-pixelValuep1.x), (int) (pixelValuep2.y- pixelValuep1.y));
    }

    public void drawOval(Graphics g, V2 p1, int width, int height, Color color){
        V2 pixelValuep1 = transform(p1);
        g.setColor(color);
        g.drawOval((int) pixelValuep1.x, (int) pixelValuep1.y, width, height);
    }

    public void drawAxis(Graphics g) {
        drawLine(g, origo, new V2(origo.x+1, origo.y), Color.BLACK);
        drawLine(g, origo, new V2(origo.x, origo.y+1), Color.BLACK);
    }

    public void drawAxis(Graphics g, int xLength, int yLength, boolean details , Color color) {
        drawGrid(g, xLength, yLength, Color.lightGray);
        drawLine(g, origo, new V2(origo.x+xLength, origo.y), color);
        drawLine(g, origo, new V2(origo.x, origo.y+yLength), color);
        Font font = new Font("Arial", Font.BOLD, 18); // You can change the font type, style, and size
        g.setFont(font);
        if (details) {
            for (int i = 0; i < xLength + 1; i++) {
                drawString(g, "" + i, new V2(origo.x + i - 0.07, origo.y - 0.5), color, font);
                drawLine(g, new V2(origo.x+i, origo.y-0.1), new V2(origo.y+i, origo.y+0.1), color);
            }

            for (int i = 0; i < yLength + 1; i++) {
                drawString(g, "" + i, new V2(origo.x - 0.5, origo.y + i - 0.15), color, font);
                drawLine(g, new V2(origo.x-0.1, origo.y+i), new V2(origo.x+0.1, origo.x+i), color);
            }
            drawString(g,"y", new V2(origo.x, origo.y+xLength+0.3), color, font);
            drawString(g,"x", new V2(origo.x+yLength+0.3, origo.y), color, font);
        }
    }

    public void moveTo(V2 v2) {
        O = transform(v2);
    }

    public void rotate(double phi) {
        M2 R = new M2(Math.cos(phi), -Math.sin(phi),
                      Math.sin(phi), Math.cos(phi));
        T=T.mul(R);
    }

    @Override
    public String toString() {
        return "(" + origo.x + ", " + origo.y + ")";
    }
}
