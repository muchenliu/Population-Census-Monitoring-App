package ui.propertyUI;

import controller.DataBaseConnectionHandler;
import requestProcessor.ProcessPropertyRequest;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PropertyPanel extends JPanel {

    private static final Font TITLE_FONT = new Font("Label", Font.BOLD, 40);
    private static final Font BUTTON_FONT_LARGE = new Font("Button", Font.BOLD, 20);
    private static final Font BUTTON_FONT_SMALL = new Font("Button", Font.BOLD, 13);
    private static final Color TITLE_BG_COLOR = new Color(200,250,200);
    private static final Color BUTTON_SELECTED = new Color(154, 248, 244);
    private static final Color BUTTON_UNSELECTED = new Color(193, 215, 215);
    private DataBaseConnectionHandler db;
    private ProcessPropertyRequest processPropertyRequest;
    private String[] thisAttributes = new String[]{"PostalCode", "StreetAddress", "PropSize", "NumBedroom", "NumBathroom"};
    private String[] subClassAttributes = new String[]{"Unit", "Floor"};
    private String[] thisAndSubAttributes = new String[]{"PostalCode", "StreetAddress", "PropSize", "NumBedroom", "NumBathroom", "Unit", "Floor"};
    private String[] subClass = new String[]{"House", "TownHouse", "Apartment"};
    private DisplayTablePanel mainTablePanel;
    private DisplayTablePanel subTablePanel;
    private ArrayList<JButton> attributeButtons = new ArrayList<>();
    private ArrayList<Boolean> attributeSelected = new ArrayList<>();
    private JLabel title;
    private JButton addProp;
    private JButton deleteProp;
    private JButton searchProp;
    private JButton modifyProp;
    private JButton viewPropValue;
    private JButton viewAmen;
    private JButton viewCity;
    private JButton findIndResidesProp;
    private JButton viewHouse;
    private JButton viewTownHouse;
    private JButton viewApartment;
    private JButton findPropOwnByInd;
    private JFrame addFrame;
    private JFrame deleteFrame;
    private JFrame searchFrame;
    private JFrame modifyFrame;
    private JFrame propValueFrame;
    private JFrame amenFrame;
    private JFrame cityFrame;
    private JFrame findIndFrame;
    private JFrame houseFrame;
    private JFrame townHouseFrame;
    private JFrame apartmentFrame;
    private JFrame findPOBIFrame;
    private boolean searchButtonSelected = false;
    private boolean viewValueButtonSelected = false;
    private boolean viewAmenButtonSelected = false;
    private boolean viewCityButtonSelected = false;
    private boolean viewHouseButtonSelected = false;
    private boolean viewTownHouseButtonSelected = false;
    private boolean viewApartButtonSelected = false;
    private boolean viewFIRIPSelected = false;
    private boolean viewPOBISelected = false;

    public PropertyPanel(DataBaseConnectionHandler dbch) {
        db = dbch;
        JFrame propertyFrame = new JFrame();
        propertyFrame.setSize(2000, 1000);
        propertyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ends system when frame closed
        propertyFrame.setTitle("Property");
        propertyFrame.add(this);
        setLayout(null);
        // adding table
        createAndAddTable();
        processPropertyRequest = new ProcessPropertyRequest(this, db, mainTablePanel, subTablePanel);
        // adding label
        createAndAddTitle("Property");
        // adding buttons
        createAndAddFunctionButtons();
        createAndAddAttributeButtons(thisAttributes);



        repaint();
        propertyFrame.setVisible(true);

    }

    public void createAndAddTable() {
        mainTablePanel = new DisplayTablePanel();
        mainTablePanel.setBounds(500, 200, 1000, 700);
        add(mainTablePanel);
        subTablePanel = new DisplayTablePanel();
        subTablePanel.setBounds(500, 200, 1000, 150);
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

    public void createAndAddAttributeButtons(String[] att) {
        for (int i = 0; i < att.length; i++) {
            attributeSelected.add(false);
            JButton currB = new JButton(att[i]);
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
        addProp = new JButton("Add Property");
        addProp.setFont(BUTTON_FONT_LARGE);
        addProp.addActionListener(e -> handleAddPropertyCall());
        addProp.setBounds(50, 200, 190, 75);
        add(addProp);
        deleteProp = new JButton("Delete Property");
        deleteProp.setFont(BUTTON_FONT_LARGE);
        deleteProp.addActionListener(e -> handleDeletePropertyCall());
        deleteProp.setBounds(260, 200, 190, 75);
        add(deleteProp);
        searchProp = new JButton("Search Property");
        searchProp.setFont(BUTTON_FONT_LARGE);
        searchProp.addActionListener(e -> handleSearchPropertyCall());
        searchProp.setBounds(50, 300, 190, 75);
        add(searchProp);
        modifyProp = new JButton("Modify Property");
        modifyProp.setFont(BUTTON_FONT_LARGE);
        modifyProp.addActionListener(e -> handleModifyPropertyCall());
        modifyProp.setBounds(260, 300, 190, 75);
        add(modifyProp);
        viewPropValue = new JButton("View Property Value");
        viewPropValue.setFont(BUTTON_FONT_SMALL);
        viewPropValue.addActionListener(e -> handleViewPropValueCall());
        viewPropValue.setBounds(50, 400, 190, 75);
        add(viewPropValue);
        viewAmen = new JButton("View Amenities");
        viewAmen.setFont(BUTTON_FONT_LARGE);
        viewAmen.addActionListener(e -> handleViewAmenCall());
        viewAmen.setBounds(260, 400, 190, 75);
        add(viewAmen);
        viewCity = new JButton("View City");
        viewCity.setFont(BUTTON_FONT_LARGE);
        viewCity.addActionListener(e -> handleViewCityCall());
        viewCity.setBounds(50, 500, 190, 75);
        add(viewCity);
        viewHouse = new JButton("View House");
        viewHouse.setFont(BUTTON_FONT_LARGE);
        viewHouse.addActionListener(e -> handleViewHouseCall());
        viewHouse.setBounds(260, 500, 190, 75);
        add(viewHouse);
        viewTownHouse = new JButton("View Townhouse");
        viewTownHouse.setFont(BUTTON_FONT_SMALL);
        viewTownHouse.addActionListener(e -> handleViewTownhouseCall());
        viewTownHouse.setBounds(50, 600, 190, 75);
        add(viewTownHouse);
        viewApartment = new JButton("View Apartment");
        viewApartment.setFont(BUTTON_FONT_LARGE);
        viewApartment.addActionListener(e -> handleViewApartmentCall());
        viewApartment.setBounds(260, 600, 190, 75);
        add(viewApartment);
        findIndResidesProp = new JButton("Find All Individuals Resides In Selected Property");
        findIndResidesProp.setFont(BUTTON_FONT_SMALL);
        findIndResidesProp.addActionListener(e -> handleFIRIPCall());
        findIndResidesProp.setBounds(50, 700, 400, 75);
        add(findIndResidesProp);
        findPropOwnByInd = new JButton("Find All Properties Own By Given Individuals");
        findPropOwnByInd.setFont(BUTTON_FONT_SMALL);
        findPropOwnByInd.addActionListener(e -> handleFPOBICall());
        findPropOwnByInd.setBounds(50, 800, 400, 75);
        add(findPropOwnByInd);
    }

    public void handleAddPropertyCall() {
        closeAllFrames();
        addFrame = new JFrame();
        addFrame.setTitle("Add Property");
        addFrame.setSize(500, 500);
        addFrame.add(new AddPopUp(thisAndSubAttributes, subClass, processPropertyRequest));
        addFrame.setVisible(true);
    }

    public void handleDeletePropertyCall() {
        closeAllFrames();
        deleteFrame = new JFrame();
        deleteFrame.setTitle("Delete Property");
        deleteFrame.setSize(500, 500);
        deleteFrame.add(new DeletePopUp(processPropertyRequest));
        deleteFrame.setVisible(true);
    }

    public void handleSearchPropertyCall() {
        closeAllFrames();
        if (searchButtonSelected) {
            searchFrame = new JFrame();
            searchFrame.setTitle("Search Property");
            searchFrame.setSize(500, 500);
            searchFrame.add(new SearchPopUp(processPropertyRequest, thisAttributes, attributeSelected));
            searchFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            setAllFunctionButtonSelectToFalse();
            searchButtonSelected = true;
        }
    }

    public void handleModifyPropertyCall() {
        closeAllFrames();
        modifyFrame = new JFrame();
        modifyFrame.setTitle("Modify Property");
        modifyFrame.setSize(500, 600);
        modifyFrame.add(new UpdatePopUp(thisAndSubAttributes, processPropertyRequest));
        modifyFrame.setVisible(true);
    }

    public void handleViewPropValueCall() {
        closeAllFrames();
        if (viewValueButtonSelected) {
            propValueFrame = new JFrame();
            propValueFrame.setTitle("View Property Value");
            propValueFrame.setSize(500, 500);
            propValueFrame.add(new ViewPropValuePopUp(processPropertyRequest, attributeSelected));
            propValueFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            addAttributeButtonsAfterDefault(new String[]{"SalvageValue", "PropertyAge", "LastRenovation"});
            setAllFunctionButtonSelectToFalse();
            viewValueButtonSelected = true;
        }
    }

    public void handleViewAmenCall() {
        closeAllFrames();
        if (viewAmenButtonSelected) {
            amenFrame = new JFrame();
            amenFrame.setTitle("View Property Amenities");
            amenFrame.setSize(500, 500);
            amenFrame.add(new ViewPropAmenPopUp(processPropertyRequest, attributeSelected));
            amenFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            addAttributeButtonsAfterDefault(new String[]{"Type", "OpeningHours"});
            setAllFunctionButtonSelectToFalse();
            viewAmenButtonSelected = true;
        }
    }

    public void handleViewCityCall() {
        closeAllFrames();
        if (viewCityButtonSelected) {
            cityFrame = new JFrame();
            cityFrame.setTitle("View Property City");
            cityFrame.setSize(500, 500);
            cityFrame.add(new ViewPropCityPopUp(processPropertyRequest, attributeSelected));
            cityFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            addAttributeButtonsAfterDefault(new String[]{"CityName", "ProvinceName"});
            setAllFunctionButtonSelectToFalse();
            viewCityButtonSelected = true;
        }
    }

    public void handleViewHouseCall() {
        closeAllFrames();
        if (viewHouseButtonSelected) {
            houseFrame = new JFrame();
            houseFrame.setTitle("View Houses");
            houseFrame.setSize(500, 500);
            houseFrame.add(new ViewHousePopUp(processPropertyRequest, attributeSelected));
            houseFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            setAllFunctionButtonSelectToFalse();
            viewHouseButtonSelected = true;
        }
    }

    public void handleViewTownhouseCall() {
        closeAllFrames();
        if (viewTownHouseButtonSelected) {
            townHouseFrame = new JFrame();
            townHouseFrame.setTitle("View Townhouses");
            townHouseFrame.setSize(500, 500);
            townHouseFrame.add(new ViewTownhousePopUp(processPropertyRequest, attributeSelected));
            townHouseFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            addAttributeButtonsAfterDefault(new String[]{"Unit"});
            setAllFunctionButtonSelectToFalse();
            viewTownHouseButtonSelected = true;
        }
    }

    public void handleViewApartmentCall() {
        closeAllFrames();
        if (viewApartButtonSelected) {
            apartmentFrame = new JFrame();
            apartmentFrame.setTitle("View Apartments");
            apartmentFrame.setSize(500, 500);
            apartmentFrame.add(new ViewApartmentPopUp(processPropertyRequest, attributeSelected));
            apartmentFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            addAttributeButtonsAfterDefault(new String[]{"Unit", "Floor"});
            setAllFunctionButtonSelectToFalse();
            viewApartButtonSelected = true;
        }
    }

    public void handleFIRIPCall() {
        closeAllFrames();
        if (viewFIRIPSelected) {
            findIndFrame = new JFrame();
            findIndFrame.setTitle("Find all individuals reside in given property");
            findIndFrame.setSize(500, 500);
            findIndFrame.add(new FIRIPPopUp(processPropertyRequest, attributeSelected));
            findIndFrame.setVisible(true);
        } else {
            setAttributeButtonsIndividual();
            setAllFunctionButtonSelectToFalse();
            viewFIRIPSelected = true;
        }
    }

    public void handleFPOBICall() {
        closeAllFrames();
        if (viewPOBISelected) {
            findPOBIFrame = new JFrame();
            findPOBIFrame.setTitle("Find all Properties owned by given individuals");
            findPOBIFrame.setSize(500, 500);
            findPOBIFrame.add(new FPOBIPopUp(processPropertyRequest, attributeSelected));
            findPOBIFrame.setVisible(true);
        } else {
            setAttributeButtonsDefault();
            setAllFunctionButtonSelectToFalse();
            viewPOBISelected = true;
        }
    }

    public void closeAllFrames() {
        JFrame[] frameList = new JFrame[]{addFrame, deleteFrame, searchFrame, modifyFrame, propValueFrame, amenFrame, cityFrame, findIndFrame, houseFrame, townHouseFrame, apartmentFrame, findPOBIFrame};
        for (JFrame jf: frameList) {
            if (jf != null) {
                jf.dispose();
            }
        }
    }

    public void setAttributeButtonsDefault() {
        if (viewFIRIPSelected) {
            while (attributeButtons.size() > 0) {
                remove(attributeButtons.get(attributeButtons.size() - 1));
                attributeButtons.remove(attributeButtons.size() - 1);
                attributeSelected.remove(attributeSelected.size() - 1);
            }
            createAndAddAttributeButtons(thisAttributes);
        } else {
            while (attributeButtons.size() > thisAttributes.length) {
                remove(attributeButtons.get(attributeButtons.size() - 1));
                attributeButtons.remove(attributeButtons.size() - 1);
                attributeSelected.remove(attributeSelected.size() - 1);
            }
        }
        repaint();
    }

    public void setAttributeButtonsIndividual() {
        while (attributeButtons.size() > 0) {
            remove(attributeButtons.get(attributeButtons.size() - 1));
            attributeButtons.remove(attributeButtons.size() - 1);
            attributeSelected.remove(attributeSelected.size() - 1);
        }
        createAndAddAttributeButtons(new String[]{"Citizenship", "PassportNum", "PersonName", "Age", "AnnualIncome", "PostalCode", "StreetAddress"});
        repaint();
    }

    public void addAttributeButtonsAfterDefault(String[] atts) {
        for (int i = thisAttributes.length; i < thisAttributes.length + atts.length; i++) {
            attributeSelected.add(false);
            JButton currB = new JButton(atts[i - thisAttributes.length]);
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
        repaint();
    }

    public void setAllFunctionButtonSelectToFalse() {
        searchButtonSelected = false;
        viewValueButtonSelected = false;
        viewAmenButtonSelected = false;
        viewCityButtonSelected = false;
        viewHouseButtonSelected = false;
        viewTownHouseButtonSelected = false;
        viewApartButtonSelected = false;
        viewFIRIPSelected = false;
        viewPOBISelected = false;
    }

    public void refreshTable() {
        remove(mainTablePanel);
        add(mainTablePanel);
        repaint();
    }

    public void shrinkMainAndAddSub() {
        mainTablePanel.setBounds(500, 400, 1400, 500);
        add(subTablePanel);
        repaint();
    }

    public void expandMainAndRemoveSub() {
        mainTablePanel.setBounds(500, 200, 1400, 700);
        remove(subTablePanel);
        repaint();
    }
}
