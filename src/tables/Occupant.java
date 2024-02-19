package tables;

public class Occupant {
    private String Citizenship;
    private String PassportNum;

    public Occupant(String citizenship, String passportNum) {
        Citizenship = citizenship;
        PassportNum = passportNum;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public String getPassportNum() {
        return PassportNum;
    }
}
