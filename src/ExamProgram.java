import Importers.WavefrontImporter;
import Mathematical_Engine.Camera;
import Mathematical_Engine.V3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class ExamProgram extends JFrame {

    public static void main(String[] args) {
        new ExamProgram();
    }

    ExamProgram() {
        setTitle("Importer");
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new PaintPanel());
        setVisible(true);
    }

    class PaintPanel extends JPanel {
        int counter = 0;
        TimerListener timer = new TimerListener(counter);
        FPSCounter fpsCounter = new FPSCounter();
        Timer myTimer = new Timer(20, timer);  // Timer updates every 50 ms
        Timer fpsTimer = new Timer(1000, fpsCounter);
        Camera cam = new Camera(100, 100, 500, 400);
        KeyListener keyListener = new KeyListener();
        MouseMotionHandler mouseMotionHandler = new MouseMotionHandler();
        MouseWheelHandler wheelHandler = new MouseWheelHandler();
        WavefrontImporter wf = new WavefrontImporter("src/Files/Ferret.obj");
        WavefrontImporter wf1 = new WavefrontImporter("src/Files/Tea_Pot.obj");



        PaintPanel() {
            myTimer.start();
            fpsTimer.start();
            setFocusable(true); // Allow panel to receive key events
            addKeyListener(keyListener);
            addMouseMotionListener(mouseMotionHandler);
            addMouseWheelListener(wheelHandler);
            cam.moveTo(new V3(30, 15,5));
            cam.focus(wf.getCenter());
            cam.zoom(6);
            wf.rotate(90, "x");
            wf.rotate(180, "z");
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawString("Counter = " + fpsCounter.counter, 10, 10);
            g.drawString("Zoom = " + cam.getZoom(), getWidth()-70, 10);
            g.drawString("Coordinates = (" + cam.getE().x + ", " + cam.getE().y + ", " + cam.getE().z+ ")", getWidth()-190, getHeight()-5);
            wf.draw(g, cam, new V3(0, 0, 0), 0.1, Color.GREEN);
            //wf1.draw(g, cam, new V3(5, 5, 0), 2, Color.RED);
            cam.drawAxis(g, 6, Color.magenta);
        }

        class TimerListener implements ActionListener {
            int counter;

            public TimerListener(int counter) {
                this.counter = counter;
            }

            public void actionPerformed(ActionEvent evt) {
                repaint();
                counter++;
            }
        }

        class FPSCounter implements ActionListener {
            int counter;

            public void actionPerformed(ActionEvent evt) {
                counter = timer.counter;
                timer.counter = 0;
            }
        }

        class KeyListener extends KeyAdapter {
            Set<String> directions = new HashSet<>();

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_A:
                        directions.add("Left");
                        break;
                    case KeyEvent.VK_D:
                        directions.add("Right");
                        break;
                    case KeyEvent.VK_W:
                        directions.add("Forward");
                        break;
                    case KeyEvent.VK_S:
                        directions.add("Back");
                        break;
                    case KeyEvent.VK_Q:
                        cam.roll(-10);
                        break;
                    case KeyEvent.VK_E:
                        cam.roll(10);
                        break;
                    case KeyEvent.VK_SPACE:
                        cam.focus(wf.getCenter());
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
                cam.moveDirection(directions);
            }


            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_A:
                        directions.remove("Left");
                        break;
                    case KeyEvent.VK_D:
                        directions.remove("Right");
                        break;
                    case KeyEvent.VK_W:
                        directions.remove("Forward");
                        break;
                    case KeyEvent.VK_S:
                        directions.remove("Back");
                        break;
                }
                cam.moveDirection(directions);
            }
        }

        class MouseMotionHandler extends MouseAdapter {
            int prevX, prevY;

            @Override
            public void mouseMoved(MouseEvent e) {
                int currentX = e.getX();
                int currentY = e.getY();

                // Update the camera's focus point based on mouse movement
                cam.updateFocusPointWithMouse(currentX, currentY, getWidth(), getHeight(), PaintPanel.this);

                // Update previous mouse position
                prevX = currentX;
                prevY = currentY;
            }
        }

        class MouseWheelHandler implements MouseWheelListener {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) { // Scroll up action
                    if (cam.getZoom() < 20) {
                        cam.zoom(cam.getZoom()+1);
                    }
                } else { // Scroll down action
                    if (cam.getZoom() > 1) {
                        cam.zoom(cam.getZoom()-1);
                    }
                }
            }
        }
    }
}
