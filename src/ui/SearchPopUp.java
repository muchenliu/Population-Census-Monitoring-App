package ui;

import requestProcessor.ProcessRequest;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchPopUp extends JPanel {

    private ArrayList<JTextField> cAttributes;
    private ArrayList<JTextField> cOperations;
    private ArrayList<JTextField> cValues;
    private ArrayList<JCheckBox> cSelected;
    private JLabel warningMessage;


    public SearchPopUp(ProcessRequest pr, String[] thisAttributes, ArrayList<Boolean> attributesSelected) {
        super();
        warningMessage = new JLabel();
        setLayout(null);
        cAttributes = new ArrayList<>();
        cOperations = new ArrayList<>();
        cValues = new ArrayList<>();
        cSelected = new ArrayList<>();
        int i;
        JLabel attHeader = new JLabel("Attribute");
        JLabel opHeader = new JLabel("Operation");
        JLabel valHeader = new JLabel("Value");
        attHeader.setBounds(40, 50, 100, 20);
        opHeader.setBounds(150, 50, 60, 20);
        valHeader.setBounds(220, 50, 100, 20);
        add(attHeader);
        add(opHeader);
        add(valHeader);
        for (i = 1; i <= 10; i++) {
            JCheckBox currSel = new JCheckBox();
            currSel.setBounds(10, 30 * i + 50, 20,20);
            add(currSel);
            cSelected.add(currSel);
            JTextField currAtt = new JTextField();
            currAtt.setBounds(40, 30 * i + 50, 100,20);
            add(currAtt);
            cAttributes.add(currAtt);
            JTextField currOp = new JTextField();
            currOp.setBounds(160, 30 * i + 50, 40,20);
            add(currOp);
            cOperations.add(currOp);
            JTextField currVal = new JTextField();
            currVal.setBounds(220, 30 * i + 50, 200,20);
            add(currVal);
            cValues.add(currVal);
        }
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(120, 30 * i + 80, 100, 30);
        searchButton.addActionListener(e -> {
            try {
                pr.processSearch(cSelected, cAttributes, cOperations, cValues, thisAttributes, attributesSelected);
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

