import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Papyrus {

    public PapyrusMenuBar menuBar = new PapyrusMenuBar();
    public TopBar topBar = new TopBar();
    public PapyrusPanel papyrusPanel = new PapyrusPanel();

    Papyrus() {
        JFrame frame = new JFrame("Papyrus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(600, 400));
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
    PapyrusPanel() {
        super(new SnapFromPanel[] { new Widget1(), new Widget2(), new Widget3() });
    }
}