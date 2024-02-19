package requestProcessor;

import aggregationReturnType.EmployeeAverageAge;
import controller.DataBaseConnectionHandler;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tableHandlers.HandleWorkplace;
import tables.Workplace;
import ui.DisplayTablePanel;
import ui.workplaceUI.WorkplacePanel;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessWorkplaceRequest implements ProcessRequest{
    private static final String[] attributes = new String[]{"PostalCode", "BranchAddress", "AnnualProfit", "CompanyName",
            "NumEmployee"};
    WorkplacePanel workplacePanel;
    HandleWorkplace handleWorkplace;
    DisplayTablePanel displayTablePanel;

    public ProcessWorkplaceRequest(WorkplacePanel workplacePanel, DataBaseConnectionHandler db, DisplayTablePanel dtp) {
        this.workplacePanel = workplacePanel;
        this.handleWorkplace = db.setUpWorkplaceHandler();
        this.displayTablePanel = dtp;
    }

    public void processAdd(ArrayList<JTextField> values, ArrayList<JCheckBox> subClass) throws NumberFormatException, SQLException {
        String postalCode = values.get(0).getText();
        String branchAddress= values.get(1).getText();
        int annualProfit = Integer.parseInt(values.get(2).getText());
        String companyName = values.get(3).getText();
        int numEmployee = Integer.parseInt(values.get(4).getText());
        if (postalCode.equals("") || branchAddress.equals("")) {
            throw new SQLException("Primary keys cannot be empty");
        } else {
            handleWorkplace.insertWorkplace(postalCode, branchAddress, annualProfit, companyName, numEmployee);
        }
    }

    public void processDelete(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values) throws NumberFormatException, SQLException {
        ArrayList<Comparison> compares = new ArrayList<>();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i).isSelected()) {
                compares.add(new Comparison(attributes.get(i).getText(), operations.get(i).getText(), values.get(i).getText()));
            }
        }
        handleWorkplace.deleteWorkplace(compares);
        workplacePanel.closeAllFrames();
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
            ArrayList<Workplace> workplaces = handleWorkplace.selectWorkplace(compares);
            String[] columns = {"PostalCode", "BranchAddress", "AnnualProfit", "CompanyName",
                    "NumEmployee"};
            String[][] data = new String[workplaces.size()][];
            for (int i = 0; i < workplaces.size(); i++) {
                Workplace currplace = workplaces.get(i);
                data[i] = new String[]{currplace.getPostalCode(), currplace.getBranchAddress(),
                        Integer.toString(currplace.getAnnualProfit()), currplace.getBranchAddress(),
                        Integer.toString(currplace.getNumEmployee())};
            }
            displayTablePanel.setInformation(data, columns);
        } else {
            ResultSet rs = handleWorkplace.projectWorkplace(projectedAtt, compares);
            String[] columns = new String[projectedAtt.size()];
            columns = projectedAtt.toArray(columns);
            ArrayList<String[]> tempData = new ArrayList<>();
            while (rs.next()) {
                String[] currData = new String[projectedAtt.size()];
                for (int i = 0; i < projectedAtt.size(); i++) {
                    String currAtt = projectedAtt.get(i);
                    if (Arrays.asList(HandleWorkplace.StringAttributes).contains(currAtt)) {
                        currData[i] = rs.getString(currAtt);
                    } else if (Arrays.asList(HandleWorkplace.IntAttributes).contains(currAtt)) {
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
        }
        workplacePanel.closeAllFrames();
        workplacePanel.refreshTable();
    }

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
        handleWorkplace.updateWorkplace(attributeUpdates, compares);
        workplacePanel.closeAllFrames();
    }

    public void processMinEmpAvgAgeWorkplace() {
        ArrayList<EmployeeAverageAge> emp = handleWorkplace.minEmployeeAgeWP();
        System.out.println(emp.toString());
        String[] columns = {"PostalCode", "BranchAddress", "AverageAge"};
        String[][] data = new String[emp.size()][];
        for (int i = 0; i < emp.size(); i++) {
            EmployeeAverageAge currwp = emp.get(i);
            data[i] = new String[]{currwp.getPostalCode(), currwp.getBranchAddress(), currwp.getAverageAge()};
        }
        displayTablePanel.setInformation(data, columns);
        displayTablePanel.setTitle("find workplace which has the min average employee age");
        workplacePanel.refreshTable();
    }
}
