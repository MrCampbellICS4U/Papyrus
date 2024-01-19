import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;

public class Papyrus {

    public PapyrusMenuBar menuBar = new PapyrusMenuBar();
    public TopBar topBar = new TopBar();
    public PapyrusPanel papyrusPanel = new PapyrusPanel();

    Papyrus() {
        JFrame frame = new JFrame("Papyrus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1200, 600);
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setLocationRelativeTo(null);
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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Papyrus();
            }
        });
    }
}

class PapyrusPanel extends SnappablePanel {

    static Library library = new Library("Library", new LibraryComparator(LibraryComparator.Type.NAME));
    static {
        library.add(new Item("The Great Gatsby", new Date(1925, 4, 10), new Date(2019, 4, 10), "F. Scott", "Fitzgerald", "", "", ""));
        library.add(new Item("The Catcher in the Rye", new Date(1951, 6, 16), new Date(2019, 4, 10), "J. D.", "Salinger", "", "", ""));
        library.add(new Item("The Grapes of Wrath", new Date(1939, 4, 14), new Date(2019, 4, 10), "John", "Steinbeck", "", "", ""));
    }
    static ItemWidget itemWidget = new ItemWidget(null);

    static SnapFromPanel[] snappablePanels = { new Widget2(), new LibraryWidget(library, itemWidget.getWidgetPanel()), itemWidget};

    PapyrusPanel(SnapFromPanel[] snappablePanels) {
        super(snappablePanels);
        updateLibrary(library);
    }

    PapyrusPanel() {
        super(snappablePanels);
        updateLibrary(library);
    }

    void updateLibrary(Library library) {
        ((LibraryWidget) snappablePanels[1]).updateLibrary(library);
    }
}
