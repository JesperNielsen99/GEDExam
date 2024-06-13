    package Importers;

    import Mathematical_Engine.Camera;
    import Mathematical_Engine.M3;
    import Mathematical_Engine.V3;

    import java.awt.*;
    import java.io.File;
    import java.io.FileNotFoundException;
    import java.util.ArrayList;
    import java.util.Locale;
    import java.util.Scanner;

    import static java.lang.Math.*;

    public class WavefrontImporter {
        ArrayList<V3> vectors;
        ArrayList<ArrayList<Integer>> faces;
        ArrayList<ArrayList<Integer>> lines;
        Scanner scanner;

        public WavefrontImporter(String filePath, double ratio, V3 startpoint) {
            vectors = new ArrayList<>();
            faces = new ArrayList<>();
            lines = new ArrayList<>();
            extractData(filePath, ratio, startpoint);
        }

        public void draw(Graphics g, Camera camera, Color color) {
            // Draw lines (if any)
            for (ArrayList<Integer> line : lines) {
                int v1Index = line.get(0) - 1;
                int v2Index = line.get(1) - 1;

                // Retrieve the corresponding vertices from the list
                if (v1Index < vectors.size() && v2Index < vectors.size()) {
                    V3 v1 = vectors.get(v1Index);
                    V3 v2 = vectors.get(v2Index);

                    // Draw the line using the camera's method
                    camera.drawLine(g, v1, v2);
                }
            }

            // Draw faces (if any)
            for (ArrayList<Integer> face : faces) {
                ArrayList<V3> vectorsToDraw = new ArrayList<>();
                for (int vIndex : face) {
                    int vectorIndex = vIndex - 1;
                    if (vectorIndex >= 0 && vectorIndex < vectors.size()) {
                        V3 vector = vectors.get(vectorIndex);
                        vectorsToDraw.add(vector);
                    }
                }
                if (vectorsToDraw.size() > 2) {
                    camera.drawFace(g, vectorsToDraw, color);
                }
            }
        }

        private void extractData(String filePath, double ratio, V3 startPoint) {
            try {
                File file = new File(filePath);
                scanner = new Scanner(file).useLocale(Locale.US);
                ArrayList<String> rawData = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    rawData.add(scanner.nextLine());
                }
                scanner.close();
                setupData(rawData, ratio,startPoint);
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        private void setupData(ArrayList<String> rawData, double ratio, V3 startPoint) {
            if (!rawData.isEmpty()) {
                for (String data : rawData) {
                    String[] lines = data.split("\\s+");
                    switch (lines[0]) {
                        case "v": {
                            double x = Double.valueOf(lines[1]);
                            double z = Double.valueOf(lines[2]);
                            double y = Double.valueOf(lines[3]);
                            V3 v3 = new V3(x, y, z);
                            vectors.add(v3.mul(ratio).add(startPoint));
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

        public void rotate(double angle, String axis) {
            M3 I = new M3(1,0,0,
                    0,1,0,
                    0,0,1);
            double phi = (PI/360)*2*angle;
            M3 rotaionMatrix = null;
            switch (axis) {
                case "y": {
                    rotaionMatrix = new M3(Math.cos(phi), 0, Math.sin(phi),
                                        0, 1, 0,
                                            -Math.sin(phi), 0, Math.cos(phi));
                    break;
                }
                case "x": {
                    rotaionMatrix = new M3(1, 0, 0,
                                            0, Math.cos(phi), -Math.sin(phi),
                                            0, Math.sin(phi), Math.cos(phi));
                    break;
                }
                case "z": {
                    rotaionMatrix = new M3(Math.cos(phi), -Math.sin(phi), 0,
                                           Math.sin(phi), Math.cos(phi), 0,
                                      0, 0, 1);
                    break;
                }
            }
            if (rotaionMatrix != null) {
                for (int i = 0; i < vectors.size(); i++) {
                    V3 vector = rotaionMatrix.mul(vectors.get(i));
                    vectors.set(i, vector);
                }
            }
        }
    }
