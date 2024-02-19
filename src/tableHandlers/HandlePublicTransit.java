//package tableHandlers;
//
//import controller.Util;
//import queryTemplates.Comparison;
//import queryTemplates.UpdateTemplate;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class HandlePublicTransit {
//    public static final int LineNameLength = 30;
//    public static final int PNameLength = 30;
//    public static final int TransitTypeLength = 10;
//    public static final int DepartingLength = 70;
//    public static final int DestinationLength = 70;
//    public static final String[] StringAttributes = new String[]{"LineName", "PName", "TransitType", "Departing", "Destination"};
//    public static final int[] StringAttLen = new int[]{LineNameLength, PNameLength, TransitTypeLength, DepartingLength, DestinationLength};
//    private static final String SQLERROR = "SQLException: ";
//    private Connection c;
//    private PreparedStatement ps;
//    private Util util = new Util();
//
//    public HandlePublicTransit(Connection c) {
//        this.c = c;
//    }
//
//    public void insertPublicTransit(String LineName, String PName, String TransitType, String Departing, String Destination) {
//        try {
//            String query = "INSERT INTO TransitType (LineName, PName, TransitType, Departing, Destination) VALUES (?,?,?,?,?)";
//            ps = c.prepareStatement(query);
//            ps.setString(1, LineName);
//            ps.setString(2, PName);
//            ps.setString(3, TransitType);
//            ps.setString(4, Departing);
//            ps.setString(5, Destination);
//            ps.executeUpdate();
//            c.commit();
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(SQLERROR + e.getMessage());
//        }
//    }
//
//    public void deletePublicTransit(ArrayList<Comparison> compares) {
//        try {
//            if (compares.isEmpty()) {
//                throw new SQLException("Warning: unsafe operation");
//            }
//            String query = "DELETE FROM PublicTransit WHERE ";
//            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "PublicTransit.", StringAttributes));
//            util.setWhereValuesInternal(ps, compares, 1, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
//            ps.executeUpdate();
//            c.commit();
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(SQLERROR + e.getMessage());
//        }
//    }
//
//    public void updatePublicTransit(ArrayList<UpdateTemplate> attributeUpdates, ArrayList<Comparison> compares) {
//        try {
//            if (compares.isEmpty()) {
//                throw new SQLException("Warning: unsafe operation");
//            }
//            if (!attributeUpdates.isEmpty()) {
//                String query = "UPDATE PublicTransit Set ";
//                for (int i = 0; i < attributeUpdates.size(); i++) {
//                    UpdateTemplate currUT = attributeUpdates.get(i);
//                    query += currUT.getAttribute();
//                    query += " = ?";
//                    if (i < attributeUpdates.size() - 1) {
//                        query += ", ";
//                    }
//                }
//                query += " WHERE ";
//                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "PublicTransit.", StringAttributes));
//                int i;
//                for (i = 1; i <= attributeUpdates.size(); i++) {
//                    UpdateTemplate currUT = attributeUpdates.get(i - 1);
//
//                    int attIdx = Arrays.asList(StringAttributes).indexOf(currUT.getAttribute());
//                    if (attIdx == -1) {
//                        throw new SQLException("invalid attribute");
//                    }
//                    int attLen = StringAttLen[attIdx];
//                    ps.setString(i, util.PadSpace(currUT.getValue(), attLen));
//
//                }
//                util.setWhereValuesInternal(ps, compares, i, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
//                ps.executeUpdate();
//                c.commit();
//                ps.close();
//            }
//        } catch (SQLException e) {
//            System.out.println(SQLERROR + e.getMessage());
//        }
//
//
//    }
//    public ArrayList<PublicTransitOld> viewPublicTransit() throws SQLException {
//        ArrayList<PublicTransitOld> results = new ArrayList<PublicTransitOld>();
//        String query = "SELECT * FROM PublicTransit";
//        ps = c.prepareStatement(query);
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            PublicTransitOld curr = new PublicTransitOld(rs.getString("LineName"),
//                    rs.getString("PName"),
//                    rs.getString("TransitType"),
//                    rs.getString("Departing"),
//                    rs.getString("Destination"));
//            results.add(curr);
//        }
//        ps.close();
//        return results;
//    }
//
//    public ArrayList<PublicTransitOld> selectProvinces(ArrayList<Comparison> compares) {
//        try {
//            if (compares.size() == 0) {
//                return viewPublicTransit();
//            }
//            ArrayList<PublicTransitOld> results = new ArrayList<PublicTransitOld>();
//            String query = "SELECT * FROM PublicTransit WHERE ";
//            ps = c.prepareStatement(util.prepareWhereClause(query, compares, "PublicTransit.", StringAttributes));
//            util.setWhereValuesInternal(ps, compares, 1, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                PublicTransitOld curr = new PublicTransitOld(rs.getString("LineName"),
//                        rs.getString("PName"),
//                        rs.getString("TransitType"),
//                        rs.getString("Departing"),
//                        rs.getString("Destination"));
//                results.add(curr);
//            }
//            ps.close();
//            return results;
//        } catch (SQLException e) {
//            System.out.println(SQLERROR + e.getMessage());
//            return new ArrayList<PublicTransitOld>();
//        } catch (NumberFormatException e) {
//            System.out.println("NumberFormatException: " + e.getMessage());
//            return new ArrayList<PublicTransitOld>();
//        }
//    }
//
//    public ResultSet projectPublicTransit(ArrayList<String> attributes, ArrayList<Comparison> compares) {
//        try {
//            String query = util.projectHelperPrepareSelectClause(attributes, "PublicTransit.", new String[0], new String[0], new String[0], StringAttributes);
//            query += " FROM PublicTransit";
//            if (compares.size() > 0) {
//                query += " WHERE ";
//                ps = c.prepareStatement(util.prepareWhereClause(query, compares, "PublicTransit.", StringAttributes));
//                util.setWhereValuesInternal(ps, compares, 1, new String[0], new String[0], new String[0], StringAttributes, StringAttLen);
//                return ps.executeQuery();
//            } else {
//                ps = c.prepareStatement(query);
//                return ps.executeQuery();
//            }
//        } catch (SQLException e) {
//            System.out.println(SQLERROR + e.getMessage());
//            return null;
//        } catch (NumberFormatException e) {
//            System.out.println("NumberFormatException: " + e.getMessage());
//            return null;
//        }
//    }
//}


