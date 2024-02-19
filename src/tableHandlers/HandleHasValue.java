package tableHandlers;

import controller.Util;
import queryTemplates.UpdateTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static tableHandlers.HandleProperty.PostalCodeLength;
import static tableHandlers.HandleProperty.StreetAddressLength;

public class HandleHasValue {

    public static final String[] DoubleAttributes = new String[]{"SalvageValue"};
    public static final String[] IntAttributes = new String[]{"PropertyAge"};
    public static final String[] DateAttributes = new String[]{"LastRenovation"};
    public static final String[] StringAttributes = new String[]{"PostalCode", "StreetAddress"};
    public static final int[] StringAttLen = new int[]{PostalCodeLength, StreetAddressLength};
    private static final String SQLERROR = "SQLException: ";
    private Connection c;
    private PreparedStatement ps;
    private Util util = new Util();

    public HandleHasValue(Connection c) {
        this.c = c;
    }

    public void updateHasValue (ArrayList<UpdateTemplate> updates, String postalCode, String streetAddress) {
        try {
            if (updates.size() == 0) {
                return;
            }
            String searchQuery = "SELECT * FROM HasValue WHERE PostalCode = ? AND StreetAddress = ?";
            ps = c.prepareStatement(searchQuery);
            ps.setString(1, util.PadSpace(postalCode, PostalCodeLength));
            ps.setString(2, util.PadSpace(streetAddress, StreetAddressLength));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String updateQuery = "UPDATE HasValue Set ";
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
                    int attIdx = Arrays.asList(StringAttributes).indexOf(curr.getAttribute());
                    if (attIdx == -1) {
                        throw new SQLException("invalid attribute");
                    }
                    int attLen = StringAttLen[attIdx];
                    ps.setString(i, util.PadSpace(curr.getValue(), attLen));
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
