    package Importers;

    import Mathematical_Engine.Camera;
    import Mathematical_Engine.Camera1;
    import Mathematical_Engine.Camera2;
    import Mathematical_Engine.V3;

    import java.awt.*;
    import java.io.File;
    import java.io.FileNotFoundException;
    import java.util.ArrayList;
    import java.util.Locale;
    import java.util.Scanner;

    public class WavefrontImporter {
        ArrayList<V3> vectors;
        ArrayList<ArrayList<Integer>> faces;
        ArrayList<ArrayList<Integer>> lines;
        ArrayList<V3>normalVectors;
        Scanner scanner;

        public WavefrontImporter(String filePath) {
            vectors = new ArrayList<>();
            faces = new ArrayList<>();
            lines = new ArrayList<>();
            normalVectors = new ArrayList<>();
            extractData(filePath);
        }

        public void draw(Graphics g, Camera2 camera) {
            // Draw lines (if any)
            for (ArrayList<Integer> line : lines) {
                camera.drawLine(g, vectors.get(line.get(0) - 1), vectors.get(line.get(1) - 1));
            }
            for (ArrayList<Integer> face : faces) {
                // Retrieve the vertex indices for the current face
                int v1Index = face.get(0) - 1;
                int v2Index = face.get(1) - 1;
                int v3Index = face.get(2) - 1;

                // Retrieve the corresponding vertices from the list
                if (v1Index <= vectors.size() && v2Index <= vectors.size() && v3Index <= vectors.size()) {
                    V3 v1 = vectors.get(v1Index);
                    V3 v2 = vectors.get(v2Index);
                    V3 v3 = vectors.get(v3Index);

                    // Retrieve the vertex normals for each vertex (if available)
                    V3 n1 = (v1Index < normalVectors.size()) ? normalVectors.get(v1Index) : null;
                    V3 n2 = (v2Index < normalVectors.size()) ? normalVectors.get(v2Index) : null;
                    V3 n3 = (v3Index < normalVectors.size()) ? normalVectors.get(v3Index) : null;

                    // Draw the face using the camera's methods
                    //camera.drawFace(g, v1, v2, v3, n1, n2, n3);
                    camera.drawFace(g, v1, v2, v3);
                }
            }
        }

        public void draw(Graphics g, Camera1 camera) {
            // Draw lines (if any)
            for (ArrayList<Integer> line : lines) {
                camera.drawLine(g, vectors.get(line.get(0) - 1), vectors.get(line.get(1) - 1));
            }
            for (ArrayList<Integer> face : faces) {
                // Retrieve the vertex indices for the current face
                int v1Index = face.get(0) - 1;
                int v2Index = face.get(1) - 1;
                int v3Index = face.get(2) - 1;

                // Retrieve the corresponding vertices from the list
                if (v1Index <= vectors.size() && v2Index <= vectors.size() && v3Index <= vectors.size()) {
                    V3 v1 = vectors.get(v1Index);
                    V3 v2 = vectors.get(v2Index);
                    V3 v3 = vectors.get(v3Index);

                    // Retrieve the vertex normals for each vertex (if available)
                    V3 n1 = (v1Index < normalVectors.size()) ? normalVectors.get(v1Index) : null;
                    V3 n2 = (v2Index < normalVectors.size()) ? normalVectors.get(v2Index) : null;
                    V3 n3 = (v3Index < normalVectors.size()) ? normalVectors.get(v3Index) : null;

                    // Draw the face using the camera's methods
                    //camera.drawFace(g, v1, v2, v3, n1, n2, n3);
                    camera.drawFace(g, v1, v2, v3);
                }
            }
        }

        public void draw(Graphics g, Camera camera) {
            // Draw lines (if any)
            for (ArrayList<Integer> line : lines) {
                camera.drawLine(g, vectors.get(line.get(0) - 1), vectors.get(line.get(1) - 1));
            }
            for (ArrayList<Integer> face : faces) {
                // Retrieve the vertex indices for the current face
                int v1Index = face.get(0) - 1;
                int v2Index = face.get(1) - 1;
                int v3Index = face.get(2) - 1;

                // Retrieve the corresponding vertices from the list
                if (v1Index <= vectors.size() && v2Index <= vectors.size() && v3Index <= vectors.size()) {
                    V3 v1 = vectors.get(v1Index);
                    V3 v2 = vectors.get(v2Index);
                    V3 v3 = vectors.get(v3Index);

                    // Retrieve the vertex normals for each vertex (if available)
                    V3 n1 = (v1Index < normalVectors.size()) ? normalVectors.get(v1Index) : null;
                    V3 n2 = (v2Index < normalVectors.size()) ? normalVectors.get(v2Index) : null;
                    V3 n3 = (v3Index < normalVectors.size()) ? normalVectors.get(v3Index) : null;

                    // Draw the face using the camera's methods
                    //camera.drawFace(g, v1, v2, v3, n1, n2, n3);
                    camera.drawFace(g, v1, v2, v3);
                }
            }
        }

        private void extractData(String filePath) {
            try {
                File file = new File(filePath);
                scanner = new Scanner(file).useLocale(Locale.US);
                ArrayList<String> rawData = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    rawData.add(scanner.nextLine());
                }
                scanner.close();
                setupData(rawData);
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        private void setupData(ArrayList<String> rawData) {
            if (!rawData.isEmpty()) {
                for (String data : rawData) {
                    String[] lines = data.split("\\s+");
                    switch (lines[0]) {
                        case "v": {
                            double x = Double.valueOf(lines[1]);
                            double z = Double.valueOf(lines[2]);
                            double y = Double.valueOf(lines[3]);
                            V3 v3 = new V3(x, y, z);
                            vectors.add(v3);
                            break;
                        } case "f": {
                            ArrayList<Integer> vectors = new ArrayList<>();
                            for (String string : lines) {
                                if (!string.isEmpty() && !string.equals("f")) {
                                    String[] vector = string.split("/");
                                    for (String vector0 : vector) {
                                        int v1 = Integer.valueOf(vector[0]);
                                        vectors.add(v1);
                                    }
                                }
                            }
                            this.faces.add(vectors);
                            vectors = new ArrayList<>();
                            break;
                        } case "l": {
                            ArrayList<Integer> vectors = new ArrayList<>();
                            for (String string : lines) {
                                if (!string.isEmpty() && !string.equals("l") && !string.equals("#")) {
                                    String[] vector = string.split("/");
                                    for (String vector0 : vector) {
                                        int v1 = Integer.valueOf(vector[0]);
                                        vectors.add(v1);
                                    }
                                }
                            }
                            this.lines.add(vectors);
                            vectors = new ArrayList<>();
                            break;
                        } case "vt": {
                            double u = Double.parseDouble(lines[1]);
                            double v = Double.parseDouble(lines[2]);
                            double w = 0;
                            if (lines.length > 3) {
                                w = Double.parseDouble(lines[3]);
                            }

                            // You can store these texture coordinates in your data structure or process them as needed
                            break;
                        } case "vn": {
                            //vn -0.4839 -0.1646 0.8595
                            double nx = Double.parseDouble(lines[1]);
                            double ny = Double.parseDouble(lines[2]);
                            double nz = Double.parseDouble(lines[3]);
                            V3 normal = new V3(nx, ny, nz);
                            normalVectors.add(normal);
                            break;
                        } case "usemtl": {
                            // Handling material usage (usemtl)
                            String materialName = lines[1];
                            // You can store the material name or associate it with relevant faces or vertices
                            break;
                        } case "mtllib": {
                            // Handling material library (mtllib)
                            String mtlFileName = lines[1];
                            // Store the name of the material library file or process it as needed
                            break;
                        }
                    }
                }

            }
        }
        public V3 getCenter() {
            V3 newVector = new V3(0, 0, 0);
            for (V3 vector : vectors) {
                newVector.add(vector);
            }
            return newVector.div(vectors.size());
        }
    }