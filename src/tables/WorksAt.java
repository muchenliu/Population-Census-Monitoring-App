package tables;

public class WorksAt {
    private String Citizenship;
    private String PassportNum;
    private String PostalCode;
    private String BranchAddress;
    private String EmploymentType;

    public WorksAt(String citizenship, String passportNum, String postalCode, String branchAddress, String employmentType) {
        Citizenship = citizenship;
        PassportNum = passportNum;
        PostalCode = postalCode;
        BranchAddress = branchAddress;
        EmploymentType = employmentType;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public String getPassportNum() {
        return PassportNum;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getBranchAddress() {
        return BranchAddress;
    }

    public String getEmploymentType() {
        return EmploymentType;
    }
}
