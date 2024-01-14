import javax.swing.*;
import java.awt.*;

public class TopBar extends JPanel {
    TopBar() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(800, 35));

        setPreferredSize(new Dimension(800, 35));
        add(Box.createHorizontalGlue());
        add(new JButton("Button 1"));
        add(new JButton("Button 2"));
        add(new JButton("Button 3"));
        add(Box.createHorizontalGlue());
    }
}
