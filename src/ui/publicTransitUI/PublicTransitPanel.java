package ui.publicTransitUI;

import controller.DataBaseConnectionHandler;
import ui.DisplayTablePanel;
//import requestProcessor.ProcessPublicTransitRequest;

import javax.swing.*;
import java.awt.*;

public class PublicTransitPanel extends JPanel {
    private static final Font TITLE_FONT = new Font("Label", Font.BOLD, 40);
    private static final Font BUTTON_FONT_LARGE = new Font("Button", Font.BOLD, 20);
    private static final Font BUTTON_FONT_SMALL = new Font("Button", Font.BOLD, 15);
    private static final Color TITLE_BG_COLOR = new Color(200, 250, 200);
    private DataBaseConnectionHandler db;
    //ProcessPublicTransitRequest processPublicTransitRequest;
    private DisplayTablePanel dtp;
    private JLabel title;
    private JButton addTransit;
    private JButton deleteTransit;
    private JButton searchTransit;
    private JButton modifyTransit;
    private JButton viewLineName;
    private JButton viewDeparture;
    private JButton viewDestination;
    private JButton findIndResidesProp;
    private JFrame addFrame;
    private JFrame deleteFrame;
    private JFrame searchFrame;
    private JFrame modifyFrame;
    private JFrame lineNameFrame;
    private JFrame departureFrame;
    private JFrame destinationFrame;
    private JFrame findIndFrame;

    public PublicTransitPanel(DataBaseConnectionHandler dbch) {
        db = dbch;
        //processPublicTransitRequest = new ProcessPublicTransitRequest(this, db);
        JFrame propertyFrame = new JFrame();
        propertyFrame.setSize(2000, 1000);
        propertyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ends system when frame closed
        propertyFrame.setTitle("Public Transit");
        propertyFrame.add(this);
        setLayout(null);
        // adding table
        createAndAddTable();
        // adding label
        createAndAddTitle("Public Transit");
        // adding buttons
        createAndAddButtons();


        repaint();
        propertyFrame.setVisible(true);

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

    public void createAndAddButtons() {
        addTransit = new JButton("Add Public Transit");
        addTransit.setFont(BUTTON_FONT_SMALL);
        //addTransit.addActionListener(e -> handleAddPublicTransitCall());
        addTransit.setBounds(50, 200, 190, 75);
        add(addTransit);
        deleteTransit = new JButton("Delete Public Transit");
        deleteTransit.setFont(BUTTON_FONT_SMALL);
        //deleteTransit.addActionListener(e -> handleDeletePublicTransitCall());
        deleteTransit.setBounds(260, 200, 190, 75);
        add(deleteTransit);
        searchTransit = new JButton("Search Public Transit");
        searchTransit.setFont(BUTTON_FONT_SMALL);
        //searchTransit.addActionListener(e -> handleAddPublicTransitCall());
        searchTransit.setBounds(50, 300, 190, 75);
        add(searchTransit);
        modifyTransit = new JButton("Modify Public Transit");
        modifyTransit.setFont(BUTTON_FONT_SMALL);
        //modifyTransit.addActionListener(e -> handleAddPublicTransitCall());
        modifyTransit.setBounds(260, 300, 190, 75);
        add(modifyTransit);
        viewLineName = new JButton("View Line Name");
        viewLineName.setFont(BUTTON_FONT_SMALL);
        //viewLineName.addActionListener(e -> handleAddPublicTransitCall());
        viewLineName.setBounds(50, 400, 400, 75);
        add(viewLineName);
        viewDeparture = new JButton("View Departure");
        viewDeparture.setFont(BUTTON_FONT_SMALL);
        //viewDeparture.addActionListener(e -> handleAddPublicTransitCall());
        viewDeparture.setBounds(260, 400, 190, 75);
        add(viewDestination);
        viewDestination = new JButton("View Destination");
        viewDestination.setFont(BUTTON_FONT_SMALL);
        //viewDestination.addActionListener(e -> handleAddPublicTransitCall());
        viewDestination.setBounds(50, 500, 400, 75);
        add(viewDestination);
        findIndResidesProp = new JButton("Find All Individuals Resides In Selected Property");
        findIndResidesProp.setFont(BUTTON_FONT_SMALL);
        //findIndResidesProp.addActionListener(e -> handleAddPublicTransitCall());
        findIndResidesProp.setBounds(50, 600, 400, 75);
        add(findIndResidesProp);
    }
}
//
//    public void handleAddPublicTransitCall() {
//        closeAllFrames();
//        addFrame = new JFrame();
//        addFrame.setTitle("Add Public Transit");
//        addFrame.setSize(500, 500);
//        String[] attributes = new String[]{"LineName", "Province", "Transit Type", "Departure", "Destination"};
//        addFrame.add(new AddPopUp(attributes, processPublicTransitRequest));
//        addFrame.setVisible(true);
//    }
//
//    public void handleDeletePublicTransitCall() {
//        closeAllFrames();
//        deleteFrame = new JFrame();
//        deleteFrame.setTitle("Delete Public Transit");
//        deleteFrame.setSize(500, 500);
//        deleteFrame.add(new DeletePopUp(processPublicTransitRequest));
//        deleteFrame.setVisible(true);
//    }
//
//    public void closeAllFrames() {
//        JFrame[] frameList = new JFrame[]{addFrame, deleteFrame, searchFrame, modifyFrame, lineNameFrame, departureFrame, destinationFrame, findIndFrame};
//        for (JFrame jf: frameList) {
//            if (jf != null) {
//                jf.dispose();
//            }
//        }
//    }
//
//}
//

