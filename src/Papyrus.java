import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

public class Papyrus {

    public TopBar topBar = new TopBar(PapyrusPanel.libraryWidget.getWidgetPanel());
    public PapyrusPanel papyrusPanel = new PapyrusPanel();

    Papyrus() {
        JFrame frame = new JFrame("Papyrus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1200, 600);
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setLocationRelativeTo(null);



        ImageIcon icon = new ImageIcon("src/papyrus.png");
        frame.setIconImage(icon.getImage());

    class PapyrusMenuBar extends JMenuBar {
        PapyrusMenuBar() {
            JMenu fileMenu = new JMenu("File");
            JMenuItem newItem = new JMenuItem("New");
            JMenuItem openItem = new JMenuItem("Open", KeyEvent.VK_O | KeyEvent.CTRL_DOWN_MASK);
            openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
            JMenuItem saveItem = new JMenuItem("Save", KeyEvent.VK_S | KeyEvent.CTRL_DOWN_MASK);
            saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
            JMenuItem saveAsItem = new JMenuItem("Save As", KeyEvent.VK_S | KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
            saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
            JMenu exportAsItem = new JMenu("Export");
            JMenuItem exportAsBibtex = new JMenuItem("Export as BibTeX");
            JMenuItem quitItem = new JMenuItem("Quit Papyrus", KeyEvent.VK_Q | KeyEvent.CTRL_DOWN_MASK);
            quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

            saveItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveLibrary();
                }
            });

            saveAsItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveLibraryAs();
                }
            });

            openItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openFile();
                }
            });

            exportAsBibtex.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exporting as BibTeX...");
                    Library currentLibrary = PapyrusPanel.libraryWidget.getLibrary();
                    System.out.println("Exporting as BibTeX: " + currentLibrary);
                    if (currentLibrary != null) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("BibTeX Files (.bib)", "bib"));

                        int result = fileChooser.showSaveDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                            LibraryBibtexParser.writeLibrary(currentLibrary, selectedFile.getAbsolutePath());
                            JOptionPane.showMessageDialog(null, "Library exported successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No library to export.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            quitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(null, "Would you like to save before exiting?", "Save before exiting?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        saveLibrary();
                        System.exit(0);
                    } else if (result == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    } else {
                        // do nothing
                    }
                }
            });

            fileMenu.add(newItem);
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(saveAsItem);
            exportAsItem.add(exportAsBibtex);
            fileMenu.add(exportAsItem);
            fileMenu.add(quitItem);

            this.add(fileMenu);
        }

        private void openFile() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Papyrus XML Files (.ppxml)", "ppxml"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                Library library = LibraryXMLParser.parseLibrary(selectedFile.getAbsolutePath(), new LibraryComparator(LibraryComparator.Type.NAME));
                System.out.println("Library loaded: " + library);
                PapyrusPanel.fileTree.fromLibrary(library);
            }
        }

        private void saveLibrary() {
            System.out.println("Saving library...");
            Library currentLibrary = PapyrusPanel.libraryWidget.getLibrary();
            System.out.println("Saving library: " + currentLibrary);
            if (currentLibrary != null) {
                LibraryXMLParser.writeLibrary(currentLibrary);
                JOptionPane.showMessageDialog(null, "Library saved successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No library to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveLibraryAs() {
            System.out.println("Saving library as...");
            Library currentLibrary = PapyrusPanel.libraryWidget.getLibrary();
            System.out.println("Saving library as: " + currentLibrary);
            if (currentLibrary != null) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Papyrus XML Files (.ppxml)", "ppxml"));

                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    currentLibrary.setPath(selectedFile.getAbsolutePath());
                    LibraryXMLParser.writeLibrary(currentLibrary);
                    JOptionPane.showMessageDialog(null, "Library saved successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No library to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }

        PapyrusMenuBar menuBar = new PapyrusMenuBar();
        frame.setJMenuBar(menuBar);

        // So lads, basically when we move a widget, we lose focus on the frame, so we can't use keyboard shortcuts hehe
        // we can fix this by adding a key listener to the frame, and then when we move a widget, we can set focus back to the frame
        // I dont want to do this because it's a bit hacky, but if we can't find a better solution, we can do this
        
        // frame.setFocusable(true);
        // frame.requestFocusInWindow();

        // frame.addKeyListener(new KeyAdapter() {
        //     public void keyPressed(KeyEvent e) {
        //         if (e.getKeyCode() == KeyEvent.VK_M) {
        //             menuBar.toggleVisibility(!menuBar.isVisible());
        //             if (menuBar.isVisible())
        //                 frame.setSize(frame.getWidth(), frame.getHeight() + menuBar.getHeight());
        //             else
        //                 frame.setSize(frame.getWidth(), frame.getHeight() - menuBar.getHeight());
        //         }
        //     }
        // });        

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                papyrusPanel.adjustPanelSizes();
                papyrusPanel.adjustSnapFromPanelSizes();
            }
        });



        frame.add(topBar, BorderLayout.NORTH);
        frame.add(papyrusPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Papyrus();
            }
        });
    }
}

class PapyrusPanel extends SnappablePanel {

    static Library library = new Library("Library", new LibraryComparator(LibraryComparator.Type.NAME));

    static ItemWidget itemWidget = new ItemWidget(null);

    static FileTree fileTree = new FileTree(); 
    static FileTreeWidgetPanel fileTreeWidgetPanel = new FileTreeWidgetPanel(fileTree);
    static {
        fileTree.setPanel(fileTreeWidgetPanel);
    }
    static FileTreeWidget fileTreeWidget = new FileTreeWidget(fileTree);
    static LibraryWidget libraryWidget = new LibraryWidget(library, itemWidget.getWidgetPanel());



    static SnapFromPanel[] snappablePanels = {
        fileTreeWidget, 
        libraryWidget,
        itemWidget
    };

    PapyrusPanel(SnapFromPanel[] snappablePanels) {
        super(snappablePanels);
        updateLibrary(library);
    }

    PapyrusPanel() {
        super(snappablePanels);
        updateLibrary(library);
    }

    public Library getLibrary() {
        return library;
    }

    void updateLibrary(Library library) {
        libraryWidget.updateLibrary(library);
    }
}
