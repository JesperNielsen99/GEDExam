package Mathematical_Engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import static java.lang.Math.*;

public class Camera {
    V3 O = new V3(0,0,0); //Virtual world Basis
    V3 i = new V3(1,0,0);
    V3 j = new V3(0,1,0);
    V3 k = new V3(0,0,1);

    V3 E = new V3(0,0,0); //Camera Basis
    V3 D = new V3(1,0,0);
    V3 U = new V3(0,1,0);
    V3 R = new V3(0,0,1);
    double z = 2;

    S2 s2;
    M3 I = new M3(1,0,0,
                  0,1,0,
                  0,0,1);

    int prevMouseX;
    int prevMouseY;

    public Camera(int Sx, int Sy, int Ox, int Oy) {
        s2 = new S2(Sx, Sy, Ox, Oy);
    }

    public V2 project(V3 p) {
        V3 EP = p.sub(E);
        double d = D.dot(EP);
        double u = U.dot(EP);
        double r = R.dot(EP);
        if (d <= 0) return null;
        double um = u*z/d;
        double rm = r*z/d;
        return new V2(rm, um);
    }

    public void moveTo(V3 p) {
        E = new V3(p.x, p.y, p.z);
    }

    public void focus(V3 p) {
        D = p.sub(E).unit();
        R = D.cross(k).unit();
        U = R.cross(D);
    }

    public void zoom(double z) {
        this.z = z;
    }
    public double getZoom() { return z; }

    public V3 getE() { return E; }

    public void drawAxis(Graphics g) {
        drawLine(g, O, i);
        drawLine(g, O, j);
        drawLine(g, O, k);
    }

    public void drawAxis(Graphics g, int xLength, int yLength, boolean details , Color color) {
        s2.drawAxis(g, xLength, yLength, details, color);
    }

    public void drawPoint(Graphics g, V3 p) {
        V2 pp1 = project(p);
        if (pp1 == null) return;
        s2.drawPoint(g, pp1);
    }

    public void drawPoint(Graphics g, V3 p, Color color) {
        V2 pp1 = project(p);
        if (pp1 == null) return;
        s2.drawPoint(g, pp1, color);
    }

    public void drawPoint(Graphics g, V3 p, Color color, int diameter) {
        V2 pp1 = project(p);
        if (pp1 == null) return;
        s2.drawPoint(g, pp1, color, diameter);
    }

    public void drawLine(Graphics g, V3 p1, V3 p2) {
        V2 pp1 = project(p1);
        V2 pp2 = project(p2);
        if (pp1 == null || pp2 == null) return;
        s2.drawLine(g, pp1, pp2);
    }

    public void drawLine(Graphics g, V3 p1, V3 p2, Color color) {
        V2 pp1 = project(p1);
        V2 pp2 = project(p2);
        if (pp1 == null || pp2 == null) return;
        s2.drawLine(g, pp1, pp2, color);
    }

    public void drawLine(Graphics g, V3 p1, V3 p2, Color color, float weight) {
        V2 pp1 = project(p1);
        V2 pp2 = project(p2);
        if (pp1 == null || pp2 == null) return;
        s2.drawLine(g, pp1, pp2, color, weight);
    }

    public void drawString(Graphics g, String text, V3 p1, Color color, Font font) {
        V2 pp1 = project(p1);
        if (pp1 == null) return;
        s2.drawString(g, text, pp1, color, font);
    }

    public void drawFace(Graphics g, ArrayList<V3> vectorsToDraw, Color color) {
        V3 firstVector;
        V3 secondVector = null;
        int[] xPoints = new int[vectorsToDraw.size()];
        int[] yPoints = new int[vectorsToDraw.size()];
        boolean canDraw = true;
        for (int i = 0; i < vectorsToDraw.size(); i++) {
            if (i != vectorsToDraw.size()-1) {
                firstVector = vectorsToDraw.get(i);
                if (secondVector != null) {
                    drawLine(g, firstVector, secondVector, Color.BLACK);
                }
                secondVector = firstVector;
            } else {
                firstVector = vectorsToDraw.get(i);
                secondVector = vectorsToDraw.get(0);
                drawLine(g, firstVector, secondVector, Color.BLACK);
            }
            V2 projectedVector = project(vectorsToDraw.get(i));
            if (projectedVector != null) {
                V2 transformedVector = s2.transform(projectedVector);
                xPoints[i] = (int) Math.round(transformedVector.x);
                yPoints[i] = (int) Math.round(transformedVector.y);
            } else {
                canDraw = false;
            }
        }
        if (canDraw) {
            drawPolygon(g, xPoints, yPoints, vectorsToDraw.size(), color);
        }
    }

    public void drawPolygon(Graphics g, int[] xPoints, int[] yPoints, int points, Color color) {
        // Fill the polygon with the specified color
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, points);

        // Draw the outline of the polygon
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, points);
    }

    public void yaw(double angle) {
        double phi = (PI / 360) * 2 * angle;
        M3 Sx = new M3(0, -U.z, U.y,
                            U.z, 0, -U.x,
                            -U.y, U.x, 0);
        M3 Rx = I.add(Sx.mul(sin(phi))).add(Sx.mul(Sx).mul(1 - cos(phi)));
        D = Rx.mul(D);
        R = Rx.mul(R);
    }

    public void roll(double angle) {
        double phi = (PI/360)*2*angle;
        M3 Sz = new M3( 0, -D.z, D.y,
                            D.z, 0, -D.x,
                            -D.y, D.x, 0);
        M3 Rz = I.add(Sz.mul(sin(phi))).add(Sz.mul(Sz).mul(1-cos(phi)));
        U = Rz.mul(U);
        R = Rz.mul(R);
    }

    public void pitch(double angle) {
        double phi = (PI/360)*angle;
        M3 Sy = new M3( 0, -R.z, R.y,
                            R.z, 0, -R.x,
                            -R.y, R.x, 0);
        M3 Ry = I.add(Sy.mul(sin(phi))).add(Sy.mul(Sy).mul(1-cos(phi)));
        D = Ry.mul(D);
        U = Ry.mul(U);
    }


    public void moveDirection(Set<String> directions) {
        double moveAmount = 5; // Adjust the amount as needed
        V3 directionToMove = new V3(0, 0, 0);

        // Determine the movement direction
        for (String direction : directions) {
            switch (direction) {
                case "Left":
                    directionToMove = D.cross(U).unit().mul(-1); // Move left
                    break;
                case "Right":
                    directionToMove = D.cross(U).unit(); // Move right
                    break;
                case "Forward":
                    directionToMove = new V3(D.x, D.y, 0).unit(); // Move forward (horizontal component of D)
                    break;
                case "Back":
                    directionToMove = new V3(D.x, D.y, 0).unit().mul(-1); // Move backward (horizontal component of D)
                    break;
            }
        }
        if (!directionToMove.equals(new V3(0, 0, 0))) {
            directionToMove = directionToMove.unit(); // Normalize the movement direction vector
        }
        // Update camera position only in the horizontal plane
        E = E.add(new V3(directionToMove.x, directionToMove.y, 0).mul(moveAmount));
    }

    private Point getWindowCenter(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);
        if (window != null) {
            Point windowLocation = window.getLocationOnScreen();
            Dimension windowSize = window.getSize();
            return new Point(windowLocation.x + windowSize.width / 2, windowLocation.y + windowSize.height / 2);
        }
        return null;
    }

    public void updateFocusPointWithMouse(int mouseX, int mouseY, int screenWidth, int screenHeight, Component component) {
        double sensitivity = 0.1;
        // Calculate delta from the middle of the screen
        int deltaX = mouseX - screenWidth / 2;
        int deltaY = mouseY - (screenHeight / 2) + 11;

        //TODO Quaternions could be used to fix the rotation of the yaw + pitch.

        // Update yaw and pitch based on mouse movement
        yaw(deltaX * sensitivity);
        pitch(deltaY * sensitivity);

        // Move the mouse back to the center of the screen
        moveMouseToCenter(component);
    }

    private void moveMouseToCenter(Component component) {
        Point windowCenter = getWindowCenter(component);
        if (windowCenter != null) {
            try {
                Robot robot = new Robot();
                robot.mouseMove(windowCenter.x, windowCenter.y);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}
