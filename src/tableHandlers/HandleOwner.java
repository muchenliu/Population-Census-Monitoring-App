package tableHandlers;

import controller.Util;
import tables.OwnsProperty;

import java.sql.*;
import java.util.ArrayList;

import static oracle.jdbc.OracleTypes.DATE;

public class HandleOwner {

    private static final String SQLERROR = "SQLException: ";

    public static final int CITIZENSHIP_LENGTH = HandleResidesIndividual.CITIZENSHIP_LENGTH;
    public static final int PASSPORTNUM_LENGTH = HandleResidesIndividual.PASSPORTNUM_LENGTH;


    public static final String[] PrimaryKeyAttributes = new String[]{"Citizenship", "PassportNum"};

    public static final String[] StringAttributes = new String[]{"Citizenship", "PassportNum"};

    public static final int[] StringAttLen = new int[]{CITIZENSHIP_LENGTH, PASSPORTNUM_LENGTH};

    private Connection c;

    private PreparedStatement ps;

    private Util util = new Util();

    public HandleOwner(Connection c) { this.c = c; }

    public void insertOwner(String citizenship, String passportNum, String postalCode, String streetAddress, String date) {
        try {
            String query = "INSERT INTO Owner (Citizenship, PassportNum) VALUES (?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, citizenship);
            ps.setString(2, passportNum);
            ps.executeUpdate();
            c.commit();
            ps.close();
            insertOwnsProperty(citizenship, passportNum, postalCode, streetAddress, date);
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void insertOwnsProperty(String citizenship, String passportNum, String postalCode, String streetAddress, String date) {
       try {
           String query = "INSERT INTO OwnsProperty(Citizenship, PassportNum, PostalCode, StreetAddress, DateOfPurchase) VALUES (?,?,?,?,?)";
           ps = c.prepareStatement(query);
           ps.setString(1, citizenship);
           ps.setString(2, passportNum);
           ps.setString(3, postalCode);
           ps.setString(4, streetAddress);
           if (date.length() > 0) {
               ps.setDate(5, Date.valueOf(date));
           } else {
               ps.setNull(5, DATE);
           }
           ps.executeUpdate();
           c.commit();
           ps.close();
       } catch (SQLException e) {
           System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteOwner (String citizenship, String passportNum) {
        try {
            String query = "DELETE FROM Owner WHERE Citizenship = ? AND PassportNum = ?";
            ps = c.prepareStatement(query);
            ps.setString(1, util.PadSpace(citizenship, CITIZENSHIP_LENGTH));
            ps.setString(2, util.PadSpace(passportNum, PASSPORTNUM_LENGTH));
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("Owner " + citizenship + " " + passportNum  + " does not exist!");
            }
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public ArrayList<OwnsProperty> viewOwnsProperty() throws SQLException {
        ArrayList<OwnsProperty> results = new ArrayList<OwnsProperty>();
        String query = "SELECT * FROM OwnsProperty";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            OwnsProperty curr = new OwnsProperty(rs.getString("Citizenship"),
                    rs.getString("PassportNum"),
                    rs.getString("PostalCode"),
                    rs.getString("StreetAddress"),
                    rs.getDate("DateOfPurchase").toString());
            results.add(curr);
        }
        ps.close();
        return results;
    }

}
