package tables;

public class Renter {
    private String Citizenship;
    private String PassportNum;
    private String LeaseTerm;

    public Renter(String citizenship, String passportNum, String leaseTerm) {
        Citizenship = citizenship;
        PassportNum = passportNum;
        LeaseTerm = leaseTerm;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public String getPassportNum() {
        return PassportNum;
    }

    public String getLeaseTerm() {
        return LeaseTerm;
    }
}
