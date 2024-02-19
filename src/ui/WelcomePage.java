package ui;

import controller.DataBaseConnectionHandler;
import ui.individualUI.IndividualPanel;
import ui.propertyUI.PropertyPanel;
import ui.workplaceUI.WorkplacePanel;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JPanel {
    private static final Font TITLE_FONT = new Font("Label", Font.BOLD, 40);
    private static final Font DESCRIPTION_FONT = new Font("Label", Font.BOLD, 30);
    private static final Font BUTTON_FONT_LARGE = new Font("Button", Font.BOLD, 20);
    private static final Font BUTTON_FONT_SMALL = new Font("Button", Font.BOLD, 13);
    private static final Color TITLE_BG_COLOR = new Color(200,250,200);
    private static final Color BUTTON_UNSELECTED = new Color(193, 215, 215);

    DataBaseConnectionHandler db;

    private JLabel title;
    private JLabel descriptionHeader;
    private JLabel description;

    private JButton demographicSearch;
    private JButton property;
    private JButton province;
    private JButton individual;
    private JButton workplace;
    private JButton park;
    private JButton publicTransit;
    private JButton vehicle;



    public WelcomePage(DataBaseConnectionHandler dbch) {
        db = dbch;
        JFrame individualFrame = new JFrame();
        individualFrame.setSize(2000, 1000);
        individualFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ends system when frame closed
        individualFrame.setTitle("Welcome");
        individualFrame.add(this);
        setLayout(null);
        createAndAddTitle("Canadian Demographic Database");
        createAndAddDescriptionHeader("Description");
        createAndAddDescription("<html>This data base contains information regarding the population and living conditions across Canada. <br> Use the buttons provided to access information regarding a specific characteristic of the population/area.</html>");
        // adding buttons
        createAndAddButtons();
        repaint();
        individualFrame.setVisible(true);
    }

    private void createAndAddDescription(String d) {
        description = new JLabel(d);
        description.setFont(DESCRIPTION_FONT);
        description.setBounds(800, 250, 600,500);
        add(description);
    }

    private void createAndAddDescriptionHeader(String dh) {
        descriptionHeader = new JLabel(dh);
        descriptionHeader.setFont(TITLE_FONT);
        JPanel descriptionHeaderPanel = new JPanel();
        descriptionHeaderPanel.add(descriptionHeader);
        descriptionHeaderPanel.setBackground(TITLE_BG_COLOR);
        descriptionHeaderPanel.setBounds(800, 200, 500,100);
        add(descriptionHeaderPanel);
    }

    //TODO add description textboxes

    public void createAndAddTitle(String titleName) {
        title = new JLabel(titleName);
        title.setFont(TITLE_FONT);
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        titlePanel.setBackground(TITLE_BG_COLOR);
        titlePanel.setBounds(50, 50, 800, 100);
        add(titlePanel);
    }

    public void createAndAddButtons() {
        demographicSearch = new JButton("DEMOGRAPHIC SEARCH");
        demographicSearch.setFont(BUTTON_FONT_SMALL);
        demographicSearch.setBounds(50, 200, 400, 75);
        add(demographicSearch);
        property = new JButton("Property");
        property.setFont(BUTTON_FONT_SMALL);
        property.addActionListener(e -> handleMainPagePropertyCall(db));
        property.setBounds(50, 300, 190, 75);
        add(property);
        province = new JButton("Province");
        province.setFont(BUTTON_FONT_SMALL);
        province.setBounds(260, 300, 190, 75);
        add(province);
        individual = new JButton("Individual");
        individual.setFont(BUTTON_FONT_SMALL);
        individual.addActionListener(e -> handleMainPageIndividualCall(db));
        individual.setBounds(50, 400, 190, 75);
        add(individual);
        workplace = new JButton("Workplace");
        workplace.setFont(BUTTON_FONT_SMALL);
        workplace.addActionListener(e -> handleMainPageWorkplaceCall(db));
        workplace.setBounds(260, 400, 190, 75);
        add(workplace);
        park = new JButton("Park");
        park.setFont(BUTTON_FONT_SMALL);
        park.setBounds(50, 500, 190, 75);
        add(park);
        publicTransit = new JButton("Public Transit");
        publicTransit.setFont(BUTTON_FONT_SMALL);
        publicTransit.setBounds(260, 500, 190, 75);
        add(publicTransit);
        vehicle = new JButton("Vehicle");
        vehicle.setFont(BUTTON_FONT_SMALL);
        vehicle.setBounds(50, 600, 400, 75);
        add(vehicle);
    }

    private void handleMainPageIndividualCall(DataBaseConnectionHandler db) {
        new IndividualPanel(db);

    }

    private void handleMainPagePropertyCall(DataBaseConnectionHandler db) {
        new PropertyPanel(db);
    }

    private void handleMainPageWorkplaceCall(DataBaseConnectionHandler db) {
        new WorkplacePanel(db);
    }
}
