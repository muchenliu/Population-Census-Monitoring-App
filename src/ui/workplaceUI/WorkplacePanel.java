package ui.workplaceUI;

import controller.DataBaseConnectionHandler;
import requestProcessor.ProcessWorkplaceRequest;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WorkplacePanel extends JPanel {
    private static final Font TITLE_FONT = new Font("Label", Font.BOLD, 40);
    private static final Font BUTTON_FONT_LARGE = new Font("Button", Font.BOLD, 20);
    private static final Font BUTTON_FONT_SMALL = new Font("Button", Font.BOLD, 13);
    private static final Color TITLE_BG_COLOR = new Color(200,250,200);
    private static final Color BUTTON_SELECTED = new Color(154, 248, 244);
    private static final Color BUTTON_UNSELECTED = new Color(193, 215, 215);

    private DataBaseConnectionHandler db;
    private ProcessWorkplaceRequest processWorkplaceRequest;

    private String[] thisAttributes = new String[]{"PostalCode", "BranchAddress", "AnnualProfit", "CompanyName",
            "NumEmployee"};

    private DisplayTablePanel dtp;

    private ArrayList<JButton> attributeButtons = new ArrayList<>();

    private ArrayList<Boolean> attributeSelected = new ArrayList<>();

    private JLabel title;

    private JButton addWorkplace;
    private JButton deleteWorkplace;
    private JButton searchWorkplace;
    private JButton modifyWorkplace;
    private JButton minEmpAvgAgeWorkplace;

    private JFrame addFrame;
    private JFrame deleteFrame;
    private JFrame searchFrame;
    private JFrame modifyFrame;

    public WorkplacePanel(DataBaseConnectionHandler dbch) {
        db = dbch;
        JFrame individualFrame = new JFrame();
        individualFrame.setSize(2000, 1000);
        individualFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ends system when frame closed
        individualFrame.setTitle("Workplace");
        individualFrame.add(this);
        setLayout(null);
        // adding table
        createAndAddTable();
        processWorkplaceRequest = new ProcessWorkplaceRequest(this, db, dtp);
        // adding label
        createAndAddTitle("Workplace");
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
        addWorkplace = new JButton("Add Workplace");
        addWorkplace.setFont(BUTTON_FONT_SMALL);
        addWorkplace.addActionListener(e -> handleAddWpCall());
        addWorkplace.setBounds(50, 200, 190, 75);
        add(addWorkplace);
        deleteWorkplace = new JButton("Delete Workplace");
        deleteWorkplace.setFont(BUTTON_FONT_SMALL);
        deleteWorkplace.addActionListener(e -> handleDeleteWpCall());
        deleteWorkplace.setBounds(260, 200, 190, 75);
        add(deleteWorkplace);
        searchWorkplace = new JButton("Search Workplace");
        searchWorkplace.setFont(BUTTON_FONT_SMALL);
        searchWorkplace.addActionListener(e -> handleSearchWpCall());
        searchWorkplace.setBounds(50, 300, 190, 75);
        add(searchWorkplace);
        modifyWorkplace = new JButton("Modify Workplace");
        modifyWorkplace.setFont(BUTTON_FONT_SMALL);
        modifyWorkplace.addActionListener(e -> handleModifyWpCall());
        modifyWorkplace.setBounds(260, 300, 190, 75);
        add(modifyWorkplace);
        minEmpAvgAgeWorkplace = new JButton("View Min Employee Average Age Workplace");
        minEmpAvgAgeWorkplace.setFont(BUTTON_FONT_SMALL);
        minEmpAvgAgeWorkplace.addActionListener(e -> handleMinEmpAvgAgeWorkplaceCall());
        minEmpAvgAgeWorkplace.setBounds(50, 400, 400, 75);
        add(minEmpAvgAgeWorkplace);
    }

    public void handleAddWpCall() {
        closeAllFrames();
        addFrame = new JFrame();
        addFrame.setTitle("Add Workplace");
        addFrame.setSize(500, 500);
        addFrame.add(new AddPopUp(thisAttributes, new String[0], processWorkplaceRequest));
        addFrame.setVisible(true);
    }

    public void handleDeleteWpCall() {
        closeAllFrames();
        deleteFrame = new JFrame();
        deleteFrame.setTitle("Delete Workplace");
        deleteFrame.setSize(500, 500);
        deleteFrame.add(new DeletePopUp(processWorkplaceRequest));
        deleteFrame.setVisible(true);
    }

    public void handleSearchWpCall() {
        closeAllFrames();
        if (searchButtonSelected) {
            searchFrame = new JFrame();
            searchFrame.setTitle("Search Workplace");
            searchFrame.setSize(500, 500);
            searchFrame.add(new SearchPopUp(processWorkplaceRequest, thisAttributes, attributeSelected));
            searchFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            setAllFunctionButtonSelectToFalse();
            searchButtonSelected = true;
        }
    }

    public void handleModifyWpCall() {
        closeAllFrames();
        modifyFrame = new JFrame();
        modifyFrame.setTitle("Modify Workplace");
        modifyFrame.setSize(800, 600);
        modifyFrame.add(new UpdatePopUp(thisAttributes, processWorkplaceRequest));
        modifyFrame.setVisible(true);
    }

    public void handleMinEmpAvgAgeWorkplaceCall() {
        closeAllFrames();
        processWorkplaceRequest.processMinEmpAvgAgeWorkplace();
    }
    private boolean searchButtonSelected = false;

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
