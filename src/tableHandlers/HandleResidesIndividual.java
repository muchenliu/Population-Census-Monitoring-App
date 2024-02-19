package tableHandlers;

import aggregationReturnType.HouseholdAvgIncome;
import aggregationReturnType.MaxOwnedValue;
import controller.Util;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tables.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.CHAR;
import static oracle.jdbc.OracleTypes.NUMBER;

public class HandleResidesIndividual {
    private static final String SQLERROR = "SQLException: ";
    public static final int CITIZENSHIP_LENGTH = 40;
    public static final int PASSPORTNUM_LENGTH = 20;
    public static final int PERSONNAME_LENGTH  = 40;
    public static final int PostalCodeLength = HandlePostalCode.PostalCodeLength;
    public static final int StreetAddressLength = HandleProperty.StreetAddressLength;

    public static final String[] IntAttributes = new String[]{"Age", "AnnualIncome"};
    public static final String[] PrimaryKeyAttributes = new String[]{"Citizenship", "PassportNum"};
    public static final String[] StringAttributes = new String[]{"Citizenship", "PassportNum", "PersonName","PostalCode", "StreetAddress"};
    public static final int[] StringAttLen = new int[]{CITIZENSHIP_LENGTH, PASSPORTNUM_LENGTH, PERSONNAME_LENGTH, PostalCodeLength, StreetAddressLength};

    private Connection c;
    private PreparedStatement ps;
    private Util util = new Util();

    private HandleOwner handleOwner;
    private HandleRenter handleRenter;
    private HandleOccupant handleOccupant;

    public HandleResidesIndividual(Connection c) {
        this.c = c;
        handleOwner = new HandleOwner(c);
        handleRenter = new HandleRenter(c);
        handleOccupant = new HandleOccupant(c);
    }

    public void insertResidesIndividual(String citizenship, String passportNum, String personName, int age, int annualIncome, String postalCode, String streetAddress,
                                        String leaseTerm, String ownedStreetAddress, String ownedPostalCode, String dateOfPurchase, ResidesIndividualType type) {
        try {
            String query = "INSERT INTO ResidesIndividual (Citizenship, PassportNum, PersonName, Age, AnnualIncome, PostalCode, StreetAddress) VALUES (?,?,?,?,?,?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, citizenship);
            ps.setString(2, passportNum);
            if (personName.length() > 0) {
                ps.setString(3, personName);
            } else {
                ps.setNull(3, CHAR);
            }
            if (age > 0) {
                ps.setInt(4, age);
            } else {
                ps.setNull(4, NUMBER);
            }
            if (annualIncome > 0) {
                ps.setInt(5, annualIncome);
            } else {
                ps.setNull(5, NUMBER);
            }
            if (postalCode.length() > 0) {
                ps.setString(6, postalCode);
            } else {
                ps.setNull(6, CHAR);
            }
            if (streetAddress.length() > 0) {
                ps.setString(7, streetAddress);
            } else {
                ps.setNull(7, CHAR);
            }
            ps.executeUpdate();
            c.commit();
            ps.close();
            if (type == ResidesIndividualType.OWNER) {
                handleOwner.insertOwner(citizenship, passportNum, ownedPostalCode, ownedStreetAddress, dateOfPurchase);
            } else if (type == ResidesIndividualType.RENTER) {
                handleRenter.insertRenter(citizenship, passportNum, leaseTerm);
            } else if (type == ResidesIndividualType.OCCUPANT){
                handleOccupant.insertOccupant(citizenship, passportNum);
            }
        } catch(SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteResidesIndividual (ArrayList<Comparison> compares) throws NumberFormatException, SQLException {
        if (compares.isEmpty()) {
            throw new SQLException("Warning: unsafe operation");
        }
        String query = "DELETE FROM ResidesIndividual WHERE ";
        ps = c.prepareStatement(util.prepareWhereClause(query, compares, "ResidesIndividual.", PrimaryKeyAttributes));
        util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
        ps.executeUpdate();
        c.commit();
        ps.close();
    }

    public void updateResidesIndividual (ArrayList<UpdateTemplate> attributeUpdates, ArrayList<Comparison> comparisons) {
        try {
            if (comparisons.isEmpty()) {
                throw new SQLException("Warning: unsafe operation");
            }
            if (!attributeUpdates.isEmpty()) {
                String query = "UPDATE ResidesIndividual SET ";
                for (int i = 0; i < attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i);
                    query += currUT.getAttribute();
                    query += " = ?";
                    if (i < attributeUpdates.size() - 1) {
                        query += ", ";
                    }
                }
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, comparisons, "ResidesIndividual.", PrimaryKeyAttributes));
                int idx;
                for (idx = 1; idx < attributeUpdates.size(); idx++) {
                    UpdateTemplate currUT = attributeUpdates.get(idx - 1);
                    if (Arrays.asList(IntAttributes).contains(currUT.getAttribute())) {
                        ps.setInt(idx, Integer.parseInt(currUT.getValue()));
                    } else {
                        int attIdx = Arrays.asList(StringAttributes).indexOf(currUT.getAttribute());
                        if (attIdx == -1) {
                            throw new SQLException("invalid attribute");
                        }
                        int attLen = StringAttLen[attIdx];
                        ps.setString(idx, util.PadSpace(currUT.getValue(), attLen));
                    }
                }
                util.setWhereValuesInternal(ps, comparisons, idx, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
                ps.executeUpdate();
                c.commit();
                ps.close();
            }
        } catch(SQLException e){
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public ArrayList<ResidesIndividual> viewResidesIndividual () throws SQLException {
        ArrayList<ResidesIndividual> results = new ArrayList<ResidesIndividual>();
        String query = "SELECT * FROM ResidesIndividual";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ResidesIndividual curr = new ResidesIndividual(rs.getString("Citizenship"),
                    rs.getString("PassportNum"),
                    rs.getString("PersonName"),
                    rs.getInt("Age"),
                    rs.getInt("AnnualIncome"),
                    rs.getString("PostalCode"),
                    rs.getString("StreetAddress"));
            results.add(curr);
        }
        ps.close();
        return results;
    }

    //Aggregation of GROUP BY: annual average household income
    public ArrayList<HouseholdAvgIncome> averageHouseholdIncome() {
        try {
            ArrayList<HouseholdAvgIncome> results = new ArrayList<HouseholdAvgIncome>();
            String query = "SELECT StreetAddress, PostalCode, AVG(AnnualIncome) " +
                    "FROM ResidesIndividual " +
                    "GROUP BY StreetAddress, PostalCode";
            ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HouseholdAvgIncome curr = new HouseholdAvgIncome(rs.getString("StreetAddress"),
                        rs.getString("PostalCode"),
                        String.valueOf(rs.getInt("AVG(AnnualIncome)")));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<HouseholdAvgIncome>();
        }
    }

    //Aggregation of HAVING: Find the highest value property for each owner who owns at least 2 properties
    public ArrayList<MaxOwnedValue> ownerMaxValue() {
        try {
            ArrayList<MaxOwnedValue> results = new ArrayList<MaxOwnedValue>();
            String query = "SELECT Citizenship, PassportNum, MAX(SalvageValue) " +
                    "FROM OwnsProperty, HasValue " +
                    "WHERE OwnsProperty.PostalCode = HasValue.PostalCode AND OwnsProperty.StreetAddress = HasValue.StreetAddress " +
                    "GROUP BY Citizenship, PassportNum " +
                    "HAVING COUNT(*) > 1";
            ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaxOwnedValue curr = new MaxOwnedValue(rs.getString("Citizenship"),
                        rs.getString("PassportNum"),
                        String.valueOf(rs.getDouble("MAX(SalvageValue)")));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<MaxOwnedValue>();
        }
    }

    public ArrayList<ResidesIndividual> selectResidesIndividual(ArrayList<Comparison> compares) {
        try {
            if (compares.size() == 0) {
                return viewResidesIndividual();
            }
            ArrayList<ResidesIndividual> results = new ArrayList<ResidesIndividual>();
            String query = "SELECT * FROM ResidesIndividual WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "ResidesIndividual.", PrimaryKeyAttributes));
            util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ResidesIndividual curr = new ResidesIndividual(rs.getString("Citizenship"),
                        rs.getString("PassportNum"),
                        rs.getString("PersonName"),
                        rs.getInt("Age"),
                        rs.getInt("AnnualIncome"),
                        rs.getString("PostalCode"),
                        rs.getString("StreetAddress"));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<ResidesIndividual>();
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            return new ArrayList<ResidesIndividual>();
        }
    }

    public ResultSet projectResidesIndividual(ArrayList<String> attributes, ArrayList<Comparison> compares) {
        try {
            String query = util.projectHelperPrepareSelectClause(attributes, "Property.", new String[0], IntAttributes, new String[0], StringAttributes);
            query += " FROM ResidesIndividual";
            if (compares.size() > 0) {
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "ResidesIndividual.", PrimaryKeyAttributes));
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



    public void callUpdateRenter(String citizenship, String passportNum, ArrayList<UpdateTemplate> updates) {
        handleRenter.updateRenter(updates, citizenship, passportNum);
    }

    public ArrayList<Renter> callViewRenter() throws SQLException {
        return handleRenter.viewRenter();
    }

    public void callDeleteRenter(String citizenship, String passportNum) {
        handleRenter.deleteRenter(citizenship, passportNum);
    }

    public ArrayList<OwnsProperty> callViewOwnsProperty() throws SQLException {
        return handleOwner.viewOwnsProperty();
    }

    public void callDeleteOwner(String citizenship, String passportNum) {
        handleOwner.deleteOwner(citizenship, passportNum);
    }

    public ArrayList<Occupant> callViewOccupant() throws SQLException {
        return handleOccupant.viewOccupant();
    }

    public void callDeleteOccupant(String citizenship, String passportNum) {
        handleOccupant.deleteOccupant(citizenship, passportNum);
    }

    public ArrayList<WorksAt> viewWorksAt() throws SQLException {
        ArrayList<WorksAt> results = new ArrayList<WorksAt>();
        String query = "SELECT * FROM WorksAt";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            WorksAt curr = new WorksAt(rs.getString("Citizenship"),
                    rs.getString("PassportNum"),
                    rs.getString("PostalCode"),
                    rs.getString("BranchAddress"),
                    rs.getString("EmploymentType"));
            results.add(curr);
        }
        ps.close();
        return results;
    }
}
