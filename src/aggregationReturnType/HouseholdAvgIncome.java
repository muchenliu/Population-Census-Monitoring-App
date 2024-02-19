package aggregationReturnType;

public class HouseholdAvgIncome {
    private String StreetAddress;
    private String PostalCode;
    private String AnnualIncome;

    public HouseholdAvgIncome(String streetAddress, String postalCode, String annualIncome) {
        StreetAddress = streetAddress;
        PostalCode = postalCode;
        AnnualIncome = annualIncome;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getAnnualIncome() {
        return AnnualIncome;
    }
}
