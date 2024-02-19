package ui.propertyUI;

import requestProcessor.ProcessPropertyRequest;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class FIRIPPopUp extends JPanel {

    private String[] attributes = new String[]{"PostalCode", "StreetAddress"};
    private String[] indAttributes = new String[]{"Citizenship", "PassportNum", "PersonName", "Age", "AnnualIncome", "PostalCode", "StreetAddress"};
    private ArrayList<JTextField> valueFields;
    private JLabel warningMessage;

    public FIRIPPopUp(ProcessPropertyRequest ppr, ArrayList<Boolean> attributesSelected) {
        super();
        warningMessage = new JLabel();
        setLayout(null);
        valueFields = new ArrayList<>();
        int i;
        for (i = 0; i < attributes.length; i++) {
            String att = attributes[i];
            JLabel currLabel = new JLabel(att);
            currLabel.setBounds(10, 30 * i + 50, 100,20);
            add(currLabel);
            JTextField currTF = new JTextField();
            currTF.setBounds(120, 30 * i + 50, 200,20);
            add(currTF);
            valueFields.add(currTF);
        }
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(120, 30 * i + 80, 100, 30);
        searchButton.addActionListener(e -> {
            try {
                ppr.processFIRIP(valueFields, indAttributes, attributesSelected);
            } catch (NumberFormatException nfe) {
                warningMessage.setText("NumberFormatException: " + nfe.getMessage());
                warningMessage.setBounds(10, 10, 450, 20);
                add(warningMessage);
                repaint();
            } catch (SQLException sqle) {
                warningMessage.setText("SQLException: " + sqle.getMessage());
                warningMessage.setBounds(10, 10, 450, 20);
                add(warningMessage);
                repaint();
            }
        });
        add(searchButton);
    }
}
