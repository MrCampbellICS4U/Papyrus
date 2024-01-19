import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;

//graphics part
public class Merging exatends JFrame {
private JComboBox<String>item1, item2;
private JButton mergeButton;

public Merging(){
    //gui componemts
    item1 = new JComboBox<>;
    item1.addItem("Annotation 1");
    item1.addItem("Annotation 2");

    item2 = new JComboBox<>;
    item2.addItem("Annotation a");
    item2.addItem("Annotation b");
    //comboBox2.addItem("Annotation c");

    mergeButton = new JButton("Merge items");
       mergeButton.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
               
                mergeAnnotations();
            }
        });


}
//layout
setLayout(newBorderLayout());
   JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Item 1 Annotation:"), BorderLayout.NORTH);
        leftPanel.add(item1, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Item 2 Annotation:"), BorderLayout.NORTH);
        rightPanel.add(item2, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(mergeButton, BorderLayout.SOUTH);


//frame
     setTitle("Merging GUI");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
private void mergeAnnotations(){
    String selectedAnnotation1 = (String) item1.getSelectedItem():
    String selectedAnnotation2 = (String) item2.getSelectedItem():

     JOptionPane.showMessageDialog(this, "Annotations merged successfully!");
    }
    public static void main(String[]args){
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Merging();
            }
        });
    }


