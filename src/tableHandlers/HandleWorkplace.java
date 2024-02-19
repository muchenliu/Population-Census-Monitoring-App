package tableHandlers;

import aggregationReturnType.EmployeeAverageAge;
import controller.Util;
import queryTemplates.Comparison;
import queryTemplates.UpdateTemplate;
import tables.Workplace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static oracle.jdbc.OracleTypes.CHAR;
import static oracle.jdbc.OracleTypes.NUMBER;

public class HandleWorkplace {
    private static final String SQLERROR = "SQLException: ";

    public static final int POSTALCODE_LENGTH = HandlePostalCode.PostalCodeLength;

    public static final int BRANCHADDRESS_LENGTH = 40;

    public static final int COMPANYNAME_LENGTH = 70;


    public static final String[] IntAttributes = new String[]{"AnnualProfit", "NumEmployee"};
    public static final String[] PrimaryKeyAttributes = new String[]{"PostalCode", "BranchAddress"};
    public static final String[] StringAttributes = new String[]{"PostalCode", "BranchAddress", "CompanyName"};

    public static final int[] StringAttLen = new int[]{POSTALCODE_LENGTH, BRANCHADDRESS_LENGTH, COMPANYNAME_LENGTH};

    private Connection c;

    private PreparedStatement ps;

    private Util util = new Util();

    public HandleWorkplace(Connection c) { this.c = c; }

    public void insertWorkplace(String postalCode, String branchAddress, int annualProfit, String companyName, int numEmployee) {
        try {
            String query = "INSERT INTO Workplace (PostalCode, BranchAddress, AnnualProfit, CompanyName, NumEmployee) " +
                    "VALUES(?,?,?,?,?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, postalCode);
            ps.setString(2, branchAddress);
            if (annualProfit > 0) {
                ps.setInt(3, annualProfit);
            } else {
                ps.setNull(3, NUMBER);
            }
            if (companyName.length() > 0) {
                ps.setString(4, companyName);
            } else {
                ps.setNull(4, CHAR);
            }
            if (numEmployee > 0) {
                ps.setInt(5, numEmployee);
            } else {
                ps.setNull(5, CHAR);
            }
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteWorkplace (ArrayList<Comparison> compares) {
        try {
            if (compares.isEmpty()) {
                throw new SQLException("Warning: unsafe operation");
            }
            String query = "DELETE FROM Workplace WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Workplace.", PrimaryKeyAttributes));
            util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void updateWorkplace (ArrayList<UpdateTemplate> attributeUpdates, ArrayList<Comparison> compares) {
        try {
            if (compares.isEmpty()) {
                throw new SQLException("Warning: unsafe operation");
            }
            if (!attributeUpdates.isEmpty()) {
                String query = "UPDATE Workplace SET ";
                for (int i = 0; i < attributeUpdates.size(); i++) {
                    UpdateTemplate currUT = attributeUpdates.get(i);
                    query += currUT.getAttribute();
                    query += " = ?";
                    if (i < attributeUpdates.size() - 1) {
                        query += ", ";
                    }
                }
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Workplace.", PrimaryKeyAttributes));
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

    public ArrayList<Workplace> viewWorkplace() throws SQLException {
        ArrayList<Workplace> results = new ArrayList<Workplace>();
        String query = "SELECT * FROM Workplace";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Workplace curr = new Workplace(rs.getString("PostalCode"), rs.getString("BranchAddress"),
                    rs.getInt("AnnualProfit"), rs.getString("CompanyName"), rs.getInt("NumEmployee"));
            results.add(curr);
        }
        ps.close();;
        return results;
    }

    public ArrayList<Workplace> selectWorkplace(ArrayList<Comparison> compares) {
        try {
            if (compares.size() == 0) {
                return viewWorkplace();
            }
            ArrayList<Workplace> results = new ArrayList<Workplace>();
            String query = "SELECT * FROM Workplace WHERE ";
            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Workplace.", PrimaryKeyAttributes));
            util.setWhereValuesInternal(ps, compares, 1, new String[0], IntAttributes, new String[0], StringAttributes, StringAttLen);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Workplace curr = new Workplace(rs.getString("PostalCode"), rs.getString("BranchAddress"),
                        rs.getInt("AnnualProfit"), rs.getString("CompanyName"), rs.getInt("NumEmployee"));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<Workplace>();
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            return new ArrayList<Workplace>();
        }
    }

    public ResultSet projectWorkplace(ArrayList<String> attributes, ArrayList<Comparison> compares) {
        try {
            String query = util.projectHelperPrepareSelectClause(attributes, "Workplace.", new String[0], IntAttributes, new String[0], StringAttributes);
            query += " FROM Workplace";
            if (compares.size() > 0) {
                query += " WHERE ";
                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "Workplace.", PrimaryKeyAttributes));
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

    //Nested Aggregation of GROUP BY: find workplace which has the min average employee age
    public ArrayList<EmployeeAverageAge> minEmployeeAgeWP() {
        try {
            String dropQuery1 = "DROP VIEW R2";
            ps = c.prepareStatement(dropQuery1);
            ps.executeQuery();
            String dropQuery2 = "DROP VIEW Temp";
            ps = c.prepareStatement(dropQuery2);
            ps.executeQuery();
            c.commit();
        } catch (Exception e) {
            // to nothing
        }
        try {
            ArrayList<EmployeeAverageAge> results = new ArrayList<EmployeeAverageAge>();
            String query = "CREATE VIEW R2(Citizenship, PassportNum, Age) as SELECT Citizenship, PassportNum, Age " +
                    "FROM ResidesIndividual";
            ps = c.prepareStatement(query);
            ps.executeQuery();
            String query2 = "CREATE VIEW Temp(PostalCode, BranchAddress, AverageAge) as " +
                    "SELECT PostalCode, BranchAddress, AVG(Age) as AverageAge " +
                    "FROM WorksAt, R2 " +
                    "WHERE WorksAt.PassportNum = R2.PassportNum AND WorksAt.Citizenship = R2.Citizenship " +
                    "GROUP BY PostalCode, BranchAddress";
            ps = c.prepareStatement(query2);
            ps.executeQuery();
            String query3 = "SELECT PostalCode, BranchAddress, AverageAge " +
                    "FROM Temp " +
                    "WHERE AverageAge = (SELECT MIN (AverageAge) FROM Temp)";
            ps = c.prepareStatement(query3);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EmployeeAverageAge curr = new EmployeeAverageAge(rs.getString("PostalCode"),
                        rs.getString("BranchAddress"),
                        String.valueOf(rs.getInt("AverageAge")));
                results.add(curr);
            }
            ps.close();
            return results;
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
            return new ArrayList<EmployeeAverageAge>();
        }
    }
}
