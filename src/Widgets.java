import java.awt.*;
import javax.swing.*;

public class Widgets {

    Widgets() {
        
    }
}

class LibraryWidget extends SnapFromPanel {
    static LibraryWidgetPanel libraryWidgetPanel;

    LibraryWidget(Library library) {
        super(libraryWidgetPanel = new LibraryWidgetPanel(library), 3, "Library");        

    }
} 

class LibraryWidgetPanel extends JPanel {
    private JScrollPane scrollPane = new JScrollPane(this);

    LibraryWidgetPanel(Library library) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        scrollPane.setViewportView(this);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    void updateLibrary(Library library) {
        removeAll();
        for (Item item : library) {
            add(new LibraryItem(item));
        }
        revalidate();
        repaint();
    }

    class LibraryItem extends JPanel {
        LibraryItem(Item item) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.WHITE);
            add(new JLabel(item.getName()));
            add(Box.createHorizontalGlue());
            add(new JLabel(item.getDatePublished().toString()));
            add(Box.createHorizontalGlue());
            add(new JLabel(item.getDateAdded().toString()));
            add(Box.createHorizontalGlue());
            // add(new JLabel(item.getTags().toString()));
            add(new JLabel("ABLAHOUBLAH"));
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

