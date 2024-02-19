package requestProcessor;

import aggregationReturnType.HouseholdAvgIncome;
import aggregationReturnType.MaxOwnedValue;
import controller.DataBaseConnectionHandler;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tableHandlers.HandleResidesIndividual;
import tables.ResidesIndividual;
import tables.ResidesIndividualType;
import ui.DisplayTablePanel;
import ui.individualUI.IndividualPanel;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessIndividualRequest implements ProcessRequest {

    private static final String[] attributes = new String[]{"Citizenship", "PassportNum", "PersonName", "Age",
            "AnnualIncome", "PostalCode", "StreetAddress", "LeaseTerm", "OwnedStreetAddress", "OwnedPostalCode",
            "DateOfPurchase"};
    IndividualPanel individualPanel;
    HandleResidesIndividual handleResidesIndividual;
    DisplayTablePanel displayTablePanel;

    public ProcessIndividualRequest(IndividualPanel individualPanel, DataBaseConnectionHandler db, DisplayTablePanel dtp) {
        this.individualPanel = individualPanel;
        this.handleResidesIndividual = db.setUpIndividualHandler();
        this.displayTablePanel = dtp;
    }
    @Override
    public void processAdd(ArrayList<JTextField> values, ArrayList<JCheckBox> subClass) throws NumberFormatException, SQLException {
        String citizenship = values.get(0).getText();
        String passportNum = values.get(1).getText();
        String personName = values.get(2).getText();
        int age = Integer.parseInt(values.get(3).getText());
        int annualIncome = Integer.parseInt(values.get(4).getText());
        String postalCode = values.get(5).getText();
        String streetAddress = values.get(6).getText();
        String leaseTerm = values.get(7).getText();
        String ownedStreetAddress = values.get(8).getText();
        String ownedPostalCode = values.get(9).getText();
        String dateOfPurchase = values.get(10).getText();
        if (citizenship.equals("") || passportNum.equals("")) {
            throw new SQLException("Primary keys cannot be empty");
        }
        if (!subClass.get(0).isSelected() && !subClass.get(1).isSelected() && !subClass.get(2).isSelected()) {
            handleResidesIndividual.insertResidesIndividual(citizenship, passportNum, personName, age, annualIncome,
                    postalCode, streetAddress, leaseTerm, ownedStreetAddress, ownedPostalCode, dateOfPurchase, ResidesIndividualType.NULL);
            individualPanel.closeAllFrames();
        } else if (subClass.get(0).isSelected() && !subClass.get(1).isSelected() && !subClass.get(2).isSelected()) {
            handleResidesIndividual.insertResidesIndividual(citizenship, passportNum, personName, age, annualIncome,
                    postalCode, streetAddress, leaseTerm, ownedStreetAddress, ownedPostalCode, dateOfPurchase, ResidesIndividualType.OWNER);
            individualPanel.closeAllFrames();
        } else if (!subClass.get(0).isSelected() && subClass.get(1).isSelected() && !subClass.get(2).isSelected()) {
            handleResidesIndividual.insertResidesIndividual(citizenship, passportNum, personName, age, annualIncome,
                    postalCode, streetAddress, leaseTerm, ownedStreetAddress, ownedPostalCode, dateOfPurchase, ResidesIndividualType.RENTER);
            individualPanel.closeAllFrames();
        } else if (!subClass.get(0).isSelected() && !subClass.get(1).isSelected() && subClass.get(2).isSelected()) {
            handleResidesIndividual.insertResidesIndividual(citizenship, passportNum, personName, age, annualIncome,
                    postalCode, streetAddress, leaseTerm, ownedStreetAddress, ownedPostalCode, dateOfPurchase, ResidesIndividualType.OCCUPANT);
            individualPanel.closeAllFrames();
        } else {
            throw new SQLException("Should select one and only one subtype");
        }
    }

    @Override
    public void processDelete(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        handleResidesIndividual.deleteResidesIndividual(compares);
        individualPanel.closeAllFrames();
    }

    @Override
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
            ArrayList<ResidesIndividual> individuals = handleResidesIndividual.selectResidesIndividual(compares);
            String[] columns = {"Citizenship", "PassportNum", "PersonName", "Age",
                    "AnnualIncome", "PostalCode", "StreetAddress"};
            String[][] data = new String[individuals.size()][];
            for (int i = 0; i < individuals.size(); i++) {
                ResidesIndividual currInd = individuals.get(i);
                data[i] = new String[]{currInd.getCitizenship(), currInd.getPassportNum(), currInd.getPersonName(),
                        Integer.toString(currInd.getAge()), Integer.toString(currInd.getAnnualIncome()), currInd.getPostalCode(),
                        currInd.getStreetAddress()};
            }
            displayTablePanel.setInformation(data, columns);
        } else {
            ResultSet rs = handleResidesIndividual.projectResidesIndividual(projectedAtt, compares);
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
            String[][] data = new String[tempData.size()][];
            data = tempData.toArray(data);
            displayTablePanel.setInformation(data, columns);
            //System.out.println(data);
        }
        individualPanel.closeAllFrames();
        individualPanel.refreshTable();
    }

    @Override
    public void processModify(ArrayList<JCheckBox> uSelected, ArrayList<JTextField> uValue, ArrayList<JCheckBox> cSelected, ArrayList<JTextField> cAttributes, ArrayList<JTextField> cOperations, ArrayList<JTextField> cValues) throws NumberFormatException, SQLException {
        ArrayList<UpdateTemplate> attributeUpdates = new ArrayList<>();
        for (int i = 0; i < uSelected.size(); i++) {
            if (uSelected.get(i).isSelected()) {
                UpdateTemplate currTemp = new UpdateTemplate(attributes[i], uValue.get(i).getText());
                attributeUpdates.add(currTemp);
            }
        }
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < cSelected.size(); i++) {
            if (cSelected.get(i).isSelected()) {
                compares.add(new Comparison(cAttributes.get(i).getText(), cOperations.get(i).getText(), cValues.get(i).getText()));
            }
        }
        handleResidesIndividual.updateResidesIndividual(attributeUpdates, compares);
        individualPanel.closeAllFrames();
    }

    public void processAverageHouseholdIncome() {
        ArrayList<HouseholdAvgIncome> households = handleResidesIndividual.averageHouseholdIncome();
        System.out.println(households.toString());
        String[] columns = {"StreetAddress", "PostalCode", "AnnualIncome"};
        String[][] data = new String[households.size()][];
        for (int i = 0; i < households.size(); i++) {
            HouseholdAvgIncome currHousehold = households.get(i);
            data[i] = new String[]{currHousehold.getStreetAddress(), currHousehold.getPostalCode(), currHousehold.getAnnualIncome()};
        }
        displayTablePanel.setInformation(data, columns);
        displayTablePanel.setTitle("annual average household income");
        individualPanel.refreshTable();
    }

    public void processMaxOwnedValue() {
        ArrayList<MaxOwnedValue> maxV = handleResidesIndividual.ownerMaxValue();
        System.out.println(maxV.toString());
        String[] columns = {"Citizenship", "PassportNum", "ValueOfOwnedProperty"};
        String[][] data = new String[maxV.size()][];
        for (int i = 0; i < maxV.size(); i++) {
            MaxOwnedValue maxOwnedValue = maxV.get(i);
            data[i] = new String[]{maxOwnedValue.getCitizenship(), maxOwnedValue.getPassportNum(),
                    maxOwnedValue.getMaxValue()};
        }
        displayTablePanel.setInformation(data, columns);
        displayTablePanel.setTitle("find the highest value property for each owner who owns at least 2 properties");
        individualPanel.refreshTable();
    }
}
