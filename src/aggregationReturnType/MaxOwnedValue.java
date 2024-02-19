package aggregationReturnType;

public class MaxOwnedValue {

    private String Citizenship;
    private String PassportNum;
    private String MaxValue;

    public MaxOwnedValue(String citizenship, String passportNum, String maxValue) {
        Citizenship = citizenship;
        PassportNum = passportNum;
        MaxValue = maxValue;
    }

    public String getCitizenship() {
        return Citizenship;
    }

    public String getPassportNum() {
        return PassportNum;
    }

    public String getMaxValue() {
        return MaxValue;
    }
}
