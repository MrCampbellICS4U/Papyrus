import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Widgets {

    Widgets() {
        
    }
}

class ItemWidget extends SnapFromPanel {
    static ItemWidgetPanel ItemWidgetPanel;
    private JScrollPane scrollPane;

    ItemWidget(Item item) {
        super(ItemWidgetPanel = new ItemWidgetPanel(item), 3, "Item");
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(ItemWidgetPanel);
        add(scrollPane);
    }

    
}

class ItemWidgetPanel extends JPanel {
    

    ItemWidgetPanel(Item item) {
        
    }
    
    void updateItem(Item item) {
        removeAll();
        add(new ItemPanel(item));
        repaint();
        revalidate();
    }
}

class LibraryWidget extends SnapFromPanel {
    static LibraryWidgetPanel libraryWidgetPanel;
    private JScrollPane scrollPane;
    LibraryWidget(Library library) {
        super(libraryWidgetPanel = new LibraryWidgetPanel(library), 3, "Library");
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(libraryWidgetPanel);
        add(scrollPane);
        
    }

    void updateLibrary(Library library) {
        libraryWidgetPanel.updateLibrary(library);
    }
} 


class LibraryWidgetPanel extends JPanel {

    public LibraryItem selectedItem = null;
    public ItemGrid itemGrid = new ItemGrid();

    LibraryWidgetPanel(Library library) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        add(itemGrid);
    }

    void updateLibrary(Library library) {
        for (Item item : library) {
            itemGrid.addItem(item);
            System.out.println(item.getName());
        }
        repaint();
        revalidate();
    }

    class LibraryItem extends JPanel {
        Item item = null;

        LibraryItem(Item item) {
            this.item = item;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(new JLabel(item.getName()));

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (selectedItem != null) {
                        selectedItem.deselect();
                    }
                    select();
                }
            });

            addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (selectedItem != null) {
                            selectedItem.deselect();
                        }
                    }
                }
            });
        }
        
        Item getItem() {
            return item;
        }

        void select() {
            selectedItem = this;
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 1, 2, false), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        }

        void deselect() {
            selectedItem = null;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder());
        }

        boolean isSelected() {
            return selectedItem == this;
        }

    }

    class ItemGrid extends JPanel {

        ItemGrid() {
            setLayout(new GridLayout(15, 3, 5, 5));
            // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
        }

        void addItem(Item item) {
            add(new LibraryItem(item));
            repaint();
            revalidate();
        }

        void removeItem(Item item) {
            for (Component component : getComponents()) {
                if (component instanceof LibraryItem) {
                    if (((LibraryItem) component).getItem() == item) {
                        remove(component);
                        repaint();
                        revalidate();
                        return;
                    }
                }
            }
        }
    }

}


class Widget1 extends SnapFromPanel {
    Widget1() {
        super(new Widget1Panel(),3, "Widget 1");
    }
}

class Widget1Panel extends JPanel {
    Widget1Panel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(new JLabel("Widget 1"), BorderLayout.NORTH);
    }
}

class Widget2 extends SnapFromPanel {
    Widget2() {
        super(new Widget2Panel(),3, "Widget 2");
    }
}

class Widget2Panel extends JPanel {
    Widget2Panel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(new JLabel("Widget 2"), BorderLayout.NORTH);
    }
}

class Widget3 extends SnapFromPanel {
    Widget3() {
        super(new Widget3Panel(),3, "Widget 3");
    }
}

class Widget3Panel extends JPanel {
    Widget3Panel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(new JLabel("Widget 3"), BorderLayout.NORTH);
    }
}

