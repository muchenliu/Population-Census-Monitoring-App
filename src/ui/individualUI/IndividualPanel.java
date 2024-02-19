package ui.individualUI;

import controller.DataBaseConnectionHandler;
import requestProcessor.ProcessIndividualRequest;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class IndividualPanel extends JPanel {
    private static final Font TITLE_FONT = new Font("Label", Font.BOLD, 40);
    private static final Font BUTTON_FONT_LARGE = new Font("Button", Font.BOLD, 20);
    private static final Font BUTTON_FONT_SMALL = new Font("Button", Font.BOLD, 13);
    private static final Color TITLE_BG_COLOR = new Color(200,250,200);
    private static final Color BUTTON_SELECTED = new Color(154, 248, 244);
    private static final Color BUTTON_UNSELECTED = new Color(193, 215, 215);

    private DataBaseConnectionHandler db;
    private ProcessIndividualRequest processIndividualRequest;

    private String[] thisAttributes = new String[]{"Citizenship", "PassportNum", "PersonName", "Age",
            "AnnualIncome", "PostalCode", "StreetAddress"};
    private String[] thisAndSubsAttributes = new String[]{"Citizenship", "PassportNum", "PersonName", "Age",
            "AnnualIncome", "PostalCode", "StreetAddress", "LeaseTerm", "OwnedStreetAddress", "OwnedPostalCode",
            "DateOfPurchase"};
    private String[] renterAttributes = new String[]{"Citizenship", "PassportNum", "LeaseTerm"};
    private String[] occupantAttributes = new String[]{"Citizenship", "PassportNum"};
    private String[] ownsPropertyAttributes = new String[]{"Citizenship", "PassportNum", "PostalCode", "StreetAddress",
            "DateOfPurchase"};
    private String[] worksAtAttributes = new String[]{"Citizenship", "PassportNum", "PostalCode", "BranchAddress",
            "EmploymentType"};

    private String[] subClass = new String[]{"Owner", "Renter", "Occupant"}; //show as checkbox when addIndividual

    private DisplayTablePanel dtp;

    private ArrayList<JButton> attributeButtons = new ArrayList<>();

    private ArrayList<Boolean> attributeSelected = new ArrayList<>();

    private JLabel title;

    private JButton addIndividual;
    private JButton deleteIndividual;
    private JButton searchIndividual;
    private JButton modifyIndividual;

    private JButton viewRenter;
//    private JButton updateRenter;
//    private JButton deleteRenter;

    private JButton viewOccupant;
//    private JButton deleteOccupant;

    private JButton viewOwnsProperty;

    private JButton viewWorksAt;

    private JButton averageHouseholdIncome;

    private JButton maxValueOwned;

    private JFrame addFrame;
    private JFrame deleteFrame;
    private JFrame searchFrame;
    private JFrame modifyFrame;
    private JFrame renterFrame;
    private JFrame occupantFrame;
    private JFrame ownsPropertyFrame;
    private JFrame worksAtFrame;
//    private JFrame averageHouseholdIncomeFrame;
//    private JFrame maxValuedOwnedFrame;

    private boolean searchButtonSelected = false;
    private boolean viewOwnsPropertySelected = false;

    public IndividualPanel(DataBaseConnectionHandler dbch) {
        db = dbch;
        JFrame individualFrame = new JFrame();
        individualFrame.setSize(2000, 1000);
        individualFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ends system when frame closed
        individualFrame.setTitle("Individual");
        individualFrame.add(this);
        setLayout(null);
        // adding table
        createAndAddTable();
        processIndividualRequest = new ProcessIndividualRequest(this, db, dtp);
        // adding label
        createAndAddTitle("Individual");
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
        addIndividual = new JButton("Add Individual");
        addIndividual.setFont(BUTTON_FONT_SMALL);
        addIndividual.addActionListener(e -> handleAddIndividualCall());
        addIndividual.setBounds(50, 200, 190, 75);
        add(addIndividual);
        deleteIndividual = new JButton("Delete Individual");
        deleteIndividual.setFont(BUTTON_FONT_SMALL);
        deleteIndividual.addActionListener(e -> handleDeleteIndividualCall());
        deleteIndividual.setBounds(260, 200, 190, 75);
        add(deleteIndividual);
        searchIndividual = new JButton("Search Individual");
        searchIndividual.setFont(BUTTON_FONT_SMALL);
        searchIndividual.addActionListener(e -> handleSearchIndividualCall());
        searchIndividual.setBounds(50, 300, 190, 75);
        add(searchIndividual);
        modifyIndividual = new JButton("Modify Individual");
        modifyIndividual.setFont(BUTTON_FONT_SMALL);
        modifyIndividual.addActionListener(e -> handleModifyIndividualCall());
        modifyIndividual.setBounds(260, 300, 190, 75);
        add(modifyIndividual);
        viewRenter = new JButton("View Renter");
        viewRenter.setFont(BUTTON_FONT_SMALL);
        viewRenter.setBounds(50, 400, 190, 75);
        add(viewRenter);
        viewOccupant = new JButton("View Occupants");
        viewOccupant.setFont(BUTTON_FONT_SMALL);
        viewOccupant.setBounds(260, 400, 190, 75);
        add(viewOccupant);
        viewOwnsProperty = new JButton("View Owns Property");
        viewOwnsProperty.setFont(BUTTON_FONT_SMALL);
        viewOwnsProperty.setBounds(50, 500, 190, 75);
        add(viewOwnsProperty);
        viewWorksAt = new JButton("View Works At");
        viewWorksAt.setFont(BUTTON_FONT_SMALL);
        viewWorksAt.setBounds(260, 500, 190, 75);
        add(viewWorksAt);
        averageHouseholdIncome = new JButton("View Average Household Income");
        averageHouseholdIncome.setFont(BUTTON_FONT_SMALL);
        averageHouseholdIncome.addActionListener(e -> handleAverageHouseholdIncomeCall());
        averageHouseholdIncome.setBounds(50, 600, 400, 75);
        add(averageHouseholdIncome);
        maxValueOwned = new JButton("View Max Value Owned");
        maxValueOwned.setFont(BUTTON_FONT_SMALL);
        maxValueOwned.addActionListener(e -> handleMaxOwnedValueCall());
        maxValueOwned.setBounds(50, 700, 400, 75);
        add(maxValueOwned);
    }

    public void handleAddIndividualCall() {
        closeAllFrames();
        addFrame = new JFrame();
        addFrame.setTitle("Add Individual");
        addFrame.setSize(500, 500);
        addFrame.add(new AddPopUp(thisAndSubsAttributes, subClass, processIndividualRequest));
        addFrame.setVisible(true);
    }

    public void handleDeleteIndividualCall() {
        closeAllFrames();
        deleteFrame = new JFrame();
        deleteFrame.setTitle("Delete Individual");
        deleteFrame.setSize(500, 500);
        deleteFrame.add(new DeletePopUp(processIndividualRequest));
        deleteFrame.setVisible(true);
    }

    public void handleSearchIndividualCall() {
        closeAllFrames();
        if (searchButtonSelected) {
            searchFrame = new JFrame();
            searchFrame.setTitle("Search Individual");
            searchFrame.setSize(500, 500);
            searchFrame.add(new SearchPopUp(processIndividualRequest, thisAttributes, attributeSelected));
            searchFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            setAllFunctionButtonSelectToFalse();
            searchButtonSelected = true;
        }
    }

    public void handleModifyIndividualCall() {
        closeAllFrames();
        modifyFrame = new JFrame();
        modifyFrame.setTitle("Modify Individual");
        modifyFrame.setSize(800, 600);
        modifyFrame.add(new UpdatePopUp(thisAttributes, processIndividualRequest));
        modifyFrame.setVisible(true);
    }

    public void handleAverageHouseholdIncomeCall() {
        closeAllFrames();
        processIndividualRequest.processAverageHouseholdIncome();
    }

    public void handleMaxOwnedValueCall() {
        closeAllFrames();
        processIndividualRequest.processMaxOwnedValue();
    }

    public void closeAllFrames() {
        JFrame[] frameList = new JFrame[]{addFrame, deleteFrame, searchFrame, modifyFrame};
        for (JFrame jf: frameList) {
            if (jf != null) {
                jf.dispose();
            }
        }
    }

    public void refreshTable() {
        remove(dtp);
        add(dtp);
        repaint();
    }

    public void setAllFunctionButtonSelectToFalse() {
        searchButtonSelected = false;
//        viewValueButtonSelected = false;
//        viewAmenButtonSelected = false;
//        viewCityButtonSelected = false;
//        viewHouseButtonSelected = false;
//        viewTownHouseButtonSelected = false;
//        viewApartButtonSelected = false;
    }

    public void setAttributeButtonsDefault() {
        while (attributeButtons.size() > thisAttributes.length) {
            remove(attributeButtons.get(attributeButtons.size() - 1));
            attributeButtons.remove(attributeButtons.size() - 1);
            attributeSelected.remove(attributeSelected.size() - 1);
        }
        repaint();
    }



}
