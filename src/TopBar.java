import javax.swing.*;
import java.awt.*;


// TopBar class that is at the top of the UI and contains the Add Item button
// TODO: Add a button to merge items
public class TopBar extends JPanel {
    private LibraryWidgetPanel libraryWidgetPanel; // Add a reference to LibraryWidgetPanel

    // Constructor that accepts a LibraryWidgetPanel object
    TopBar(LibraryWidgetPanel libraryWidgetPanel) {
        this.libraryWidgetPanel = libraryWidgetPanel;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(800, 35));

        setPreferredSize(new Dimension(800, 35));
        add(Box.createHorizontalGlue());
        JButton addButton = new JButton("Add Item");
        add(addButton);
        addButton.addActionListener(e -> {
            LibraryWidgetPanel.ItemGrid.addBlankItem(libraryWidgetPanel);
        });

        add(Box.createHorizontalGlue());
        // add(new JButton("Button 2"));
        // add(new JButton("Button 3"));
        add(Box.createHorizontalGlue());
    }
}
