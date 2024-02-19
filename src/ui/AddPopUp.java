package ui;

import requestProcessor.ProcessRequest;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddPopUp extends JPanel {

    private ArrayList<JTextField> valueFields;
    private ArrayList<JCheckBox> subClassChoice;
    private JLabel warningMessage;

    public AddPopUp(String[] attributes, ProcessRequest pr) {
        super();
        constructorHelper(attributes, pr);
    }

    public AddPopUp(String[] attributes, String[] subClass, ProcessRequest pr) {
        super();
        constructorHelper(attributes, pr);
        subClassChoice = new ArrayList<>();
        for (int i = 0; i < subClass.length; i++) {
            String sc = subClass[i];
            JCheckBox currCB = new JCheckBox();
            currCB.setBounds(350, 30 * i + 50, 20,20);
            add(currCB);
            subClassChoice.add(currCB);
            JLabel currLabel = new JLabel(sc);
            currLabel.setBounds(380, 30 * i + 50, 100,20);
            add(currLabel);
        }
    }

    private void constructorHelper(String[] attributes, ProcessRequest pr) {
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
        JButton addButton = new JButton("Add");
        addButton.setBounds(120, 30 * i + 80, 100, 30);
        addButton.addActionListener(e -> {
            try {
                pr.processAdd(valueFields, subClassChoice);
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
        add(addButton);
    }
}
