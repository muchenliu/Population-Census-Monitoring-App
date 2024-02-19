package tableHandlers;

import controller.Util;
import queryTemplates.UpdateTemplate;
import tables.Renter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.DATE;

public class HandleRenter {
    private static final String SQLERROR = "SQLException: ";

    public static final int CITIZENSHIP_LENGTH = HandleResidesIndividual.CITIZENSHIP_LENGTH;
    public static final int PASSPORTNUM_LENGTH = HandleResidesIndividual.PASSPORTNUM_LENGTH;


    public static final String[] PrimaryKeyAttributes = new String[]{"Citizenship", "PassportNum"};

    public static final String[] StringAttributes = new String[]{"Citizenship", "PassportNum"};

    public static final int[] StringAttLen = new int[]{CITIZENSHIP_LENGTH, PASSPORTNUM_LENGTH};

    public static final String[] DateAttributes = new String[]{"LeaseTerm"};



    private Connection c;

    private PreparedStatement ps;

    private Util util = new Util();

    public HandleRenter(Connection c) { this.c = c; }

    public void insertRenter(String citizenship, String passportNum, String leaseTerm) {
        try {
            String query = "INSERT INTO Renter (Citizenship, PassportNum, LeaseTerm) VALUES (?,?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, citizenship);
            ps.setString(2, passportNum);
            if (leaseTerm.length() > 0) {
                ps.setDate(3, Date.valueOf(leaseTerm));
            } else {
                ps.setNull(3, DATE);
            }
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteRenter (String citizenship, String passportNum) {
        try {
            String query = "DELETE FROM Renter WHERE Citizenship = ? AND PassportNum = ?";
            ps = c.prepareStatement(query);
            ps.setString(1, util.PadSpace(citizenship, CITIZENSHIP_LENGTH));
            ps.setString(2, util.PadSpace(passportNum, PASSPORTNUM_LENGTH));
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("Renter " + citizenship + " " + passportNum  + " does not exist!");
            }
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void updateRenter (ArrayList<UpdateTemplate> updates, String citizenship, String passportNum) {
        try {
            if (updates.size() == 0) {
                return;
            }
            String searchQuery = "SELECT * FROM Renter WHERE Citizenship = ? AND PassportNum = ?";
            ps = c.prepareStatement(searchQuery);
            ps.setString(1, util.PadSpace(citizenship, CITIZENSHIP_LENGTH));
            ps.setString(2, util.PadSpace(passportNum, PASSPORTNUM_LENGTH));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String updateQuery = "UPDATE Renter Set ";
                for (int i = 0; i < updates.size(); i ++) {
                    UpdateTemplate curr = updates.get(i);
                    updateQuery += curr.getAttribute();
                    updateQuery += " = ?";
                    if (i < updates.size() - 1) {
                        updateQuery += ", ";
                    }
                }
                updateQuery += " WHERE Citizenship = ? AND PassportNum = ?";
                ps = c.prepareStatement(updateQuery);
                for (int i = 1; i <= updates.size(); i ++) {
                    UpdateTemplate curr = updates.get(i-1);
                    if (Arrays.asList(DateAttributes).contains(curr.getAttribute())) {
                        ps.setDate(i, Date.valueOf(curr.getValue()));
                    }
                }
                ps.executeUpdate();
                c.commit();
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public ArrayList<Renter> viewRenter() throws SQLException {
            ArrayList<Renter> results = new ArrayList<Renter>();
            String query = "SELECT * FROM Renter";
            ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Renter curr = new Renter(rs.getString("Citizenship"),
                        rs.getString("PassportNum"),
                        rs.getDate("LeaseTerm").toString());
                results.add(curr);
            }
            ps.close();
            return results;
        }
}
