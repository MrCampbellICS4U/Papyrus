import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class SnappablePanel extends JPanel {
    JLayeredPane layeredPane = new JLayeredPane();
    SnapToPanel[] snapToPanels = new SnapToPanel[3];
    SnapFromPanel[] snappablePanels;
    
    SnappablePanel(SnapFromPanel[] snappablePanels) {
        this.snappablePanels = snappablePanels;
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
            addSnapFromPanel(snappablePanels[i], snapToPanels[i]);
        }

        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustPanelSizes();
                adjustSnapFromPanelSizes();
            }
        });

        adjustPanelSizes(); // Initial adjustment
    }

    public void adjustPanelSizes() {
        int totalWidth = getWidth();
        int countWidth = 0;
        for (int i = 0; i < snapToPanels.length; i++) {
            int panelWidth = totalWidth / snapToPanels[i].scale;
            snapToPanels[i].setBounds(countWidth, 0, panelWidth, getHeight());
            countWidth += panelWidth;
        }
    }

    public void adjustSnapFromPanelSizes() {
        for (int i = 0; i < snappablePanels.length; i++) {
            snappablePanels[i].setBounds(snapToPanels[i].getBounds());
        }
    }


  public void addSnapFromPanel(SnapFromPanel snapFromPanel, SnapToPanel snapToPanel) {
        snapFromPanel.setBounds(snapToPanel.getBounds()); // Set initial position and size
        layeredPane.add(snapFromPanel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void removeSnapFromPanel(SnapFromPanel snappablePanel) {
        layeredPane.remove(snappablePanel);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void snapTo(SnapFromPanel snappablePanel, SnapToPanel snapToPanel) {
        // Rectangle bounds = snapToPanel.getBounds();
        // snappablePanel.setBounds(bounds); 

        for (int i = 0; i < snapToPanels.length; i++) {
            if (snapToPanels[i] == snapToPanel) {
                for (int j = 0; j < snappablePanels.length; j++) {
                    if (snappablePanels[j] == snappablePanel) {
                        SnapFromPanel tempPanel = snappablePanels[i];
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
    
    public void unsnap(SnapFromPanel snappablePanel, SnapToPanel snapToPanel) {
        removeSnapFromPanel(snappablePanel);
        // You can add additional logic for default positioning if needed
        // For example, add it back with some default bounds
        addSnapFromPanel(snappablePanel, snapToPanel);
    }
}

class SnapToPanel extends JPanel {

    int scale;

    SnapToPanel(Color color, int scale) {
        setLayout(new BorderLayout());
        setBackground(color);
        this.scale = scale;
    }
}

class SnapFromPanel extends JPanel {
    private Point mousePressPoint;
    
    private TopDragPanel topDragPanel = new TopDragPanel();
    boolean dragging = false;
    
    SnapFromPanel(JPanel panel, int scale) {


        setLayout(new BorderLayout());
        SnappablePanel mainPanel = (SnappablePanel) SwingUtilities.getAncestorOfClass(SnappablePanel.class, this);
        if (mainPanel != null) {
            setPreferredSize(new Dimension(mainPanel.getWidth() / scale, mainPanel.getHeight()));
        }

        add(panel, BorderLayout.CENTER);
        add(topDragPanel, BorderLayout.NORTH);
    }

    SnapFromPanel(JPanel panel) {
        this(panel, 4);
    }

    SnapFromPanel(JPanel panel, int scale, String text) {
        this(panel, scale);
        topDragPanel = new TopDragPanel(text);
        add(topDragPanel, BorderLayout.NORTH);
    }

    SnapFromPanel() {
        this(new JPanel(), 4);
    }

    void setText(String text) {
        topDragPanel.removeAll();
        topDragPanel.add(topDragPanel.dragButton, BorderLayout.WEST); 
        topDragPanel.add(new JLabel(text), BorderLayout.CENTER);
        topDragPanel.revalidate();
        topDragPanel.repaint();
    }

    private void snapPanel(SnapFromPanel panel) {
        SnappablePanel mainPanel = (SnappablePanel) SwingUtilities.getAncestorOfClass(SnappablePanel.class, panel);
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

    class TopDragPanel extends JPanel {
        DragButton dragButton = new DragButton();

        TopDragPanel(String text) {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 25));
            setLayout(new BorderLayout());
            setBackground(Color.GRAY);
            add(new JLabel(text), BorderLayout.CENTER);
            add(dragButton, BorderLayout.WEST);
        }

        TopDragPanel() {
            this("");
        }

        class DragButton extends JButton {
            DragButton() {
                setPreferredSize(new Dimension(35, 25));
                setMargin(new Insets(0, 0, 0, 10));
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                setIcon(new ImageIcon("src/drag-icon.png"));
            }

            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mousePressPoint = e.getPoint();
                    dragging = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    snapPanel(SnapFromPanel.this);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!dragging) {
                        return;
                    }
                    Point newPoint = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(newPoint, SnapFromPanel.this.getParent());
                    newPoint.translate(-mousePressPoint.x , -mousePressPoint.y );
                    SnapFromPanel.this.setLocation(newPoint);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragging = false;
                    setCursor(Cursor.getDefaultCursor());
                    snapPanel(SnapFromPanel.this);
                }
            };

            {
                addMouseListener(mouseHandler);
                addMouseMotionListener(mouseHandler);
            }
        }
    }
}