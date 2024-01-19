import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Widgets {

    Widgets() {
        
    }
}

class ItemWidget extends SnapFromPanel {
    static ItemWidgetPanel itemWidgetPanel;
    private JScrollPane scrollPane;
    ItemWidget(Item item) {
        super(itemWidgetPanel = new ItemWidgetPanel(item), 3, "Item");
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(itemWidgetPanel);
        add(scrollPane);
    }

    ItemWidgetPanel getWidgetPanel() {
        return itemWidgetPanel;
    }

    void updateItem(Item item) {
        itemWidgetPanel.updateItem(item);
    }
}

class ItemWidgetPanel extends JPanel {
    private Item item = null;

    ItemWidgetPanel(Item item) {
        this.item = item;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        updateItem(item);
    }

    class ItemAttributeWidget extends JPanel {
        LibraryComparator.Type itemAttribute = null;

        ItemAttributeWidget(LibraryComparator.Type itemAttribute) {
            this.itemAttribute = itemAttribute;
            if (itemAttribute == null) {
                return;
            }
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 1, 2, false), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
            add(new JPanel() {
                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBackground(Color.WHITE);
                    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
                    add(Box.createHorizontalGlue());
                    add(new JLabel(item.getComparatorTypeName(itemAttribute)));

                    add(Box.createHorizontalGlue());
                }
            }
            );
            add(Box.createHorizontalGlue());
            JTextField nameTextField = new JTextField(item.getInfo(itemAttribute));
            nameTextField.setSize(50, 12);
            nameTextField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // itemAttribute.setName(nameTextField.getText());
                }
            });
            add(nameTextField);
        }

        LibraryComparator.Type getItem() {
            return itemAttribute;
        }
    }

    void drawItem(Item item) {
        add(new JPanel() {
            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
                add(Box.createHorizontalGlue());
                add(new JLabel(item.getName())); // Assuming item is a valid object with getName()
                add(Box.createHorizontalGlue());
            }
        });

        for (LibraryComparator.Type type : LibraryComparator.Type.values()) {
            add(new ItemAttributeWidget(type));
        }

        add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonPanel.add(Box.createHorizontalGlue());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // itemWidgetPanel.updateItem(item);
            }
        });

        buttonPanel.add(deleteButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // itemWidgetPanel.updateItem(item);
            }
        });

        buttonPanel.add(saveButton);

        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel);
    }

    void updateItem(Item item) {
        this.item = item;
        removeAll();
        if (item != null) {
            drawItem(item);
        }
        repaint();
        revalidate();
    }

    Item getItem() {
        return item;
    }
}

class LibraryWidget extends SnapFromPanel {
    static LibraryWidgetPanel libraryWidgetPanel;
    private JScrollPane scrollPane;
    private ItemWidgetPanel itemWidgetPanel;

    LibraryWidget(Library library, ItemWidgetPanel itemWidgetPanel) {
        super(libraryWidgetPanel = new LibraryWidgetPanel(library, itemWidgetPanel), 3, "Library");
        this.itemWidgetPanel = itemWidgetPanel;
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(libraryWidgetPanel);
        add(scrollPane);
    }

    ItemWidgetPanel getItemWidgetPanel() {
        return itemWidgetPanel;
    }

    void updateLibrary(Library library) {
        libraryWidgetPanel.updateLibrary(library);
    }
} 


class LibraryWidgetPanel extends JPanel {
    public Item selectedItem = null;
    public ItemGrid itemGrid = new ItemGrid();
    private ItemWidgetPanel itemWidgetPanel;
    private Library library;
    

    LibraryWidgetPanel(Library library, ItemWidgetPanel itemWidgetPanel) {
        this.library = library;
        this.itemWidgetPanel = itemWidgetPanel;
        selectedItem = library.getSelectedItem();
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

    Item getSelectedItem() {
        return library.getSelectedItem();
    }

    class LibraryItem extends JPanel {
        Item item = null;

        LibraryItem(Item item) {
            this.item = item;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(new JLabel(item.getName()));
        }
        
        Item getItem() {
            return item;
        }

        void select() {
            selectedItem = this.item;
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 1, 2, false), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
            itemWidgetPanel.updateItem(item);
        }

        void deselect() {
            selectedItem = null;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        boolean isSelected() {
            return selectedItem == this.item;
        }

    }

    class ItemGrid extends JPanel {

        ItemGrid() {
            setLayout(new GridLayout(15, 3, 5, 5));
            // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    for (Component component : getComponents()) {
                        if (component instanceof LibraryItem) {
                            if (component.getBounds().contains(e.getPoint())) {
                                if (((LibraryItem) component).isSelected()) {
                                    ((LibraryItem) component).deselect();
                                    library.setSelectedItem(null);
                                } else {
                                    ((LibraryItem) component).select();
                                    library.setSelectedItem(((LibraryItem) component).getItem());
                                }
                            } else {
                                ((LibraryItem) component).deselect();
                            }
                        }
                    }
                }
            });
        }

        void addItem(Item item) {
            add(new LibraryItem(item));
            repaint();
            revalidate();
        }

        void selectItem(Item item) {
            for (Component component : getComponents()) {
                if (component instanceof LibraryItem) {
                    if (((LibraryItem) component).getItem() == item) {
                        ((LibraryItem) component).select();
                    } else {
                        ((LibraryItem) component).deselect();
                    }
                }
            }
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