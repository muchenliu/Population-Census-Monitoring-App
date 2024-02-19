package tableHandlers;

import controller.Util;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tables.OwnsVehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.CHAR;

public class HandleOwnsVehicle {

    private static final String SQLERROR = "SQLException: ";
    public static final int VIN_LENGTH = 30;
    public static final int COLOR_LENGTH = 20;
    public static final int TYPE_LENGTH = 20;
    public static final int BRAND_LENGTH = 20;
    public static final int LICENSEPLATE_LENGTH = 10;
    public static final int MODEL_LENGTH = 20;
    public static final int CITIZENSHIP_LENGTH = HandleResidesIndividual.CITIZENSHIP_LENGTH;
    public static final int PASSPORTNUM_LENGTH = HandleResidesIndividual.PASSPORTNUM_LENGTH;


    public static final String[] PrimaryKeyAttributes = new String[]{"VIN"};
    public static final String[] StringAttributes = new String[]{"VIN", "Color", "Type", "Brand",
            "LicensePlate", "Model", "Citizenship", "PassportNum"};

    public static final int[] StringAttLen = new int[]{VIN_LENGTH, COLOR_LENGTH, TYPE_LENGTH, BRAND_LENGTH,
            LICENSEPLATE_LENGTH, MODEL_LENGTH, CITIZENSHIP_LENGTH, PASSPORTNUM_LENGTH};

    private Connection c;

    private PreparedStatement ps;

    private Util util = new Util();

    public HandleOwnsVehicle(Connection c) { this.c = c; }

    public void insertOwnsVehicle(String vin, String color, String type, String brand, String licensePlate,
                                  String model, String citizenship, String passportNum) {
        try {
            String query = "INSERT INTO OwnsVehicle (VIN, Color, Type, Brand, " +
                    "LicensePlate, Model, Citizenship, PassportNum) VALUES (?,?,?,?,?,?,?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, vin);
            if (color.length() > 0) {
                ps.setString(2, color);
            } else {
                ps.setNull(2, CHAR);
            }
            if (type.length() > 0) {
                ps.setString(3, type);
            } else {
                ps.setNull(3, CHAR);
            }
            if (brand.length() > 0) {
                ps.setString(4, brand);
            } else {
                ps.setNull(4, CHAR);
            }
            if (licensePlate.length() > 0) {
                ps.setString(5, licensePlate);
            } else {
                ps.setNull(5, CHAR);
            }
            if (model.length() > 0) {
                ps.setString(6, model);
            } else {
                ps.setNull(6, CHAR);
            }
            ps.setString(7, citizenship);
            ps.setString(8, passportNum);
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteOwnsVehicle(ArrayList<Comparison> compares) throws SQLException {
        if (compares.isEmpty()) {
            throw new SQLException("Warning: unsafe operation");
        }
        String query = "DELETE FROM OwnsVehicle WHERE ";
        ps = c.prepareStatement(util.prepareWhereClause(query, compares, "OwnsVehicle.", PrimaryKeyAttributes));
        util.setWhereValuesInternal(ps, compares, 1, new String[0], new String [0], new String[0], StringAttributes, StringAttLen);
        ps.executeUpdate();
        c.commit();
        ps.close();
    }

    public void updateOwnsVehicle(ArrayList<UpdateTemplate> attributeUpdates, ArrayList<Comparison> comparisons) {
        try {
            if (comparisons.isEmpty()) {
                throw new SQLException("Warning: unsafe operation");
            }
            if (!attributeUpdates.isEmpty()) {
                String query = "UPDATE OwnsVehicle SET ";
                for (int i = 0; i < attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i);
                    query += currUT.getAttribute();
                    query += " = ?";
                    if (i < attributeUpdates.size() - 1) {
                        query += ", ";
                    }
                }
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, comparisons, "OwnsVehicle.", PrimaryKeyAttributes));
                int idx;
                for (idx = 1; idx < attributeUpdates.size(); idx++) {
                    UpdateTemplate currUT = attributeUpdates.get(idx - 1);
                    int attIdx = Arrays.asList(StringAttributes).indexOf(currUT.getAttribute());
                    if (attIdx == -1) {
                        throw new SQLException("invalid attribute");
                    }
                    int attLen = StringAttLen[attIdx];
                    ps.setString(idx, util.PadSpace(currUT.getValue(), attLen));
                }
                util.setWhereValuesInternal(ps, comparisons, idx, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
                ps.executeUpdate();
                c.commit();
                ps.close();
            }
            } catch(SQLException e){
                System.out.println(SQLERROR + e.getMessage());
            }
        }

    public ArrayList<OwnsVehicle> viewOwnsVehicle() throws SQLException {
        ArrayList<OwnsVehicle> results = new ArrayList<OwnsVehicle>();
        String query = "SELECT * FROM OwnsVehicle";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            OwnsVehicle curr = new OwnsVehicle(rs.getString("VIN"), rs.getString("Color"),
                    rs.getString("Type"), rs.getString("Brand"), rs.getString(("LicensePlate")),
                    rs.getString("Model"), rs.getString("Citizenship"),
                    rs.getString("PassportNum"));
            results.add(curr);
        }
        ps.close();
        return results;
    }

    public ArrayList<OwnsVehicle> selectOwnsVehicle(ArrayList<Comparison> compares) {
        try {
            if (compares.size() == 0) {
                return viewOwnsVehicle();
            }
            ArrayList<OwnsVehicle> results = new ArrayList<OwnsVehicle>();
            String query = "SELECT * FROM OwnsVehicle WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "OwnsVehicle.", PrimaryKeyAttributes));
            util.setWhereValuesInternal(ps, compares, 1, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OwnsVehicle curr = new OwnsVehicle(rs.getString("VIN"), rs.getString("Color"),
                        rs.getString("Type"), rs.getString("Brand"), rs.getString(("LicensePlate")),
                        rs.getString("Model"), rs.getString("Citizenship"),
                        rs.getString("PassportNum"));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<OwnsVehicle>();
        }
    }

    public ResultSet projectOwnsVehicle(ArrayList<String> attributes, ArrayList<Comparison> compares) {
        try {
            String query = util.projectHelperPrepareSelectClause(attributes, "OwnsVehicle.",
                    new String[0], new String[0], new String[0], StringAttributes);
            query += " FROM OwnsVehicle";
            if (compares.size() > 0) {
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "OwnsVehicle.", PrimaryKeyAttributes));
                util.setWhereValuesInternal(ps, compares, 1, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
                return ps.executeQuery();
            } else {
                ps = c.prepareStatement(query);
                return ps.executeQuery();
            }
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return null;
        }
    }


}
