package ui;

import javax.swing.*;
import java.awt.*;

public class DisplayTablePanel extends JInternalFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private static final Font STANDARD_FONT = new Font("Standard", Font.BOLD, 15);

    public DisplayTablePanel() {
        super("Table Result", false, false, false, false);
        String[][] data = {};
        String[] columns = new String[]{};
        setInformation(data, columns);
        setVisible(true);
    }

    public void setInformation(String[][] data, String[] columns) {
        if (scrollPane != null) {
            remove(scrollPane);
        }
        table = new JTable(data, columns);
        table.setDefaultEditor(Object.class, null);
        table.setFont(STANDARD_FONT);
        table.setBounds(0, 0, 1400, 700);
        scrollPane = new JScrollPane(table);
        scrollPane.setFont(STANDARD_FONT);
        add(scrollPane);
        repaint();
    }
}
