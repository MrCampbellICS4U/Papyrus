import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnappablePanels extends JFrame {

    MainPanel mainPanel = new MainPanel();

    SnappablePanels() {
        super("Widgets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(true);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new SnappablePanels());
    }
}

class MainPanel extends JPanel {
    JLayeredPane layeredPane = new JLayeredPane();
    SnapToPanel[] snapToPanels = new SnapToPanel[3];
    SnappablePanel[] snappablePanels = new SnappablePanel[3];
    
    MainPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        add(layeredPane, BorderLayout.CENTER);

        snapToPanels[0] = new SnapToPanel(Color.WHITE, 4);
        snapToPanels[1] = new SnapToPanel(Color.WHITE, 2);
        snapToPanels[2] = new SnapToPanel(Color.WHITE, 4);

        for (int i = 0; i < snapToPanels.length; i++) {
            layeredPane.add(snapToPanels[i], JLayeredPane.DEFAULT_LAYER);
        }

        for (int i = 0; i < snappablePanels.length; i++) {
            snappablePanels[i] = new SnappablePanel(new Color((int) (Math.random() * 0x1000000)), snapToPanels[i].scale);
            addSnappablePanel(snappablePanels[i], snapToPanels[i]);
        }

        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustPanelSizes();
                adjustSnappablePanelSize();
            }
        });

        adjustPanelSizes(); // Initial adjustment
    }

    private void adjustPanelSizes() {
        int totalWidth = getWidth();
        int panelWidth = totalWidth / snapToPanels.length;
        for (int i = 0; i < snapToPanels.length; i++) {
            snapToPanels[i].setBounds(i * panelWidth, 0, panelWidth, getHeight());
        }
    }

    private void adjustSnappablePanelSize() {
        for (int i = 0; i < snappablePanels.length; i++) {
            snappablePanels[i].setBounds(snapToPanels[i].getBounds());
        }
    }


  public void addSnappablePanel(SnappablePanel snappablePanel, SnapToPanel snapToPanel) {
        snappablePanel.setBounds(snapToPanel.getBounds()); // Set initial position and size
        layeredPane.add(snappablePanel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void removeSnappablePanel(SnappablePanel snappablePanel) {
        layeredPane.remove(snappablePanel);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void snapTo(SnappablePanel snappablePanel, SnapToPanel snapToPanel) {
        // Rectangle bounds = snapToPanel.getBounds();
        // snappablePanel.setBounds(bounds); 

        for (int i = 0; i < snapToPanels.length; i++) {
            if (snapToPanels[i] == snapToPanel) {
                for (int j = 0; j < snappablePanels.length; j++) {
                    if (snappablePanels[j] == snappablePanel) {
                        SnappablePanel tempPanel = snappablePanels[i];
                        Rectangle tempBounds = tempPanel.getBounds();

                        snappablePanels[i] = snappablePanels[j];
                        snappablePanels[i].setBounds(tempBounds);
                        snappablePanels[j] = tempPanel;
                        snappablePanels[j].setBounds(snapToPanels[j].getBounds());
                        layeredPane.moveToFront(snappablePanels[i]);
                        layeredPane.moveToFront(snappablePanels[j]);
                    }
                }
            }
        }


        layeredPane.revalidate();
        layeredPane.repaint();
    }
    
    public void unsnap(SnappablePanel snappablePanel, SnapToPanel snapToPanel) {
        removeSnappablePanel(snappablePanel);
        // You can add additional logic for default positioning if needed
        // For example, add it back with some default bounds
        addSnappablePanel(snappablePanel, snapToPanel);
    }
}

class SnapToPanel extends JPanel {

    int scale;

    SnapToPanel(Color color, int scale) {
        setLayout(new BorderLayout());
        setBackground(color);
        MainPanel mainPanel = (MainPanel) SwingUtilities.getAncestorOfClass(MainPanel.class, this);
        if (mainPanel != null) {
            this.scale = scale;
            setPreferredSize(new Dimension(mainPanel.getWidth() / scale, mainPanel.getHeight()));
        }
    }
}

class SnappablePanel extends JPanel {
    private Point mousePressPoint;
    SnappablePanel(Color color, int scale) {

        setLayout(new BorderLayout());
        MainPanel mainPanel = (MainPanel) SwingUtilities.getAncestorOfClass(MainPanel.class, this);
        if (mainPanel != null) {
            setPreferredSize(new Dimension(mainPanel.getWidth() / scale, mainPanel.getHeight()));
        }
        setBackground(color);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                snapPanel(SnappablePanel.this);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point newPoint = e.getLocationOnScreen();
                SwingUtilities.convertPointFromScreen(newPoint, SnappablePanel.this.getParent());
                newPoint.translate(-mousePressPoint.x, -mousePressPoint.y);
                SnappablePanel.this.setLocation(newPoint);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                snapPanel(SnappablePanel.this);
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private void snapPanel(SnappablePanel panel) {
        MainPanel mainPanel = (MainPanel) SwingUtilities.getAncestorOfClass(MainPanel.class, panel);
        if (mainPanel != null) {
            // Determine the nearest SnapToPanel
            SnapToPanel nearestPanel = mainPanel.snapToPanels[0]; // Default
            int minDistance = Integer.MAX_VALUE;

            for (SnapToPanel snapToPanel : mainPanel.snapToPanels) {
                int distance = calculateDistance(panel.getLocation(), snapToPanel.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPanel = snapToPanel;
                }
            }

            mainPanel.snapTo(panel, nearestPanel);
        }
    }

    private int calculateDistance(Point p1, Point p2) {
        return (int) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }
}
