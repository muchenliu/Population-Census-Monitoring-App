package requestProcessor;

import controller.DataBaseConnectionHandler;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tableHandlers.*;
import tables.Property;
import tables.PropertyType;
import ui.DisplayTablePanel;
import ui.propertyUI.PropertyPanel;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessPropertyRequest implements ProcessRequest{
    private static final String[] attributes = new String[]{"PostalCode", "StreetAddress", "PropSize", "NumBedroom", "NumBathroom", "Unit", "Floor"};
    PropertyPanel propertyPanel;
    HandleProperty handleProperty;
    DisplayTablePanel displayTablePanel;
    DisplayTablePanel subTablePanel;

    public ProcessPropertyRequest (PropertyPanel propertyPanel, DataBaseConnectionHandler db, DisplayTablePanel dtp, DisplayTablePanel subTP) {
        this.propertyPanel = propertyPanel;
        this.handleProperty = db.setUpPropertyHandler();
        this.displayTablePanel = dtp;
        this.subTablePanel = subTP;
    }

    public void processAdd(ArrayList<JTextField> values, ArrayList<JCheckBox> subClass) throws NumberFormatException, SQLException {
        String postalCode = values.get(0).getText();
        String streetAddress = values.get(1).getText();
        double propSize = Double.parseDouble(values.get(2).getText());
        int numBedroom = Integer.parseInt(values.get(3).getText());
        int numBathroom = Integer.parseInt(values.get(4).getText());
        String unit = values.get(5).getText();
        String floor = values.get(6).getText();
        if (postalCode.equals("") || streetAddress.equals("")) {
            throw new SQLException("Primary keys cannot be empty");
        }
        if (subClass.get(0).isSelected() && !subClass.get(1).isSelected() && !subClass.get(2).isSelected()) {
            handleProperty.insertProperty(postalCode, streetAddress, propSize, numBedroom, numBathroom, unit, floor, PropertyType.HOUSE);
            propertyPanel.closeAllFrames();
        } else if (!subClass.get(0).isSelected() && subClass.get(1).isSelected() && !subClass.get(2).isSelected()) {
            handleProperty.insertProperty(postalCode, streetAddress, propSize, numBedroom, numBathroom, unit, floor, PropertyType.TOWNHOUSE);
            propertyPanel.closeAllFrames();
        } else if (!subClass.get(0).isSelected() && !subClass.get(1).isSelected() && subClass.get(2).isSelected()) {
            handleProperty.insertProperty(postalCode, streetAddress, propSize, numBedroom, numBathroom, unit, floor, PropertyType.APARTMENT);
            propertyPanel.closeAllFrames();
        } else {
            throw new SQLException("Should select one and only one subtype");
        }
    }

    public void processDelete(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        handleProperty.deleteProperty(compares);
        propertyPanel.closeAllFrames();
    }

    public void processSearch(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        boolean keepAll = true;
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            } else {
                keepAll = false;
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        if (keepAll) {
            ArrayList<Property> properties = handleProperty.selectProperties(compares);
            String[] columns = {"PostalCode", "StreetAddress", "PropSize", "NumBedroom", "NumBathroom"};
            String[][] data = new String[properties.size()][];
            for (int i = 0; i < properties.size(); i++) {
                Property currProp = properties.get(i);
                data[i] = new String[]{currProp.getPostalCode(), currProp.getStreetAddress(), Double.toString(currProp.getPropSize()), Integer.toString(currProp.getNumBedroom()), Integer.toString(currProp.getNumBathroom())};
            }
            displayTablePanel.setInformation(data, columns);
        } else {
            ResultSet rs = handleProperty.projectProperties(projectedAtt, compares);
            String[] columns = new String[projectedAtt.size()];
            columns = projectedAtt.toArray(columns);
            ArrayList<String[]> tempData = new ArrayList<>();
            while (rs.next()) {
                String[] currData = new String[projectedAtt.size()];
                for (int i = 0; i < projectedAtt.size(); i++) {
                    String currAtt = projectedAtt.get(i);
                    if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt)) {
                        currData[i] = rs.getString(currAtt);
                    } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                        currData[i] = Double.toString(rs.getDouble(currAtt));
                    } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt)) {
                        currData[i] = Integer.toString(rs.getInt(currAtt));
                    } else {
                        throw new SQLException("invalid attribute");
                    }
                }
                tempData.add(currData);
            }
            handleProperty.closePS();
            String[][] data = new String[tempData.size()][];
            data = tempData.toArray(data);
            displayTablePanel.setInformation(data, columns);
        }
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processModify(ArrayList<JCheckBox> uSelected, ArrayList<JTextField> uValue, ArrayList<JCheckBox> cSelected, ArrayList<JTextField> cAttributes, ArrayList<JTextField> cOperations, ArrayList<JTextField> cValues) throws NumberFormatException, SQLException {
        ArrayList<UpdateTemplate> primaryUpdates = new ArrayList<>();
        ArrayList<UpdateTemplate> attributeUpdates = new ArrayList<>();
        ArrayList<UpdateTemplate> subClassUpdates = new ArrayList<>();
        for (int i = 0; i < uSelected.size(); i++) {
            if (uSelected.get(i).isSelected()) {
                UpdateTemplate currTemp = new UpdateTemplate(attributes[i], uValue.get(i).getText());
                if (i < 2) {
                    primaryUpdates.add(currTemp);
                } else if (i < 5) {
                    attributeUpdates.add(currTemp);
                } else {
                    subClassUpdates.add(currTemp);
                }
            }
        }
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < cSelected.size(); i++) {
            if (cSelected.get(i).isSelected()) {
                compares.add(new Comparison(cAttributes.get(i).getText(), cOperations.get(i).getText(), cValues.get(i).getText()));
            }
        }
        handleProperty.updateProperty(primaryUpdates, attributeUpdates, subClassUpdates, compares);
        propertyPanel.closeAllFrames();
    }

    public void processViewValue(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        ResultSet rs = handleProperty.viewPropertyValue(projectedAtt, compares);
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt) || Arrays.asList(HandleHasValue.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt) || Arrays.asList(HandleHasValue.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else if (Arrays.asList(HandleHasValue.DateAttributes).contains(currAtt)) {
                    currData[i] = rs.getDate(currAtt).toString();
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processViewAmen(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        ResultSet rs = handleProperty.viewAmenities(projectedAtt, compares);
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt) || Arrays.asList(HandleAmenities.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processViewCity(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        ResultSet rs = handleProperty.viewCity(projectedAtt, compares);
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt) || Arrays.asList(HandlePostalCode.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processViewHouse(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        ResultSet rs = handleProperty.viewHouse(projectedAtt, compares);
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt) || Arrays.asList(HandleHouse.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processViewTownhouse(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        ResultSet rs = handleProperty.viewTownhouse(projectedAtt, compares);
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt) || Arrays.asList(HandleTownHouse.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processViewApartment(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < thisAttributes.length; i++) {
            if (attSelected.get(i)) {
                projectedAtt.add(thisAttributes[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        ResultSet rs = handleProperty.viewApartment(projectedAtt, compares);
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt) || Arrays.asList(HandleApartment.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt) || Arrays.asList(HandleApartment.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processFIRIP(ArrayList<JTextField> values, String[] indAtt, ArrayList<Boolean> selectedAtt) throws NumberFormatException, SQLException {
        String postalCode = values.get(0).getText();
        String streetAddress = values.get(1).getText();
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < indAtt.length; i++) {
            if (selectedAtt.get(i)) {
                projectedAtt.add(indAtt[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        if (postalCode.equals("") || streetAddress.equals("")) {
            throw new SQLException("Primary keys cannot be empty");
        }
        ResultSet rs = handleProperty.findIndividualsResideProperty(postalCode, streetAddress, projectedAtt);
        propertyPanel.closeAllFrames();
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleResidesIndividual.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleResidesIndividual.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        displayTablePanel.setTitle("Individuals Resides In Property with PostalCode = \"" + values.get(0).getText().trim() + "\" and StreetAddress = \"" + values.get(1).getText().trim() + "\"");
        propertyPanel.closeAllFrames();
        propertyPanel.expandMainAndRemoveSub();
        propertyPanel.refreshTable();
    }

    public void processFPOBI(ArrayList<JCheckBox> selected, ArrayList<JTextField> citizenships, ArrayList<JTextField> passportNums, ArrayList<Boolean> selectedAtt, String[] propAtt) throws NumberFormatException, SQLException {
        ArrayList<String> projectedAtt = new ArrayList<String>();
        for (int i = 0; i < propAtt.length; i++) {
            if (selectedAtt.get(i)) {
                projectedAtt.add(propAtt[i]);
            }
        }
        if (projectedAtt.isEmpty()) {
            throw new SQLException("Please select columns to view");
        }
        boolean hasInd = false;
        ArrayList<String> keepCiti = new ArrayList<>();
        ArrayList<String> keepPPNum = new ArrayList<>();
        ArrayList<String[]> tempSubData = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                hasInd = true;
                keepCiti.add(citizenships.get(i).getText());
                keepPPNum.add(passportNums.get(i).getText());
                tempSubData.add(new String[]{citizenships.get(i).getText(), passportNums.get(i).getText()});
            }
        }
        if (!hasInd) {
            throw new SQLException("Please provide at least one individual");
        }
        ResultSet rs = handleProperty.findPropertiesOwnByIndividuals(keepCiti, keepPPNum, projectedAtt);
        propertyPanel.closeAllFrames();
        String[] columns = new String[projectedAtt.size()];
        columns = projectedAtt.toArray(columns);
        ArrayList<String[]> tempData = new ArrayList<>();
        while (rs.next()) {
            String[] currData = new String[projectedAtt.size()];
            for (int i = 0; i < projectedAtt.size(); i++) {
                String currAtt = projectedAtt.get(i);
                if (Arrays.asList(HandleProperty.StringAttributes).contains(currAtt)) {
                    currData[i] = rs.getString(currAtt);
                } else if (Arrays.asList(HandleProperty.IntAttributes).contains(currAtt)) {
                    currData[i] = Integer.toString(rs.getInt(currAtt));
                } else if (Arrays.asList(HandleProperty.DoubleAttributes).contains(currAtt)) {
                    currData[i] = Double.toString(rs.getDouble(currAtt));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
            tempData.add(currData);
        }
        handleProperty.closePS();
        String[][] data = new String[tempData.size()][];
        data = tempData.toArray(data);
        displayTablePanel.setInformation(data, columns);
        String[] subColumns = new String[]{"Citizenship", "PassportNumber"};
        String[][] subData = new String[tempSubData.size()][];
        subData = tempSubData.toArray(subData);
        subTablePanel.setInformation(subData, subColumns);
        propertyPanel.closeAllFrames();
        propertyPanel.shrinkMainAndAddSub();
        subTablePanel.setTitle("Properties Owned By the Following Individuals");
        propertyPanel.refreshTable();
    }
}
