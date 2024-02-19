package aggregationReturnType;

public class EmployeeAverageAge {
    private String PostalCode;
    private String BranchAddress;
    private String AverageAge;

    public EmployeeAverageAge(String postalCode, String branchAddress, String averageAge) {
        PostalCode = postalCode;
        BranchAddress = branchAddress;
        AverageAge = averageAge;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getBranchAddress() {
        return BranchAddress;
    }

    public String getAverageAge() {
        return AverageAge;
    }
}
