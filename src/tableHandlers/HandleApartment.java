package tableHandlers;

import controller.Util;
import queryTemplates.UpdateTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.CHAR;
import static tableHandlers.HandleProperty.PostalCodeLength;
import static tableHandlers.HandleProperty.StreetAddressLength;

public class HandleApartment {
    public static final int UnitLength = 10;
    public static final String[] IntAttributes = new String[]{"Floor"};
    public static final String[] StringAttributes = new String[]{"PostalCode", "StreetAddress", "Unit"};
    public static final int[] StringAttLen = new int[]{PostalCodeLength, StreetAddressLength, UnitLength};
    private static final String SQLERROR = "SQLException: ";
    private Connection c;
    private PreparedStatement ps;
    private Util util = new Util();

    public HandleApartment(Connection c) {
        this.c = c;
    }

    public void insertApartment(String PostalCode, String StreetAddress, String Unit, String Floor) {
        try {
            String query = "INSERT INTO Apartment (PostalCode, StreetAddress, Unit, Floor) VALUES (?,?,?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, PostalCode);
            ps.setString(2, StreetAddress);
            if (Unit.isEmpty()) {
                ps.setNull(3, CHAR);
            } else {
                ps.setString(3, Unit);
            }
            if (Floor.isEmpty()) {
                ps.setNull(4, CHAR);
            } else {
                ps.setString(4, Floor);
            }
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteApartment (String PostalCode, String StreetAddress) {
        try {
            String query = "DELETE FROM Apartment WHERE Postalcode = ? AND StreetAddress = ?";
            ps = c.prepareStatement(query);
            ps.setString(1, util.PadSpace(PostalCode, PostalCodeLength));
            ps.setString(2, util.PadSpace(StreetAddress, StreetAddressLength));
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("Apartment " + PostalCode + " " + StreetAddress  + " does not exist!");
            }
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void updateApartment (ArrayList<UpdateTemplate> updates, String postalCode, String streetAddress) {
        try {
            if (updates.size() == 0) {
                return;
            }
            String searchQuery = "SELECT * FROM Apartment WHERE PostalCode = ? AND StreetAddress = ?";
            ps = c.prepareStatement(searchQuery);
            ps.setString(1, util.PadSpace(postalCode, PostalCodeLength));
            ps.setString(2, util.PadSpace(streetAddress, StreetAddressLength));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String updateQuery = "UPDATE Apartment Set ";
                for (int i = 0; i < updates.size(); i ++) {
                    UpdateTemplate curr = updates.get(i);
                    updateQuery += curr.getAttribute();
                    updateQuery += " = ?";
                    if (i < updates.size() - 1) {
                        updateQuery += ", ";
                    }
                }
                updateQuery += " WHERE PostalCode = ? AND StreetAddress = ?";
                ps = c.prepareStatement(updateQuery);
                for (int i = 1; i <= updates.size(); i ++) {
                    UpdateTemplate curr = updates.get(i-1);
                    if (Arrays.asList(IntAttributes).contains(curr.getAttribute())) {
                        ps.setInt(i, Integer.parseInt(curr.getValue()));
                    } else {
                        int attIdx = Arrays.asList(StringAttributes).indexOf(curr.getAttribute());
                        if (attIdx == -1) {
                            throw new SQLException("invalid attribute");
                        }
                        int attLen = StringAttLen[attIdx];
                        ps.setString(i, util.PadSpace(curr.getValue(), attLen));
                    }
                }
                ps.setString(updates.size() + 1, util.PadSpace(postalCode, PostalCodeLength));
                ps.setString(updates.size() + 2, util.PadSpace(streetAddress, StreetAddressLength));
                ps.executeUpdate();
                c.commit();
            }
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }
}
