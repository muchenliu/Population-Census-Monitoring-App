package tables;

public class Workplace {
    private String PostalCode;

    private String BranchAddress;

    private int AnnualProfit;

    private String CompanyName;

    private int NumEmployee;

    public Workplace(String postalCode, String branchAddress, int annualProfit, String companyName, int numEmployee) {
        PostalCode = postalCode;
        BranchAddress = branchAddress;
        AnnualProfit = annualProfit;
        CompanyName = companyName;
        NumEmployee = numEmployee;
    }

    public int getNumEmployee() {
        return NumEmployee;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public int getAnnualProfit() {
        return AnnualProfit;
    }

    public String getBranchAddress() { return BranchAddress; }

    public String getPostalCode() {
        return PostalCode;
    }
}
