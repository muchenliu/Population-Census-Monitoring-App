package ui;

import requestProcessor.ProcessRequest;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdatePopUp extends JPanel {

    private static final Font BIG_HEADER_FONT = new Font("Header", Font.BOLD, 15);
    private ArrayList<JTextField> setAttValues;
    private ArrayList<JCheckBox> updateSelected;
    private ArrayList<JTextField> comAttributes;
    private ArrayList<JTextField> comOperations;
    private ArrayList<JTextField> comValues;
    private ArrayList<JCheckBox> comSelected;
    private JLabel warningMessage;

    public UpdatePopUp(String[] attributes, ProcessRequest pr) {
        super();
        warningMessage = new JLabel();
        setLayout(null);
        setAttValues = new ArrayList<>();
        updateSelected = new ArrayList<>();
        comAttributes = new ArrayList<>();
        comOperations = new ArrayList<>();
        comValues = new ArrayList<>();
        comSelected = new ArrayList<>();
        JLabel updateHeader = new JLabel("Update Attributes");
        JLabel conHeader = new JLabel("Conditions");
        JLabel attHeader = new JLabel("Attribute");
        JLabel opHeader = new JLabel("Operation");
        JLabel valHeader = new JLabel("Value");
        updateHeader.setBounds(10, 50, 150, 20);
        conHeader.setBounds(390, 50, 100, 20);
        attHeader.setBounds(390, 90, 100, 20);
        opHeader.setBounds(500, 90, 60, 20);
        valHeader.setBounds(570, 90, 100, 20);
        updateHeader.setFont(BIG_HEADER_FONT);
        conHeader.setFont(BIG_HEADER_FONT);
        add(updateHeader);
        add(conHeader);
        add(attHeader);
        add(opHeader);
        add(valHeader);
        for (int i = 0; i < attributes.length; i++) {
            JCheckBox currSel = new JCheckBox();
            currSel.setBounds(10, 30 * i + 120, 20,20);
            add(currSel);
            updateSelected.add(currSel);
            String att = attributes[i];
            JLabel currLabel = new JLabel(att);
            currLabel.setBounds(40, 30 * i + 120, 100,20);
            add(currLabel);
            JTextField currTF = new JTextField();
            currTF.setBounds(140, 30 * i + 120, 200,20);
            add(currTF);
            setAttValues.add(currTF);
        }
        int i;
        for (i = 1; i <= 10; i++) {
            JCheckBox currSel = new JCheckBox();
            currSel.setBounds(360, 30 * i + 90, 20,20);
            add(currSel);
            comSelected.add(currSel);
            JTextField currAtt = new JTextField();
            currAtt.setBounds(390, 30 * i + 90, 100,20);
            add(currAtt);
            comAttributes.add(currAtt);
            JTextField currOp = new JTextField();
            currOp.setBounds(510, 30 * i + 90, 40,20);
            add(currOp);
            comOperations.add(currOp);
            JTextField currVal = new JTextField();
            currVal.setBounds(570, 30 * i + 90, 200,20);
            add(currVal);
            comValues.add(currVal);
        }
        JButton updateButton = new JButton("Update");
        updateButton.setBounds(120, 30 * i + 120, 100, 30);
        updateButton.addActionListener(e -> {
            try {
                pr.processModify(updateSelected, setAttValues, comSelected, comAttributes, comOperations, comValues);
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
        add(updateButton);
    }
}

