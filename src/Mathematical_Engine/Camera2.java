package Mathematical_Engine;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

import static java.lang.Math.*;

public class Camera2 {
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
    double FOV = (PI/360)*2*90; // Field of View angle in degrees
    double aspectRatio; // Aspect ratio of the screen
    Frustum frustum;
    double distancetoFrustum = 10;

    public Camera2(int Sx, int Sy, int Ox, int Oy) {
        s2 = new S2(Sx, Sy, Ox, Oy);
    }

    public V2 project(V3 p) {
        V3 EP = p.sub(E);
        double d = D.dot(EP);
        double u = U.dot(EP);
        double r = R.dot(EP);
        double um = u*z/d;
        double rm = r*z/d;
        return new V2(rm, um);
    }

    public void moveTo(V3 p, int screenWidth, int screenHeight) {
        E = new V3(p.x, p.y, p.z);
    }

    public void focus(V3 p, int screenWidth, int screenHeight) {
        D = p.sub(E).unit();
        R = D.cross(k).unit();
        U = R.cross(D);
    }

    public void zoom(double z, int screenWidth, int screenHeight) {
        this.z = z;
    }

    public double getZoom() { return z; }

    public void drawAxis(Graphics g) {
        drawLine(g, O, i);
        drawLine(g, O, j);
        drawLine(g, O, k);
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

    public void drawString(Graphics g, String text, V3 p1, Color color, Font font) {
        s2.drawString(g, text, project(p1), color, font);
    }

    public void drawFace(Graphics g, V3 v1, V3 v2, V3 v3) {
        V3 newV1 = v1.mul(10.0);
        V3 newV2 = v2.mul(10.0);
        V3 newV3 = v3.mul(10.0);

        // Project vertices onto the 2D screen
        //V2 p1 = project(v1);
        //V2 p2 = project(v2);
        //V2 p3 = project(v3);
        V2 p1 = project(newV1);
        V2 p2 = project(newV2);
        V2 p3 = project(newV3);

        //drawLine(g, v1, v2, Color.BLACK);
        //drawLine(g, v2, v3, Color.BLACK);
        //drawLine(g, v3, v1, Color.BLACK);
        drawLine(g, newV1, newV2, Color.BLACK);
        drawLine(g, newV2, newV3, Color.BLACK);
        drawLine(g, newV3, newV1, Color.BLACK);

        /*
        //System.out.println("Double X: " + p1.x + ", " + p2.x + ", " + p3.x);
        //System.out.println("Double Y: " + p1.y + ", " + p2.y + ", " + p3.y);

        // Create an array of x and y coordinates for the vertices of the polygon
        int[] xPoints = {(int) p1.x, (int) p2.x, (int) p3.x};
        int[] yPoints = {(int) p1.y, (int) p2.y, (int) p3.y};
        //System.out.println(xPoints[0] + ", " + xPoints[1] + ", " + xPoints[2]);
        //System.out.println(yPoints[0] + ", " + yPoints[1] + ", " + yPoints[2]);

        // Fill the polygon with the specified color
        g.setColor(Color.GRAY); // You can adjust the fill color as needed
        g.fillPolygon(xPoints, yPoints, 3);

        // Draw the outline of the polygon
        g.setColor(Color.BLACK); // Example outline color, you can adjust it as needed
        g.drawPolygon(xPoints, yPoints, 3);
        */
    }

    public void yaw(double angle) {
        M3 Sx = new M3(0, -1, 0,
                1, 0, 0,
                0, 0, 0);
        double phi = (PI / 360) * 2 * angle;
        M3 Rx = I.add(Sx.mul(sin(phi))).add(Sx.mul(Sx).mul(1 - cos(phi)));
        D = Rx.mul(D).unit();
        R = D.cross(k).unit();
        U = R.cross(D);
    }

    public void roll(double angle) {
        M3 Sz = new M3( 0, 0, 0,
                0, 0, -1,
                0, 1, 0);
        double phi = (PI/360)*2*angle;
        M3 Rz = I.add(Sz.mul(sin(phi))).add(Sz.mul(Sz).mul(1-cos(phi)));
        U = Rz.mul(U);
        R = Rz.mul(R);
    }

    public void pitch(double angle) {
        M3 Sy = new M3( 0, 0, 1,
                0, 0, 0,
                -1, 0, 0);
        double phi = (PI/360)*angle;
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
                    directionToMove = D.cross(U).unit().mul(-1);
                    break;
                case "Right":
                    directionToMove = D.cross(U).unit();
                    break;
                case "Forward":
                    directionToMove = new V3(D.x, D.y, 0).unit();
                    break;
                case "Back":
                    directionToMove = new V3(D.x, D.y, 0).unit().mul(-1);
                    break;
            }
        }
        if (!directionToMove.equals(new V3(0, 0, 0))) {
            directionToMove = directionToMove.unit();
        }
        E = E.add(directionToMove.mul(moveAmount));
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

        System.out.println("DELTA X: " + deltaX);
        System.out.println("DELTA Y: " + deltaY);

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

    // Method to set FOV
    public void setFOV(double fov, int screenWidth, int screenHeight) {
        this.FOV = fov;
    }

    public void setAspectRatio(double aspectRatio, int screenWidth, int screenHeight) {
        this.aspectRatio = aspectRatio;
    }

    // Method to calculate projection matrix with FOV
    private M3 calculateProjectionMatrix() {
        double fovRadians = Math.toRadians(FOV);
        double yScale = 1 / Math.tan(fovRadians / 2);
        double xScale = yScale / aspectRatio;

        return new M3(
                xScale, 0, 0,
                0, yScale, 0,
                0, 0, -1
        );
    }
}
