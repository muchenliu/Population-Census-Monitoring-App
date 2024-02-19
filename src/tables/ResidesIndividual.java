package tables;

public class ResidesIndividual {

    private String Citizenship;

    private String PassportNum;

    private String PersonName;

    private int Age;

    private int AnnualIncome;

    private String PostalCode;

    private String StreetAddress;

    public ResidesIndividual(String citizenship, String passportNum, String personName, int age,
                             int annualIncome, String postalCode, String streetAddress) {
        Citizenship = citizenship;
        PassportNum = passportNum;
        PersonName = personName;
        Age = age;
        AnnualIncome = annualIncome;
        PostalCode = postalCode;
        StreetAddress = streetAddress;
    }

    public String getPassportNum() {
        return PassportNum;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public int getAnnualIncome() {
        return AnnualIncome;
    }

    public int getAge() {
        return Age;
    }

    public String getPersonName() {
        return PersonName;
    }

    public String getCitizenship() { return Citizenship; }
}
