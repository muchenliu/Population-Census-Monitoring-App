package tables;

public class OwnsProperty{

    private String Citizenship;
    private String PassportNUm;
    private String PostalCode;
    private String StreetAddress;
    private String DateOfPurchase;

    public OwnsProperty(String citizenship, String passportNUm, String postalCode, String streetAddress, String dateOfPurchase) {
        Citizenship = citizenship;
        PassportNUm = passportNUm;
        PostalCode = postalCode;
        StreetAddress = streetAddress;
        DateOfPurchase = dateOfPurchase;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public String getPassportNUm() {
        return PassportNUm;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public String getDateOfPurchase() {
        return DateOfPurchase;
    }
}
