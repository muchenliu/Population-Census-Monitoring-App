package requestProcessor;

import controller.DataBaseConnectionHandler;
import tableHandlers.HandleOwnsVehicle;
import ui.DisplayTablePanel;
import ui.vehicleUI.VehiclePanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProcessVehicleRequest implements ProcessRequest {

    VehiclePanel vehiclePanel;
    HandleOwnsVehicle handleOwnsVehicle;
    DisplayTablePanel displayTablePanel;

    public ProcessVehicleRequest(VehiclePanel vehiclePanel, DataBaseConnectionHandler db, DisplayTablePanel dtp) {
        this.vehiclePanel = vehiclePanel;
        this.handleOwnsVehicle = db.setUpOwnsVehicleHandler();
        this.displayTablePanel = dtp;
    }

    public void processAdd(ArrayList<JTextField> values, ArrayList<JCheckBox> subClass) throws NumberFormatException, SQLException {
    }

    public void processDelete(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values) throws NumberFormatException, SQLException {
    }

    public void processSearch(ArrayList<JCheckBox> selected, ArrayList<JTextField> attributes, ArrayList<JTextField> operations, ArrayList<JTextField> values, String[] thisAttributes, ArrayList<Boolean> attSelected) throws NumberFormatException, SQLException {
    }

    public void processModify(ArrayList<JCheckBox> uSelected, ArrayList<JTextField> uValue, ArrayList<JCheckBox> cSelected, ArrayList<JTextField> cAttributes, ArrayList<JTextField> cOperations, ArrayList<JTextField> cValues) throws NumberFormatException, SQLException {
    }
}
