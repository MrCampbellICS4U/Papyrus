import javax.swing.*;
import java.awt.*;

public class PapyrusMenuBar extends JMenuBar{
    PapyrusMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Open"));
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.add(new JMenuItem("Export"));
        fileMenu.add(new JMenuItem("Quit Papyrus"));

        this.add(fileMenu);
    }
    public void toggleVisibility(boolean visible) {
        setVisible(visible);
    }
}
