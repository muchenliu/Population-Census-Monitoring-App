package ui.vehicleUI;

import controller.DataBaseConnectionHandler;
import requestProcessor.ProcessVehicleRequest;
import ui.DisplayTablePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VehiclePanel extends JPanel{
    private static final Font TITLE_FONT = new Font("Label", Font.BOLD, 40);
    private static final Font BUTTON_FONT_LARGE = new Font("Button", Font.BOLD, 20);
    private static final Font BUTTON_FONT_SMALL = new Font("Button", Font.BOLD, 13);
    private static final Color TITLE_BG_COLOR = new Color(200,250,200);
    private static final Color BUTTON_SELECTED = new Color(154, 248, 244);
    private static final Color BUTTON_UNSELECTED = new Color(193, 215, 215);

    private DataBaseConnectionHandler db;
    private ProcessVehicleRequest processVehicleRequest;

    private String[] thisAttributes = new String[]{"VIN", "Color", "Type", "Brand",
            "LicensePlate", "Model", "Citizenship", "PassportNum"};

    private DisplayTablePanel dtp;

    private ArrayList<JButton> attributeButtons = new ArrayList<>();

    private ArrayList<Boolean> attributeSelected = new ArrayList<>();

    private JLabel title;

    private JButton addVehicle;
    private JButton deleteVehicle;
    private JButton searchVehicle;
    private JButton modifyVehicle;


    public VehiclePanel(DataBaseConnectionHandler dbch) {
        db = dbch;
        JFrame individualFrame = new JFrame();
        individualFrame.setSize(2000, 1000);
        individualFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ends system when frame closed
        individualFrame.setTitle("Vehicle");
        individualFrame.add(this);
        setLayout(null);
        // adding table
        createAndAddTable();
        processVehicleRequest = new ProcessVehicleRequest(this, db, dtp);
        // adding label
        createAndAddTitle("Vehicle");
        // adding buttons
        createAndAddFunctionButtons();
        createAndAddAttributeButtons();
        repaint();
        individualFrame.setVisible(true);
    }
    public void createAndAddTable() {
        dtp = new DisplayTablePanel();
        dtp.setBounds(500, 200, 1400, 700);
        add(dtp);
    }

    public void createAndAddTitle(String titleName) {
        title = new JLabel(titleName);
        title.setFont(TITLE_FONT);
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        titlePanel.setBackground(TITLE_BG_COLOR);
        titlePanel.setBounds(50, 50, 400, 100);
        add(titlePanel);
    }

    public void createAndAddAttributeButtons() {
        for (int i = 0; i < thisAttributes.length; i++) {
            attributeSelected.add(false);
            JButton currB = new JButton(thisAttributes[i]);
            currB.setFont(BUTTON_FONT_SMALL);
            currB.setBackground(BUTTON_UNSELECTED);
            int curr = i;
            currB.addActionListener(e -> {
                if (attributeSelected.get(curr)) {
                    currB.setBackground(BUTTON_UNSELECTED);
                    attributeSelected.set(curr, false);
                } else {
                    currB.setBackground(BUTTON_SELECTED);
                    attributeSelected.set(curr, true);
                }
            });
            currB.setBounds(500 + i * 150, 100, 140, 50);
            attributeButtons.add(currB);
            add(currB);
        }
    }

    public void createAndAddFunctionButtons() {
        addVehicle = new JButton("Add Vehicle");
        addVehicle.setFont(BUTTON_FONT_SMALL);
        addVehicle.setBounds(50, 200, 190, 75);
        add(addVehicle);
        deleteVehicle = new JButton("Delete Vehicle");
        deleteVehicle.setFont(BUTTON_FONT_SMALL);
        deleteVehicle.setBounds(260, 200, 190, 75);
        add(deleteVehicle);
        searchVehicle = new JButton("Search Vehicle");
        searchVehicle.setFont(BUTTON_FONT_SMALL);
        searchVehicle.setBounds(50, 300, 190, 75);
        add(searchVehicle);
        modifyVehicle = new JButton("Modify Vehicle");
        modifyVehicle.setFont(BUTTON_FONT_SMALL);
        modifyVehicle.setBounds(260, 300, 190, 75);
        add(modifyVehicle);
    }
}
