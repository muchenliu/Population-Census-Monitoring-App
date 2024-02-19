package ui.propertyUI;

import requestProcessor.ProcessPropertyRequest;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class FPOBIPopUp extends JPanel {

    private ArrayList<JTextField> citizenships;
    private ArrayList<JTextField> passportNums;
    private ArrayList<JCheckBox> selected;
    private JLabel warningMessage;

    private String[] attributes = new String[]{"PostalCode", "StreetAddress", "PropSize", "NumBedroom", "NumBathroom"};

    public FPOBIPopUp(ProcessPropertyRequest ppr, ArrayList<Boolean> attributesSelected) {
        super();
        warningMessage = new JLabel();
        setLayout(null);
        citizenships = new ArrayList<>();
        passportNums = new ArrayList<>();
        selected = new ArrayList<>();
        int i;
        JLabel citiHeader = new JLabel("Citizenship");
        JLabel ppNumHeader = new JLabel("PassportNum");
        citiHeader.setBounds(40, 50, 100, 20);
        ppNumHeader.setBounds(180, 50, 100, 20);
        add(citiHeader);
        add(ppNumHeader);
        for (i = 1; i <= 10; i++) {
            JCheckBox currSel = new JCheckBox();
            currSel.setBounds(10, 30 * i + 50, 20,20);
            add(currSel);
            selected.add(currSel);
            JTextField currCiti = new JTextField();
            currCiti.setBounds(40, 30 * i + 50, 100,20);
            add(currCiti);
            citizenships.add(currCiti);
            JTextField currPPNum = new JTextField();
            currPPNum.setBounds(180, 30 * i + 50, 200,20);
            add(currPPNum);
            passportNums.add(currPPNum);
        }
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(120, 30 * i + 80, 100, 30);
        searchButton.addActionListener(e -> {
            try {
                ppr.processFPOBI(selected, citizenships, passportNums, attributesSelected, attributes);
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

