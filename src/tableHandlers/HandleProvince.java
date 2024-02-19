package tableHandlers;

import controller.Util;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tables.Province;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.NUMBER;

public class HandleProvince {
    private static final String SQLERROR = "SQLException: ";
    public static final int PNameLength = 30;

    public static final String[] IntAttributes = new String[]{"AvgIncome", "Population"};
    public static final String[] StringAttributes = new String[]{"PName"};
    public static final int[] StringAttLen = new int[]{PNameLength};
    private Connection c;
    private PreparedStatement ps;
    private Util util = new Util();

    public HandleProvince(Connection c) {
        this.c = c;
    }

    public void insertProvince(String PName, int AvgIncome, int Population) {
        try {
            String query = "INSERT INTO Province (PName, AvgIncome, Population) VALUES (?,?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, PName);
            if (AvgIncome > 0) {
                ps.setInt(2, AvgIncome);
            } else {
                ps.setNull(2, NUMBER);
            }
            if (Population > 0) {
                ps.setInt(3, Population);
            } else {
                ps.setNull(3, NUMBER);
            }
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteProvince (ArrayList<Comparison> compares) {
        try {
            if (compares.isEmpty()) {
                throw new SQLException("Warning: unsafe operation");
            }
            String query = "DELETE FROM Province WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Province.", StringAttributes));
            util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void updateProvince (ArrayList<UpdateTemplate> attributeUpdates, ArrayList<Comparison> compares) {
        try {
            if (compares.isEmpty()) {
                throw new SQLException("Warning: unsafe operation");
            }
            if (!attributeUpdates.isEmpty()) {
                String query = "UPDATE Province SET ";
                for (int i = 0; i < attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i);
                    query += currUT.getAttribute();
                    query += " = ?";
                    if (i < attributeUpdates.size() - 1) {
                        query += ", ";
                    }
                }
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Province.", StringAttributes));
                int i;
                for (i = 1; i <= attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i - 1);
                    if (Arrays.asList(IntAttributes).contains(currUT.getAttribute())) {
                        ps.setInt(i, Integer.parseInt(currUT.getValue()));
                    } else {
                        int attIdx = Arrays.asList(StringAttributes).indexOf(currUT.getAttribute());
                        if (attIdx == -1) {
                            throw new SQLException("invalid attribute");
                        }
                        int attLen = StringAttLen[attIdx];
                        ps.setString(i, util.PadSpace(currUT.getValue(), attLen));
                    }
                }
                util.setWhereValuesInternal(ps, compares, i, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
                ps.executeUpdate();
                c.commit();
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public ArrayList<Province> viewProvinces() throws SQLException {
        ArrayList<Province> results = new ArrayList<Province>();
        String query = "SELECT * FROM Province";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Province curr = new Province(rs.getString("PName"),
                    rs.getInt("AvgIncome"),
                    rs.getInt("Population"));
            results.add(curr);
        }
        ps.close();
        return results;
    }

    public ArrayList<Province> selectProvinces(ArrayList<Comparison> compares) {
        try {
            if (compares.size() == 0) {
                return viewProvinces();
            }
            ArrayList<Province> results = new ArrayList<Province>();
            String query = "SELECT * FROM Province WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Province.", StringAttributes));
            util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Province curr = new Province(rs.getString("PName"),
                        rs.getInt("AvgIncome"),
                        rs.getInt("Population"));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<Province>();
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            return new ArrayList<Province>();
        }
    }

    public ResultSet projectProvinces(ArrayList<String> attributes, ArrayList<Comparison> compares) {
        try {
            String query = util.projectHelperPrepareSelectClause(attributes, "Province.", new String[0], IntAttributes, new String[0], StringAttributes);
            query += " FROM Province";
            if (compares.size() > 0) {
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Province.", StringAttributes));
                util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
                return ps.executeQuery();
            } else {
                ps = c.prepareStatement(query);
                return ps.executeQuery();
            }
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            return null;
        }
    }
}
