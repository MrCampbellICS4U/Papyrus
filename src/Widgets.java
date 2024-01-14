import java.awt.*;
import javax.swing.*;

public class Widgets {

    Widgets() {
        
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

