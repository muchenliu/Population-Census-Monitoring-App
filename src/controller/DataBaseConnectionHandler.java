package controller;

import tableHandlers.HandleOwnsVehicle;
import tableHandlers.HandleProperty;
import tableHandlers.HandleResidesIndividual;
import tableHandlers.HandleWorkplace;

import java.sql.*;

public class DataBaseConnectionHandler {

    private Connection connection = null;

    public DataBaseConnectionHandler() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }
            System.out.println("close connection");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", username, password);
            System.out.println("connected");
            connection.setAutoCommit(false);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public HandleProperty setUpPropertyHandler() {
        return new HandleProperty(connection);
    }

    public HandleResidesIndividual setUpIndividualHandler() {
        return new HandleResidesIndividual(connection);
    }

    public HandleWorkplace setUpWorkplaceHandler() {
        return new HandleWorkplace(connection);
    }

    public HandleOwnsVehicle setUpOwnsVehicleHandler() {
        return new HandleOwnsVehicle(connection);
    }
}
