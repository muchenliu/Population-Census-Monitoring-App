package tableHandlers;

import controller.Util;
import tables.Occupant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandleOccupant {

    private static final String SQLERROR = "SQLException: ";

    public static final int CITIZENSHIP_LENGTH = HandleResidesIndividual.CITIZENSHIP_LENGTH;
    public static final int PASSPORTNUM_LENGTH = HandleResidesIndividual.PASSPORTNUM_LENGTH;


    public static final String[] PrimaryKeyAttributes = new String[]{"Citizenship", "PassportNum"};

    public static final String[] StringAttributes = new String[]{"Citizenship", "PassportNum"};

    public static final int[] StringAttLen = new int[]{CITIZENSHIP_LENGTH, PASSPORTNUM_LENGTH};

    private Connection c;

    private PreparedStatement ps;

    private Util util = new Util();

    public HandleOccupant(Connection c) { this.c = c; }

    public void insertOccupant(String citizenship, String passportNum) {
        try {
            String query = "INSERT INTO Occupant (Citizenship, PassportNum) VALUES (?,?)";
            ps = c.prepareStatement(query);
            ps.setString(1, citizenship);
            ps.setString(2, passportNum);
            ps.executeUpdate();
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }

    public void deleteOccupant (String citizenship, String passportNum) {
        try {
            String query = "DELETE FROM Occupant WHERE Citizenship = ? AND PassportNum = ?";
            ps = c.prepareStatement(query);
            ps.setString(1, util.PadSpace(citizenship, CITIZENSHIP_LENGTH));
            ps.setString(2, util.PadSpace(passportNum, PASSPORTNUM_LENGTH));
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("Occupant " + citizenship + " " + passportNum  + " does not exist!");
            }
            c.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(SQLERROR + e.getMessage());
        }
    }


    public ArrayList<Occupant> viewOccupant() throws SQLException {
        ArrayList<Occupant> results = new ArrayList<Occupant>();
        String query = "SELECT * FROM Occupant";
        ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Occupant curr = new Occupant(rs.getString("Citizenship"),
                    rs.getString("PassportNum"));
            results.add(curr);
        }
        ps.close();
        return results;
    }
}
