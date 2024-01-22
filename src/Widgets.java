import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;

class ItemWidget extends SnapFromPanel {
    static ItemWidgetPanel itemWidgetPanel;
    private JScrollPane scrollPane;
    ItemWidget(Item item) {
        super(itemWidgetPanel = new ItemWidgetPanel(item), 3, item == null ? " " : item.getName());
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

    Item getItem() {
        return itemWidgetPanel.getItem();
    }

    Item setNewItem(Item item) {
        itemWidgetPanel.updateItem(item);
        return itemWidgetPanel.getItem();
    }
}

class ItemWidgetPanel extends JPanel {
    private Item item = null;
    private Item oldItem = null;
    ItemWidgetPanel(Item item) {
        this.oldItem = item;
        this.item = item;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        updateItem(item);
    }


    class ItemAttributeWidget extends JPanel {
        LibraryComparator.Type itemAttribute = null;

        ItemAttributeWidget(LibraryComparator.Type itemAttribute) {
            this.itemAttribute = itemAttribute;
            if (itemAttribute == null || item == null || item.getComparatorTypeName(itemAttribute) == null) {
                setVisible(false);
                return;
            }

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 1, 2, false));
            add(new JPanel() {
                {
                    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                    setBackground(Color.WHITE);
                    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                    
                    add(Box.createHorizontalGlue());
                    add(new JLabel(item.getComparatorTypeName(itemAttribute)));

                    add(Box.createHorizontalGlue());
                }
            }
            );
            add(Box.createHorizontalGlue());
            JTextField nameTextField = new JTextField(item.getInfo(itemAttribute));
            nameTextField.setSize(50, 12);
            nameTextField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                }
            
                public void focusLost(FocusEvent e) {
                    item.setInfo(itemAttribute, nameTextField.getText());
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
        deleteButton.addActionListener(new ActionListener() {
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

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEditedItem(item);
                System.out.println("Saved item: " + item.getName());
            }
        });

        buttonPanel.add(saveButton);

        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel);
    }

    private void saveEditedItem(Item item) {
        Library library = LibraryWidget.libraryWidgetPanel.getLibrary();
        if (!library.contains(oldItem)) {
            return;
        }
        library.remove(oldItem);
        library.add(item);
        System.out.println("Saved item: " + item.getName());
        LibraryWidgetPanel libraryWidgetPanel = LibraryWidget.libraryWidgetPanel;
        libraryWidgetPanel.refresh();
        FileTreeWidget.fileTreeWidgetPanel.fromLibrary(libraryWidgetPanel.getLibrary());

        revalidate();
        repaint();

    }


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

    Item getItem() {
        return item;
    }
}

class LibraryWidget extends SnapFromPanel {
    static LibraryWidgetPanel libraryWidgetPanel;
    private JScrollPane scrollPane;
    private ItemWidgetPanel itemWidgetPanel;

    LibraryWidget(Library library, ItemWidgetPanel itemWidgetPanel) {
        super(libraryWidgetPanel = new LibraryWidgetPanel(library, itemWidgetPanel), 3, library.getName());
        this.itemWidgetPanel = itemWidgetPanel;
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(libraryWidgetPanel);
        add(scrollPane);
    }

    ItemWidgetPanel getItemWidgetPanel() {
        return itemWidgetPanel;
    }

    LibraryWidgetPanel getWidgetPanel() {
        return libraryWidgetPanel;
    }

    Library getLibrary() {
        return libraryWidgetPanel.getLibrary();
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
    ItemSortWidget itemSortWidget = new ItemSortWidget();    

    LibraryWidgetPanel(Library library, ItemWidgetPanel itemWidgetPanel) {
        this.library = library;
        this.itemWidgetPanel = itemWidgetPanel;
        selectedItem = library.getSelectedItem();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        itemGrid.add(itemSortWidget);
        add(itemGrid);
    }

    class ItemSortWidget extends JPanel {
        ItemSortWidget() {
            add(new JPanel() {
                {
                    setLayout(new GridLayout(1, 4));
                    JLabel nameLabel = new JLabel("Name");
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

    void refresh() {
        System.out.println("Refreshing library: " + library.getName());
        updateLibrary(library);
    }

    Library getLibrary() {
        return library;
    }

    Item getSelectedItem() {
        return library.getSelectedItem();
    }

    class LibraryItem extends JPanel {
        Item item = null;

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
            JLabel dateLabel = new JLabel(item.getDateAdded() == null ? "" : item.getDateAdded().toString());
            add(dateLabel);
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
            
            inputField.requestFocusInWindow();
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

class FileTreeWidget extends SnapFromPanel {
    private JScrollPane scrollPane;
    static FileTreeWidgetPanel fileTreeWidgetPanel;

    FileTreeWidget(FileTree fileTree) {
        super(fileTreeWidgetPanel = new FileTreeWidgetPanel(fileTree), 3, "File Tree");
        scrollPane = new JScrollPane(fileTreeWidgetPanel);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
}

class FileTreeWidgetPanel extends JPanel {
    private FileTree fileTree;

    FileTreeWidgetPanel(FileTree fileTree) {
        this.fileTree = fileTree;
        setLayout(new BorderLayout());
        add(fileTree, BorderLayout.CENTER);
    }

    void fromLibrary(Library library) {
        fileTree.fromLibrary(library);
    }

    void refresh(Library library) {
        fileTree.fromLibrary(library);
    }
}

class FileTree extends JTree {
    private JPanel panel;
    private JTextField editor;
    private DefaultMutableTreeNode editableNode;

    FileTree() {
        super(new DefaultMutableTreeNode("Papyrus"));
        createTreeNodes();

        editor = new JTextField();
        editor.setVisible(false);
        editor.addActionListener(e -> renameNode());
        add(editor);

        addMouseListener(new MouseAdapter() {
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

    private void createTreeNodes() {
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        // // Example to add a child node. Ppl u guys might want to dynamically add nodes based on files idk what we fina do wit dis.
        // root.add(new DefaultMutableTreeNode("child"));

        model.reload();
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    void setPanel(JPanel panel) {
        this.panel = panel;
    }
}