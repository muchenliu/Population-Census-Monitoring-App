package tableHandlers;

import controller.Util;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tables.Property;
import tables.PropertyType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.NUMBER;

public class HandleProperty {
    private static final String SQLERROR = "SQLException: ";
    public static final int PostalCodeLength = HandlePostalCode.PostalCodeLength;
    public static final int StreetAddressLength = 40;

    public static final String[] DoubleAttributes = new String[]{"PropSize"};
    public static final String[] IntAttributes = new String[]{"NumBedroom", "NumBathroom"};
    public static final String[] StringAttributes = new String[]{"PostalCode", "StreetAddress"};
    public static final int[] StringAttLen = new int[]{PostalCodeLength, StreetAddressLength};
    private Connection c;
    private PreparedStatement ps;
    private Util util = new Util();
    private HandleHouse handleHouse;
    private HandleTownHouse handleTownHouse;
    private HandleApartment handleApartment;
    private HandleHasValue handleHasValue;
    private HandleAmenities handleAmenities;

    public HandleProperty(Connection c) {
        this.c = c;
        handleHouse = new HandleHouse(c);
        handleTownHouse = new HandleTownHouse(c);
        handleApartment = new HandleApartment(c);
        handleHasValue = new HandleHasValue(c);
        handleAmenities = new HandleAmenities(c);
    }

    public void closePS() {
        try {
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void insertProperty(String PostalCode, String StreetAddress, double PropSize, int NumBedroom, int NumBathroom, String unit, String floor, PropertyType pt) throws SQLException {
        String query = "INSERT INTO Property (PostalCode, StreetAddress, PropSize, NumBedroom, NumBathroom) VALUES (?,?,?,?,?)";
        ps = c.prepareStatement(query);
        ps.setString(1, PostalCode);
        ps.setString(2, StreetAddress);
        if (PropSize > 0) {
            ps.setDouble(3, PropSize);
        } else {
            ps.setNull(3, NUMBER);
        }
        if (NumBedroom > 0) {
            ps.setInt(4, NumBedroom);
        } else {
            ps.setNull(4, NUMBER);
        }
        if (NumBathroom > 0) {
            ps.setInt(5, NumBathroom);
        } else {
            ps.setNull(5, NUMBER);
        }
        ps.executeUpdate();
        c.commit();
        ps.close();
        if (pt == PropertyType.HOUSE) {
            handleHouse.insertHouse(PostalCode, StreetAddress);
        } else if (pt == PropertyType.TOWNHOUSE) {
            handleTownHouse.insertTownHouse(PostalCode, StreetAddress, unit);
        } else {
            handleApartment.insertApartment(PostalCode, StreetAddress, unit, floor);
        }
    }

    public void deleteProperty (ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        if (compares.isEmpty()) {
            throw new SQLException("Warning: unsafe operation");
        }
        String query = "DELETE FROM Property WHERE ";
        ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
        util.setWhereValuesInternal(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen);
        ps.executeUpdate();
        c.commit();
        ps.close();
    }

    public void updateProperty (ArrayList<UpdateTemplate> primaryUpdates, ArrayList<UpdateTemplate> attributeUpdates, ArrayList<UpdateTemplate> subUpdates, ArrayList<Comparison> comparisons) throws NumberFormatException, SQLException {
        if (primaryUpdates.size() + attributeUpdates.size() + subUpdates.size() == 0) {
            return;
        }
        if (comparisons.isEmpty()) {
            throw new SQLException("Warning: unsafe operation");
        }
        ArrayList<Property> properties = selectProperties(comparisons);
        for(Property curr: properties) {
            String newPC = curr.getPostalCode();
            String newSA = curr.getStreetAddress();
            boolean changePC = false;
            boolean changeSA = false;
            for (UpdateTemplate u: primaryUpdates) {
                if (u.getAttribute().equals("PostalCode")) {
                    newPC = u.getValue();
                    changePC = true;
                } else if (u.getAttribute().equals("StreetAddress")) {
                    newSA = u.getValue();
                    changeSA = true;
                }
            }
            if (changePC || changeSA) {
                updatePrimaryKey(changePC, changeSA, newPC, newSA, curr, primaryUpdates);
            }
            if (!attributeUpdates.isEmpty()) {
                String query = "UPDATE Property Set ";
                for (int i = 0; i < attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i);
                    query += currUT.getAttribute();
                    query += " = ?";
                    if (i < attributeUpdates.size() - 1) {
                        query += ", ";
                    }
                }
                query += " WHERE PostalCode = ? AND StreetAddress = ?";
                ps = c.prepareStatement(query);
                for (int i = 1; i <= attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i - 1);
                    if (Arrays.asList(DoubleAttributes).contains(currUT.getAttribute())) {
                        ps.setDouble(i, Double.parseDouble(currUT.getValue()));
                    } else if (Arrays.asList(IntAttributes).contains(currUT.getAttribute())) {
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
                ps.setString(attributeUpdates.size() + 1, util.PadSpace(newPC, PostalCodeLength));
                ps.setString(attributeUpdates.size() + 2, util.PadSpace(newSA, StreetAddressLength));
                ps.executeUpdate();
            }
            ps.close();
            handleHouse.updateHouse(subUpdates, newPC, newSA);
            handleTownHouse.updateTownHouse(subUpdates, newPC, newSA);
            handleApartment.updateApartment(subUpdates, newPC, newSA);
        }
        c.commit();
    }

    public ArrayList<Property> viewProperties () throws SQLException {
        ArrayList<Property> results = new ArrayList<Property>();
        String query = "SELECT * FROM Property";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Property curr = new Property(rs.getString("PostalCode"),
                    rs.getString("StreetAddress"),
                    rs.getDouble("PropSize"),
                    rs.getInt("NumBedroom"),
                    rs.getInt("NumBathroom"));
            results.add(curr);
        }
        ps.close();
        return results;
    }

    public ArrayList<Property> selectProperties(ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        if (compares.size() == 0) {
            return viewProperties();
        }
        ArrayList<Property> results = new ArrayList<Property>();
        String query = "SELECT * FROM Property WHERE ";
        ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
        util.setWhereValuesInternal(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Property curr = new Property(rs.getString("PostalCode"),
                    rs.getString("StreetAddress"),
                    rs.getDouble("PropSize"),
                    rs.getInt("NumBedroom"),
                    rs.getInt("NumBathroom"));
            results.add(curr);
        }
        ps.close();
        return results;
    }

    public ResultSet projectProperties(ArrayList<String> attributes, ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property";
        if (compares.size() > 0) {
            query += " WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
            util.setWhereValuesInternal(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen);
            return ps.executeQuery();
        } else {
            ps = c.prepareStatement(query);
            return ps.executeQuery();
        }
    }

    public ResultSet viewPropertyValue (ArrayList<String> attributes, ArrayList<Comparison> compares) {
        try {
            String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
            query += " FROM Property, HasValue WHERE Property.PostalCode = HasValue.PostalCode AND Property.StreetAddress = HasValue.StreetAddress";
            if (compares.size() > 0) {
                query += " AND ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
                util.setWhereValues(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen, HandleHasValue.DoubleAttributes, HandleHasValue.IntAttributes, HandleHasValue.DateAttributes, HandleHasValue.StringAttributes, HandleHasValue.StringAttLen);
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

    public ResultSet viewAmenities (ArrayList<String> attributes, ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property, HasAmenities WHERE Property.PostalCode = HasAmenities.PostalCode AND Property.StreetAddress = HasAmenities.StreetAddress";
        if (compares.size() > 0) {
            query += " AND ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
            util.setWhereValues(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen, new String[0], new String[0], new String[0], HandleAmenities.StringAttributes, HandleAmenities.StringAttLen);
            return ps.executeQuery();
        } else {
            ps = c.prepareStatement(query);
            return ps.executeQuery();
        }
    }

    public ResultSet viewCity (ArrayList<String> attributes, ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property, PostalCode WHERE Property.PostalCode = PostalCode.PostalCode";
        if (compares.size() > 0) {
            query += " AND ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
            util.setWhereValues(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen, new String[0], new String[0], new String[0], HandlePostalCode.StringAttributes, HandlePostalCode.StringAttLen);
            return ps.executeQuery();
        } else {
            ps = c.prepareStatement(query);
            return ps.executeQuery();
        }
    }

    public ResultSet findIndividualsResideProperty(String PostalCode, String StreetAddress, ArrayList<String> attributes) throws SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "r2.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM ResidesIndividual r2 WHERE NOT EXISTS (SELECT p1.PostalCode, p1.StreetAddress FROM Property p1 WHERE p1.PostalCode = ? AND p1.StreetAddress = ? MINUS (SELECT r1.PostalCode, r1.StreetAddress FROM ResidesIndividual r1 WHERE r1.Citizenship = r2.Citizenship AND r1.PassportNum = r2.PassportNum))";
        ps = c.prepareStatement(query);
        ps.setString(1, util.PadSpace(PostalCode, PostalCodeLength));
        ps.setString(2, util.PadSpace(StreetAddress, StreetAddressLength));
        return ps.executeQuery();
    }

    public ResultSet viewHouse (ArrayList<String> attributes, ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property, House WHERE Property.PostalCode = House.PostalCode AND Property.StreetAddress = House.StreetAddress";
        if (compares.size() > 0) {
            query += " AND ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
            util.setWhereValues(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen, new String[0], new String[0], new String[0], HandleHouse.StringAttributes, HandleHouse.StringAttLen);
            return ps.executeQuery();
        } else {
            ps = c.prepareStatement(query);
            return ps.executeQuery();
        }
    }

    public ResultSet viewTownhouse (ArrayList<String> attributes, ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property, Townhouse WHERE Property.PostalCode = Townhouse.PostalCode AND Property.StreetAddress = Townhouse.StreetAddress";
        if (compares.size() > 0) {
            query += " AND ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
            util.setWhereValues(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen, new String[0], new String[0], new String[0], HandleTownHouse.StringAttributes, HandleTownHouse.StringAttLen);
            return ps.executeQuery();
        } else {
            ps = c.prepareStatement(query);
            return ps.executeQuery();
        }
    }

    public ResultSet viewApartment (ArrayList<String> attributes, ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "Property.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property, Apartment WHERE Property.PostalCode = Apartment.PostalCode AND Property.StreetAddress = Apartment.StreetAddress";
        if (compares.size() > 0) {
            query += " AND ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Property.", StringAttributes));
            util.setWhereValues(ps, compares, 1, DoubleAttributes, IntAttributes, new String[0], StringAttributes, StringAttLen, new String[0], HandleApartment.IntAttributes, new String[0], HandleApartment.StringAttributes, HandleApartment.StringAttLen);
            return ps.executeQuery();
        } else {
            ps = c.prepareStatement(query);
            return ps.executeQuery();
        }
    }

    public ResultSet findPropertiesOwnByIndividuals(ArrayList<String> Citizenship, ArrayList<String> PassportNumber, ArrayList<String> attributes) throws SQLException {
        String query = util.projectHelperPrepareSelectClause(attributes, "p.", DoubleAttributes, IntAttributes, new String[0], StringAttributes);
        query += " FROM Property p WHERE NOT EXISTS (SELECT o.Citizenship, o.PassportNum FROM Owner o WHERE (";
        for (int i = 0; i < Citizenship.size(); i++) {
            query += "(o.Citizenship = ? AND o.PassportNum = ?)";
            if (i < Citizenship.size() - 1) {
                query += "OR";
            }
        }
        query += ") MINUS (SELECT op.Citizenship, op.PassportNum FROM OwnsProperty op WHERE op.PostalCode = p.PostalCode AND op.StreetAddress = p.StreetAddress))";
        System.out.println(query);
        ps = c.prepareStatement(query);
        for (int i = 0; i < Citizenship.size(); i++) {
            ps.setString(i * 2 + 1, util.PadSpace(Citizenship.get(i), HandleResidesIndividual.CITIZENSHIP_LENGTH));
            ps.setString(i * 2 + 2, util.PadSpace(PassportNumber.get(i), HandleResidesIndividual.PASSPORTNUM_LENGTH));
        }
        return ps.executeQuery();
    }



    /*
    --------------------------------------------------------------------------------------------------------------------
    Helper Functions
    --------------------------------------------------------------------------------------------------------------------
     */
    private void updatePrimaryKey (boolean changePC, boolean changeSA, String newPC, String newSA, Property curr, ArrayList<UpdateTemplate> primaryUpdates) throws SQLException {
        String tempInsert = "INSERT INTO Property (PostalCode, StreetAddress, PropSize, NumBedroom, NumBathroom) VALUES (?,?,?,?,?)";
        ps = c.prepareStatement(tempInsert);
        if (changePC) {
            ps.setString(1, newPC);
        } else {
            ps.setString(1, curr.getPostalCode());
        }
        if (changeSA) {
            ps.setString(2, newSA);
        } else {
            ps.setString(2, curr.getPostalCode());
        }
        if (curr.getPropSize() > 0) {
            ps.setDouble(3, curr.getPropSize());
        } else {
            ps.setNull(3, NUMBER);
        }
        if (curr.getNumBedroom() > 0) {
            ps.setInt(4, curr.getNumBedroom());
        } else {
            ps.setNull(4, NUMBER);
        }
        if (curr.getNumBathroom() > 0) {
            ps.setInt(5, curr.getNumBathroom());
        } else {
            ps.setNull(5, NUMBER);
        }
        ps.executeUpdate();
        handleHouse.updateHouse(primaryUpdates, curr.getPostalCode(), curr.getStreetAddress());
        handleTownHouse.updateTownHouse(primaryUpdates, curr.getPostalCode(), curr.getStreetAddress());
        handleApartment.updateApartment(primaryUpdates, curr.getPostalCode(), curr.getStreetAddress());
        handleHasValue.updateHasValue(primaryUpdates, curr.getPostalCode(), curr.getStreetAddress());
        handleAmenities.updateAmenity(primaryUpdates, curr.getPostalCode(), curr.getStreetAddress());

        String deleteQuery = "DELETE FROM Property WHERE Postalcode = ? AND StreetAddress = ?";
        ps = c.prepareStatement(deleteQuery);
        ps.setString(1, util.PadSpace(curr.getPostalCode(), PostalCodeLength));
        ps.setString(2, util.PadSpace(curr.getStreetAddress(), StreetAddressLength));
        ps.executeUpdate();
        ps.close();
    }
}
