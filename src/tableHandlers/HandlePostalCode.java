package tableHandlers;

public class HandlePostalCode {
    public static final int PostalCodeLength = 7;
    public static final int CNameLength = 70;  // temporary placed here
    public static final int PNameLength = 30;  // temporary placed here

    public static final String[] StringAttributes = new String[]{"PostalCode", "CName", "PName"};
    public static final int[] StringAttLen = new int[]{PostalCodeLength, CNameLength, PNameLength};
}
