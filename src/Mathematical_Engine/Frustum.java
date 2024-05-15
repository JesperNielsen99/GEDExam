package Mathematical_Engine;

import Mathematical_Engine.*;

public class Frustum {
    private Plane[] planes; // Array of frustum planes
    private double distanceToCamera;
    private double frustumLength;

    // Constructor to create frustum from camera parameters
    // Constructor to create frustum from camera parameters
    public Frustum(Camera1 camera, double distanceToCamera, int viewportWidth, int viewportHeight) {
        this.distanceToCamera = distanceToCamera;
        frustumLength = distanceToCamera*10;
        // Calculate frustum planes based on camera parameters
        this.planes = new Plane[6]; // 6 planes for left, right, top, bottom, near, and far
        update(camera, viewportWidth, viewportHeight);
    }

    public void update(Camera1 camera, int viewportWidth, int viewportHeight) {
        V3 E = camera.E;
        V3 D = camera.D;
        V3 U = camera.U;
        V3 R = camera.R;
        double zNear = distanceToCamera;
        double zFar = frustumLength;

        // Calculate the centers of the near and far planes
        V3 nearCenter = E.add(D.mul(zNear));
        V3 farCenter = E.add(D.mul(zFar));

        // Calculate half-widths and half-heights of near and far planes
        double nearHalfWidth = Math.tan(Math.toRadians(camera.FOV / 2)) * zNear;
        double nearHalfHeight = nearHalfWidth * viewportHeight / viewportWidth;
        double farHalfWidth = Math.tan(Math.toRadians(camera.FOV / 2)) * zFar;
        double farHalfHeight = farHalfWidth * viewportHeight / viewportWidth;

        // Calculate the normals of the frustum planes
        V3 nNear = D.mul(-1); // Normal points towards the camera
        V3 nFar = D; // Normal points away from the camera
        V3 nLeft = R.cross(U).unit(); // Normal points towards the left
        V3 nRight = U.cross(R).unit(); // Normal points towards the right
        V3 nTop = U; // Normal points towards the top
        V3 nBottom = U.mul(-1); // Normal points towards the bottom

        // Calculate points on the near and far planes
        V3[] nearPoints = {
                nearCenter.add(R.mul(nearHalfWidth)).add(U.mul(nearHalfHeight)), // Top right
                nearCenter.add(R.mul(-nearHalfWidth)).add(U.mul(nearHalfHeight)), // Top left
                nearCenter.add(R.mul(-nearHalfWidth)).add(U.mul(-nearHalfHeight)), // Bottom left
                nearCenter.add(R.mul(nearHalfWidth)).add(U.mul(-nearHalfHeight)) // Bottom right
        };

        V3[] farPoints = {
                farCenter.add(R.mul(farHalfWidth)).add(U.mul(farHalfHeight)), // Top right
                farCenter.add(R.mul(-farHalfWidth)).add(U.mul(farHalfHeight)), // Top left
                farCenter.add(R.mul(-farHalfWidth)).add(U.mul(-farHalfHeight)), // Bottom left
                farCenter.add(R.mul(farHalfWidth)).add(U.mul(-farHalfHeight)) // Bottom right
        };

        // Create frustum planes
        planes[0] = new Plane(nLeft, -nLeft.dot(nearPoints[0])); // Left plane
        planes[1] = new Plane(nRight, -nRight.dot(nearPoints[0])); // Right plane
        planes[2] = new Plane(nTop, -nTop.dot(nearPoints[0])); // Top plane
        planes[3] = new Plane(nBottom, -nBottom.dot(nearPoints[0])); // Bottom plane
        planes[4] = new Plane(nNear, -nNear.dot(E)); // Near plane
        planes[5] = new Plane(nFar, -nFar.dot(E.add(D.mul(zFar)))); // Far plane
    }

    public boolean containsPoint(V3 point) {
        for (Plane plane : planes) {
            if (!plane.isInside(point)) {
                return false;
            }
        }
        return true;
    }
}
