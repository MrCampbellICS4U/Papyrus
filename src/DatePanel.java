import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DatePanel extends JPanel {
    // class attributes
    JComboBox<Integer> dayComboBox = new JComboBox<>();
    JComboBox<Integer> monthComboBox = new JComboBox<>();
    JComboBox<Integer> yearComboBox = new JComboBox<>();

    /**
     * Constructor for DatePanel
     */
    DatePanel() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(dayComboBox);
        add(monthComboBox);
        add(yearComboBox);

        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(i);
        }

        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }

        for (int i = 1400; i <= 2024; i++) {
            yearComboBox.addItem(i);
        }
    }

    /**
     * Constructor for DatePanel
     * @param day
     * @param month
     * @param year
     */
    DatePanel(int day, int month, int year) {
        this();
        dayComboBox.setSelectedItem(day);
        monthComboBox.setSelectedItem(month);
        yearComboBox.setSelectedItem(year);
    }

    /**
     * Copy constructor for DatePanel
     * @param other
     */
    DatePanel(DatePanel other) {
        this(other == null ? 1 : other.getDay(), other == null ? 1 : other.getMonth(), other == null ? 1400 : other.getYear());
    }

    /**
     * returns the date as a DatePanel
     * @param date
     * @return date as a DatePanel
     */
    public static DatePanel fromString(String date) {
        if (date == null || date.equals("") || date.equals("null")) {
            return null;
        }
        String[] dateArray = date.split("-");
        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int year = Integer.parseInt(dateArray[2]);
        return new DatePanel(day, month, year);
    }

    /**
     * adds a listener to the date panel
     * @param item
     * @param attributeType
     */
    public void addDateChangeListener(Item item, LibraryComparator.Type attributeType) {
        ActionListener listener = e -> {
            System.out.println("Date changed");
            String newDate = toString();
            item.setInfo(attributeType, newDate);
        };

        dayComboBox.addActionListener(listener);
        monthComboBox.addActionListener(listener);
        yearComboBox.addActionListener(listener);
    }

    /**
     * returns the date as a string
     * @return date as a string
     */
    public String toString() {
        return dayComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + yearComboBox.getSelectedItem();
    }

    // getter and setter methods
    public int getDay() {
        return (int) dayComboBox.getSelectedItem();
    }

    public int getMonth() {
        return (int) monthComboBox.getSelectedItem();
    }

    public int getYear() {
        return (int) yearComboBox.getSelectedItem();
    }

    public void setDay(int day) {
        dayComboBox.setSelectedItem(day);
    }

    public void setMonth(int month) {
        monthComboBox.setSelectedItem(month);
    }

    public void setYear(int year) {
        yearComboBox.setSelectedItem(year);
    }

    public void setDay(String day) {
        dayComboBox.setSelectedItem(Integer.parseInt(day));
    }

    public void setMonth(String month) {
        monthComboBox.setSelectedItem(Integer.parseInt(month));
    }

    public void setYear(String year) {
        yearComboBox.setSelectedItem(Integer.parseInt(year));
    }

    public int compareTo(DatePanel other) {
        if (this.getYear() != other.getYear()) {
            return this.getYear() - other.getYear();
        } else if (this.getMonth() != other.getMonth()) {
            return this.getMonth() - other.getMonth();
        } else {
            return this.getDay() - other.getDay();
        }
    }
}
