import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;

// ItemWidget class that is used to display an item in the UI
class ItemWidget extends SnapFromPanel {
    static ItemWidgetPanel itemWidgetPanel;
    private JScrollPane scrollPane;

    /**
     * Constructor that accepts an Item object
     * @param item
     */
    ItemWidget(Item item) {
        super(itemWidgetPanel = new ItemWidgetPanel(item), 3, item == null ? " " : item.getName());
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(itemWidgetPanel);
        add(scrollPane);
    }

    /**
     * Returns the ItemWidgetPanel object
     * @return ItemWidgetPanel
     */
    ItemWidgetPanel getWidgetPanel() {
        return itemWidgetPanel;
    }

    /**
     * Updates the item in the ItemWidgetPanel
     * @param item
     */
    void updateItem(Item item) {
        itemWidgetPanel.updateItem(item);

    }

    /**
     * Returns the Item object
     * @return Item
     */
    Item getItem() {
        return itemWidgetPanel.getItem();
    }

    /**
     * Sets the Item object
     * @param item
     * @return Item
     */
    Item setNewItem(Item item) {
        itemWidgetPanel.updateItem(item);
        return itemWidgetPanel.getItem();
    }
}

// ItemWidgetPanel class that is used to be put inside the ItemWidget class (i.e. the panel that goes inside the snap panel)
class ItemWidgetPanel extends JPanel {
    private Item item = null;

    /**
     * Constructor that accepts an Item object
     * @param item
     */
    ItemWidgetPanel(Item item) {
        this.item = item;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        setBackground(Color.WHITE);
        updateItem(item);
    }

    // ItemAttributeWidget class that is used to display an item attribute in the Item Widget Panel, whcih is editable
    class ItemAttributeWidget extends JPanel {
        LibraryComparator.Type itemAttribute = null;
        DatePanel dateAddedPanel = null;
        DatePanel datePublishedPanel = null;
        /**
         * Constructor that accepts a LibraryComparator.Type object
         * @param itemAttribute
         */
        ItemAttributeWidget(LibraryComparator.Type itemAttribute) {
            this.itemAttribute = itemAttribute;
            if (itemAttribute == null || item == null || item.getComparatorTypeName(itemAttribute) == null) {
                setVisible(false);
                return;
            }

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // add panel with label and text field for each item attribute
            add(new JPanel() {
                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBackground(Color.WHITE);
                    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                    
                    add(Box.createHorizontalGlue());
                    add(new JLabel(item.getComparatorTypeName(itemAttribute)));

                    add(Box.createHorizontalGlue());
                }
            });

            add(Box.createHorizontalGlue());
            if (itemAttribute == LibraryComparator.Type.DATEADDED || itemAttribute == LibraryComparator.Type.DATEPUBLISHED) {
                if (itemAttribute == LibraryComparator.Type.DATEADDED) {
                    System.out.println(item.getInfo(itemAttribute));
                    dateAddedPanel = new DatePanel(DatePanel.fromString(item.getInfo(itemAttribute)));
                    add(dateAddedPanel);
                    dateAddedPanel.addDateChangeListener(item, itemAttribute);
                } else {
                    System.out.println(item.getInfo(itemAttribute));

                    datePublishedPanel = new DatePanel(DatePanel.fromString(item.getInfo(itemAttribute)));
                    add(datePublishedPanel);
                    datePublishedPanel.addDateChangeListener(item, itemAttribute);
                }
            } else {
            JTextField nameTextField = new JTextField(item.getInfo(itemAttribute));
            nameTextField.setSize(50, 12);

            // THESE FIVE LINES TOOK ME 40 MINUTES AHAHAHASHAH
            nameTextField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                }
            
                public void focusLost(FocusEvent e) {
                    item.setInfo(itemAttribute, nameTextField.getText());
                }
            });

            add(nameTextField);
            }
        }

        /**
         * Returns the LibraryComparator.Type object
         * @return LibraryComparator.Type
         */
        LibraryComparator.Type getItem() {
            return itemAttribute;
        }
    }

    /**
     * Draws the item in the ItemWidgetPanel
     * @param item
     */
    void drawItem(Item item) {
        add(new JPanel() {
            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
                add(Box.createHorizontalGlue());
                add(new JLabel(item.getName())); 
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
        // button to delete item
        deleteButton.addActionListener(new ActionListener() {
            /**
             * Deletes the item from the library
             * @param e
             */
            public void actionPerformed(ActionEvent e) {
                Library library = LibraryWidget.libraryWidgetPanel.getLibrary();
                if (!library.contains(item)) {
                    return;
                }
                library.remove(item);
                System.out.println("Deleted item: " + item.getName());
                LibraryWidgetPanel libraryWidgetPanel = LibraryWidget.libraryWidgetPanel;
                libraryWidgetPanel.refresh();
                libraryWidgetPanel.getLibrary().setSelectedItem(null);

                FileTreeWidget.fileTreeWidgetPanel.fromLibrary(libraryWidgetPanel.getLibrary());
                revalidate();
                repaint();
            }
        });

        buttonPanel.add(deleteButton);
        // button to save item
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            /**
             * Saves the item to the library
             * @param e
             */
            public void actionPerformed(ActionEvent e) {
                saveEditedItem();
            }
        });

        buttonPanel.add(saveButton);

        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel);
    }

    /**
     * Updates the item in the ItemWidgetPanel
     * @param item
     */
    private void saveEditedItem() {
        LibraryWidgetPanel.ItemGrid.refresh();
        revalidate();
        repaint();
    }

    /**
     * Updates the item in the ItemWidgetPanel
     * @param item
     */
    void updateItem(Item item) {
        this.item = item;
        removeAll();
        if (item != null) {
            SnapFromPanel itemWidget = (ItemWidget) SwingUtilities.getAncestorOfClass(ItemWidget.class, this);
            if (itemWidget != null) {
                itemWidget.setText(item.getName());
            }
            drawItem(item);
        }
        repaint();
        revalidate();
    }

    /**
     * Returns the Item object
     * @return Item
     */
    Item getItem() {
        return item;
    }
}

class LibraryWidget extends SnapFromPanel {
    static LibraryWidgetPanel libraryWidgetPanel;
    private JScrollPane scrollPane;
    private ItemWidgetPanel itemWidgetPanel;

    /**
     * Constructor that accepts a Library object and an ItemWidgetPanel object
     * @param library
     * @param itemWidgetPanel
     */
    LibraryWidget(Library library, ItemWidgetPanel itemWidgetPanel) {
        super(libraryWidgetPanel = new LibraryWidgetPanel(library, itemWidgetPanel), 3, library.getName());
        this.itemWidgetPanel = itemWidgetPanel;
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(libraryWidgetPanel);
        add(scrollPane);
    }

    /**
     * Returns the ItemWidgetPanel object
     * @return ItemWidgetPanel
     */
    ItemWidgetPanel getItemWidgetPanel() {
        return itemWidgetPanel;
    }

    /**
     * Returns the LibraryWidgetPanel object
     * @return LibraryWidgetPanel
     */
    LibraryWidgetPanel getWidgetPanel() {
        return libraryWidgetPanel;
    }

    /**
     * Returns the Library object
     * @return Library
     */
    Library getLibrary() {
        return libraryWidgetPanel.getLibrary();
    }

    /**
     * Updates the library in the LibraryWidgetPanel
     * @param library
     */
    void updateLibrary(Library library) {
        libraryWidgetPanel.updateLibrary(library);
    }
} 

// LibraryWidgetPanel class that is used to be put inside the LibraryWidget class (i.e. the panel that goes inside the snap panel)
class LibraryWidgetPanel extends JPanel {
    public Item selectedItem = null;
    public ItemGrid itemGrid = new ItemGrid();
    private ItemWidgetPanel itemWidgetPanel;
    private Library library;
    ItemSortWidget itemSortWidget = new ItemSortWidget();    

    /**
     * Constructor that accepts a Library object and an ItemWidgetPanel object
     * @param library
     * @param itemWidgetPanel
     */
    LibraryWidgetPanel(Library library, ItemWidgetPanel itemWidgetPanel) {
        this.library = library;
        this.itemWidgetPanel = itemWidgetPanel;
        selectedItem = library.getSelectedItem();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        itemGrid.add(itemSortWidget);
        add(itemGrid);
    }

    /**
     * ItemSortWidget class that is used to display the sorting options in the LibraryWidgetPanel
     */
    class ItemSortWidget extends JPanel {
        ItemSortWidget() {
            add(new JPanel() {
                {
                    setLayout(new GridLayout(1, 4));
                    JLabel nameLabel = new JLabel("Name");
                    // to sort a widget, simply click on the label of the attribute you want to sort by, this sorts the library in ascending order
                    // and then replace the old library with the new sorted library, and then refresh the library
                    nameLabel.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("Sorting by name");
                            library = library.sort(LibraryComparator.Type.NAME);
                            LibraryWidget.libraryWidgetPanel.updateLibrary(library);
                            refresh();
                        }
                    });
                    add(nameLabel);
                    add(Box.createHorizontalGlue());
                    add(drawVerticalLine());
                    JLabel authorLastLabel = new JLabel("Author Last");
                    authorLastLabel.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("Sorting by author last");
                            library = library.sort(LibraryComparator.Type.AUTHORLAST);
                            LibraryWidget.libraryWidgetPanel.updateLibrary(library);
                            refresh();
                        }
                    });
                    add(authorLastLabel);
                    add(Box.createHorizontalGlue());
                    add(drawVerticalLine());
                    JLabel authorFirstLabel = new JLabel("Author First");
                    authorFirstLabel.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("Sorting by author first");
                            library = library.sort(LibraryComparator.Type.AUTHORFIRST);
                            LibraryWidget.libraryWidgetPanel.updateLibrary(library);
                            refresh();
                        }
                    });
                    add(authorFirstLabel);
                    add(Box.createHorizontalGlue());
                    add(drawVerticalLine());
                    JLabel dateLabel = new JLabel("Date Created");
                    dateLabel.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("Sorting by date created");
                            library = library.sort(LibraryComparator.Type.DATEADDED);
                            LibraryWidget.libraryWidgetPanel.updateLibrary(library);
                            refresh();
                        }
                    });
                    add(dateLabel);
                }
            }
            );
        }
    }

    /**
     * Draws a vertical line
     * this is basically useless but i'm keeping it here just in case
     * @return JPanel
     */
    JPanel drawVerticalLine() {
        return new JPanel() {
            {
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                setOpaque(false);
                add(Box.createVerticalGlue());
                add(new JPanel() {
                    {
                        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                        add(Box.createHorizontalGlue());
                        add(Box.createHorizontalGlue());
                    }
                });
                add(Box.createVerticalGlue());
            }
        };
    }

    /**
     * Refreshes the library in the LibraryWidgetPanel
     */
    void updateLibrary(Library library) {
        this.library = library;
        System.out.println("Updating library: " + library.getName());
        selectedItem = library.getSelectedItem();
        itemGrid.removeAll();
        itemGrid.add(itemSortWidget);
        for (Item item : library) {
            itemGrid.addItem(item);
            System.out.println(item.getName());
        }

        LibraryWidget libraryWidget = (LibraryWidget) SwingUtilities.getAncestorOfClass(LibraryWidget.class, this);
        if (libraryWidget != null) {
            libraryWidget.setText(library.getName());
        }

        repaint();
        revalidate();
    }

    /**
     * Refreshes the library in the LibraryWidgetPanel
     */
    void refresh() {
        System.out.println("Refreshing library: " + library.getName());
        selectedItem = library.getSelectedItem();
        for (Component component : itemGrid.getComponents()) {
            if (component instanceof LibraryItem) {
                ((LibraryItem) component).refresh();
            }
        }
    }

    /**
     * Returns the Library object
     * @return Library
     */
    Library getLibrary() {
        return library;
    }

    /**
     * Returns the selected item
     * @return Item
     */
    Item getSelectedItem() {
        return library.getSelectedItem();
    }

    // LibraryItem class that is used to display an item in the LibraryWidgetPanel
    class LibraryItem extends JPanel {
        Item item = null;

        /**
         * Constructor that accepts an Item object, draws an item in the LibraryWidgetPanel
         * @param item
         */
        LibraryItem(Item item) {
            this.item = item;
            setLayout(new GridLayout(1, 4));
            setBackground(Color.WHITE);
            JLabel nameLabel = new JLabel(item.getName());
            add(nameLabel);
            add(Box.createHorizontalGlue());
            add(drawVerticalLine());
            JLabel authorLastLabel = new JLabel(item.getAuthorLast());
            add(authorLastLabel);
            add(Box.createHorizontalGlue());
            add(drawVerticalLine());
            JLabel authorFirstLabel = new JLabel(item.getAuthorFirst());
            add(authorFirstLabel);
            add(Box.createHorizontalGlue());
            add(drawVerticalLine());
            JPanel datePanel = new DatePanel();
            add(datePanel);
        }
        
        /**
         * Returns the Item object
         * @return Item
         */
        Item getItem() {
            return item;
        }

        /**
         * Selects the item in the LibraryWidgetPanel
         */
        void select() {
            selectedItem = this.item;
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 1, 2, false), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
            itemWidgetPanel.updateItem(item);
        }

        /**
         * Deselects the item in the LibraryWidgetPanel
         */
        void deselect() {
            selectedItem = null;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        
        /**
         * Returns true if the item is selected, false otherwise
         * @return boolean
         */
        boolean isSelected() {
            return selectedItem == this.item;
        }

        /**
         * Refreshes the LibraryWidgetPanel
         */
        void refresh() {
            removeAll();
            setLayout(new GridLayout(1, 4));
            JLabel nameLabel = new JLabel(item.getName());
            add(nameLabel);
            add(Box.createHorizontalGlue());
            add(drawVerticalLine());
            JLabel authorLastLabel = new JLabel(item.getAuthorLast());
            add(authorLastLabel);
            add(Box.createHorizontalGlue());
            add(drawVerticalLine());
            JLabel authorFirstLabel = new JLabel(item.getAuthorFirst());
            add(authorFirstLabel);
            add(Box.createHorizontalGlue());
            add(drawVerticalLine());
            JLabel dateLabel = new JLabel(item.getDateAdded() == null ? "" : item.getDateAdded().toString());
            add(dateLabel);
        }
    }

    class ItemGrid extends JPanel {

        ItemGrid() {
            setLayout(new GridLayout(15, 3, 5, 5));
            // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            /**
             * Adds a mouse listener to the ItemGrid
             * If the mouse is pressed on a LibraryItem, then the item is selected
             */
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

        /**
         * Refreshes the item grid
         * @param library
         */
        static void refresh() {
            System.out.println("Refreshing item grid");
            LibraryWidgetPanel libraryWidgetPanel = LibraryWidget.libraryWidgetPanel;
            for (Component component : libraryWidgetPanel.itemGrid.getComponents()) {
                if (component instanceof LibraryItem) {
                    ((LibraryItem) component).refresh();
                }
            }

            libraryWidgetPanel.itemGrid.revalidate();
            libraryWidgetPanel.itemGrid.repaint();
        }

        /**
         * Adds an item to the LibraryWidgetPanel, as a textfield, and then adds the item to the library
         * @param libraryWidgetPanel
         */
        static void addBlankItem(LibraryWidgetPanel libraryWidgetPanel) {
            System.out.println("Adding blank item");
    
            JTextField inputField = new JTextField(10);
            inputField.setMaximumSize(inputField.getPreferredSize());
            inputField.addActionListener(e -> {
                String itemName = inputField.getText();
                if (!itemName.isEmpty()) {
                    Item newItem = new Item(itemName);
                    
                    libraryWidgetPanel.itemGrid.addItem(newItem);
                    
                    libraryWidgetPanel.itemGrid.remove(inputField);

                    libraryWidgetPanel.library.add(newItem);
                    libraryWidgetPanel.refresh(); 
                                        FileTreeWidget.fileTreeWidgetPanel.fromLibrary(libraryWidgetPanel.library);
                    FileTreeWidget.fileTreeWidgetPanel.refresh(libraryWidgetPanel.library);

                    LibraryWidget.libraryWidgetPanel.updateLibrary(libraryWidgetPanel.library);
                    LibraryWidget.libraryWidgetPanel.refresh(); 

                    libraryWidgetPanel.itemGrid.revalidate();
                    libraryWidgetPanel.itemGrid.repaint();
                }
            });
    
            libraryWidgetPanel.itemGrid.add(inputField);
            libraryWidgetPanel.itemGrid.revalidate();
            libraryWidgetPanel.itemGrid.repaint();
            // focusing is important
            inputField.requestFocusInWindow();
        }
    
        /**
         * Adds an item to the LibraryWidgetPanel
         * @param item
         */
        void addItem(Item item) {
            add(new LibraryItem(item));
            repaint();
            revalidate();
        }

        /**
         * Selects an item in the LibraryWidgetPanel
         * @param item
         */
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

        /**
         * Removes an item from the LibraryWidgetPanel
         * @param item
         */
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

// FileTreeWidget class that is used to display the file tree in the UI
class FileTreeWidget extends SnapFromPanel {
    private JScrollPane scrollPane;
    static FileTreeWidgetPanel fileTreeWidgetPanel;

    /**
     * Constructor that accepts a FileTree object
     * @param fileTree
     */
    FileTreeWidget(FileTree fileTree) {
        super(fileTreeWidgetPanel = new FileTreeWidgetPanel(fileTree), 3, "File Tree");
        scrollPane = new JScrollPane(fileTreeWidgetPanel);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
}

// FileTreeWidgetPanel class that is used to be put inside the FileTreeWidget class
class FileTreeWidgetPanel extends JPanel {
    private FileTree fileTree;

    /**
     * Constructor that accepts a FileTree object
     * @param fileTree
     */
    FileTreeWidgetPanel(FileTree fileTree) {
        this.fileTree = fileTree;
        setLayout(new BorderLayout());
        add(fileTree, BorderLayout.CENTER);
    }

    /**
     * Returns the FileTree object
     * @return FileTree
     */
    void fromLibrary(Library library) {
        fileTree.fromLibrary(library);
    }

    /**
     * Refreshes the FileTree object
     * @param library
     */
    void refresh(Library library) {
        fileTree.fromLibrary(library);
    }
}

// LibraryWidget class that is used to display the library in the UI
class FileTree extends JTree {
    private JPanel panel;
    private JTextField editor;
    private DefaultMutableTreeNode editableNode;

    /**
     * Constructor that creates a DefaultMutableTreeNode object
     * @param root
     */
    FileTree() {
        super(new DefaultMutableTreeNode("Papyrus"));
        createTreeNodes();
        editor = new JTextField();
        editor.setVisible(false);
        editor.addActionListener(e -> renameNode());
        add(editor);

        addMouseListener(new MouseAdapter() {
            /**
             * If the node is double clicked, then the node is editable
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        editableNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Rectangle nodeBounds = getPathBounds(path);
                        editor.setBounds(nodeBounds);
                        editor.setText(editableNode.toString());
                        editor.setVisible(true);
                        editor.requestFocusInWindow();
                    }
                }
            }
        });
    }

    /**
     * Renames the node
     */
    private void renameNode() {
        if (editableNode != null) {
            String newName = editor.getText();
            editableNode.setUserObject(newName);
            ((DefaultTreeModel) getModel()).nodeChanged(editableNode);
            editor.setVisible(false);
            
            LibraryWidgetPanel libraryWidgetPanel = LibraryWidget.libraryWidgetPanel;
            libraryWidgetPanel.getLibrary().setName(newName);
            libraryWidgetPanel.refresh();
    
            editableNode = null;
        }
    }

    /**
     * updates the model of the tree from the library
     * @param library
     */
    public void fromLibrary(Library library) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(library.getName());
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        (LibraryWidget.libraryWidgetPanel).updateLibrary(library);
        model.setRoot(root);

        for (Item item : library) {
            DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(item.getName());
            root.add(itemNode); 
        }

        model.reload(); 
    }

    /**
     * Creates the tree nodes
     */
    private void createTreeNodes() {
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        // // Example to add a child node. Ppl u guys might want to dynamically add nodes based on files idk what we fina do wit dis.
        // root.add(new DefaultMutableTreeNode("child"));

        model.reload();
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Returns the JPanel object
     * @return JPanel
     */
    void setPanel(JPanel panel) {
        this.panel = panel;
    }
}