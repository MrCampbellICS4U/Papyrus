import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Snappable panel class is a driver class for the snapping Panel system
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
        // TODO: Make this work with fullscreen
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustPanelSizes();
                adjustSnapFromPanelSizes();
            }
        });

        adjustPanelSizes(); // Initial adjustment
    }

    /**
     * Adjusts the sizes of the snapToPanels based on the size of the SnappablePanels
     */
    public void adjustPanelSizes() {
        int totalWidth = getWidth();
        int countWidth = 0;
        for (int i = 0; i < snapToPanels.length; i++) {
            int panelWidth = totalWidth / snapToPanels[i].scale;
            snapToPanels[i].setBounds(countWidth, 0, panelWidth, getHeight());
            countWidth += panelWidth;
        }
    }

    /**
     * Adjusts the sizes of the snapFromPanels based on the size of the snapToPanels
     */
    public void adjustSnapFromPanelSizes() {
        for (int i = 0; i < snappablePanels.length; i++) {
            snappablePanels[i].setBounds(snapToPanels[i].getBounds());
        }
    }

    /**
     * Adds a snapFromPanel to the layeredPane
     * @param snapFromPanel
     * @param snapToPanel
     */
    public void addSnapFromPanel(SnapFromPanel snapFromPanel, SnapToPanel snapToPanel) {
            snapFromPanel.setBounds(snapToPanel.getBounds());
            // put the snapFromPanel on the draggable layer
            layeredPane.add(snapFromPanel, JLayeredPane.DRAG_LAYER);
            layeredPane.revalidate();
            layeredPane.repaint();
    }

    /**
     * Removes a snapFromPanel from the layeredPane
     * @param snappablePanel
     */
    public void removeSnapFromPanel(SnapFromPanel snappablePanel) {
        layeredPane.remove(snappablePanel);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /**
     * Snaps a snapFromPanel to a snapToPanel
     * @param snappablePanel
     * @param snapToPanel
     */
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

    /**
     * Unsnaps a snapFromPanel from a snapToPanel
     * @param snappablePanel
     * @param snapToPanel
     */
    public void unsnap(SnapFromPanel snappablePanel, SnapToPanel snapToPanel) {
        removeSnapFromPanel(snappablePanel);
        addSnapFromPanel(snappablePanel, snapToPanel);
    }
}


// SnapToPanel and SnapFromPanel classes are the panels that are snapped together
// SnapToPanel is the panel that is snapped to, these are three fixed panels
class SnapToPanel extends JPanel {

    int scale;

    SnapToPanel(Color color, int scale) {
        setLayout(new BorderLayout());
        setBackground(color);
        this.scale = scale;
    }
}

class SnapFromPanel extends JPanel {
    // Define the point where the mouse is pressed
    private Point mousePressPoint;
    private TopDragPanel topDragPanel = new TopDragPanel();
    boolean dragging = false;
    
    /**
     * Constructor for SnapFromPanel, adds the panel that is desired to be snapped to the layout
     * @param panel
     * @param scale
     */
    SnapFromPanel(JPanel panel, int scale) {
        setLayout(new BorderLayout());
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

    /**
     * Sets the text of the topDragPanel
     * @param text
     */
    void setText(String text) {
        topDragPanel.removeAll();
        topDragPanel.add(topDragPanel.dragButton, BorderLayout.WEST); 
        topDragPanel.add(new JLabel(text), BorderLayout.CENTER);
        topDragPanel.revalidate();
        topDragPanel.repaint();
    }

    /**
     * Snaps the panel to the nearest snapToPanel
     * @param panel
     */
    private void snapPanel(SnapFromPanel panel) {
        SnappablePanel mainPanel = (SnappablePanel) SwingUtilities.getAncestorOfClass(SnappablePanel.class, panel);
        if (mainPanel != null) {
            // Determine the nearest SnapToPanel
            SnapToPanel nearestPanel = mainPanel.snapToPanels[0]; // Default
            int minDistance = Integer.MAX_VALUE;

            for (SnapToPanel snapToPanel : mainPanel.snapToPanels) {
                // Calculate the distance between the two panels
                int distance = calculateDistance(panel.getLocation(), snapToPanel.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPanel = snapToPanel;
                }
            }

            mainPanel.snapTo(panel, nearestPanel);
        }
    }
    
    /**
     * Calculates the distance between two points
     * @param p1
     * @param p2
     * @return
     */
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
            /**
             * Constructor for the DragButton class
             */
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
                /**
                 * Handles the mouse events for the DragButton
                 * @param e
                 */
                @Override
                public void mousePressed(MouseEvent e) {
                    mousePressPoint = e.getPoint();
                    dragging = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    snapPanel(SnapFromPanel.this);
                }

                /**
                 * Handles the mouse events for the DragButton
                 */
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!dragging) {
                        return;
                    }
                    Point newPoint = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(newPoint, SnapFromPanel.this.getParent());
                    newPoint.translate(-mousePressPoint.x , -mousePressPoint.y);
                    SnapFromPanel.this.setLocation(newPoint);
                }

                /**
                 * Handles the mouse events for the DragButton, snaps the panel to the nearest snapToPanel when the mouse is released
                 * @param e
                 */
                @Override
                public void mouseReleased(MouseEvent e) {
                    dragging = false;
                    setCursor(Cursor.getDefaultCursor());
                    snapPanel(SnapFromPanel.this);
                }
            };

            // Add the mouse handler to the DragButton, in a way that it will only be added once
            {
                addMouseListener(mouseHandler);
                addMouseMotionListener(mouseHandler);
            }
        }
    }
}