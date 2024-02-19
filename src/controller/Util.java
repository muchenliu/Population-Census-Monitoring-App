package controller;

import queryTemplates.Comparison;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {
    public String PadSpace (String value, int len) {
        if (value.length() >= len) {
            return value;
        } else {
            int difference = len - value.length();
            for (int i = 0; i < difference; i ++) {
                value += " ";
            }
            return value;
        }
    }

    // EFFECTS: This function is used to prepare the WHERE clause of a select or project statement.
    //          It returns the string of the query (i.e. 'ps = c.prepareStatement(query);' is called on the outside
    public String prepareWhereClause (String query, ArrayList<Comparison> compares, String tableName,
                                                  String[] primaryKeys) throws SQLException {
        for (int i = 0; i < compares.size(); i ++) {
            Comparison curr = compares.get(i);
            if (Arrays.asList(primaryKeys).contains(curr.getAttribute())) {
                query += tableName;
            }
            query += curr.getAttribute();
            query += curr.getOperator();
            query += "?";
            if (i < compares.size() - 1) {
                query += " AND ";
            }
        }
        return query;
    }

    // EFFECTS: This function is used when selection/delete/update is done on one table (i.e. no joining)
    //          String[] parameters that start with 'this' represents attributes of this table
    //                  (e.g. if HandleProperty calls this function, Property is 'this' table
    public void setWhereValuesInternal(PreparedStatement ps, ArrayList<Comparison> compares, int index,
                                       String[] thisDoubles, String[] thisInts, String[] thisDates,
                                       String[] thisStrings, int[] thisStringLen) throws SQLException, NumberFormatException {
        setWhereValues(ps, compares, index, thisDoubles, thisInts, thisDates, thisStrings, thisStringLen, new String[0], new String[0], new String[0], new String[0], new int[0]);
    }

    // EFFECTS: This function is used when selection is done after joining two tables
    //          String[] parameters that start with 'this' represents attributes of this table
    //                  (e.g. if HandleProperty calls this function, Property is 'this' table
    //          String[] parameters that start with 'other' represents attributes of the other table
    //                  (e.g. if HandleProperty joins with HasValue and calls this function, HasValue is the 'other' table
    public void setWhereValues(PreparedStatement ps, ArrayList<Comparison> compares, int index, String[] thisDoubles,
                                String[] thisInts, String[] thisDates, String[] thisStrings,
                                int[] thisStringLen, String[] otherDoubles, String[] otherInts, String[] otherDates,
                                String[] otherStrings, int[] otherStringLen) throws SQLException, NumberFormatException {
        for (int i = index; i <= compares.size(); i ++) {
            Comparison curr = compares.get(i-1);
            String currAttr = curr.getAttribute();
            if(Arrays.asList(thisDoubles).contains(currAttr) || Arrays.asList(otherDoubles).contains(currAttr)) {
                ps.setDouble(i, Double.parseDouble(curr.getValue()));
            } else if (Arrays.asList(thisInts).contains(currAttr) || Arrays.asList(otherInts).contains(currAttr)) {
                ps.setInt(i, Integer.parseInt(curr.getValue()));
            } else if (Arrays.asList(thisDates).contains(currAttr) || Arrays.asList(otherDates).contains(currAttr)) {
                ps.setDate(i, Date.valueOf(curr.getValue())); // yyyy-[m]m-[d]d
            } else {
                int attIdx1 = Arrays.asList(thisStrings).indexOf(currAttr);
                int attIdx2 = Arrays.asList(otherStrings).indexOf(currAttr);
                if (attIdx1 >= 0) {
                    int attLen = thisStringLen[attIdx1];
                    ps.setString(i, PadSpace(curr.getValue(), attLen));
                } else if (attIdx2 >= 0) {
                    int attLen = otherStringLen[attIdx2];
                    ps.setString(i, PadSpace(curr.getValue(), attLen));
                } else {
                    throw new SQLException("invalid attribute");
                }
            }
        }
    }

    // EFFECTS: This function is used to prepare SELECT clause of project
    //          String[] parameters that start with 'this' represents attributes of this table
    //                  (e.g. if HandleProperty calls this function, Property is 'this' table
    public String projectHelperPrepareSelectClause (ArrayList<String> attributes, String tableName, String[] thisDoubles,
                                                    String[] thisInts, String[] thisDates, String[] thisStrings){
        String query = "SELECT ";
        for (int i = 0; i < attributes.size(); i++) {
            String curr = attributes.get(i);
            if (Arrays.asList(thisStrings).contains(curr) || Arrays.asList(thisDoubles).contains(curr) || Arrays.asList(thisInts).contains(curr) || Arrays.asList(thisDates).contains(curr)) {
                query += tableName; // tableName = "Property." and not this "Property"
            }
            query += attributes.get(i);
            if (i < attributes.size() - 1) {
                query += ", ";
            }
        }
        return query;
    }
}
