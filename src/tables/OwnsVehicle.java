package tables;

public class OwnsVehicle {

    private String VIN;

    private String Color;

    private String Type;

    private String Brand;

    private String LicensePlate;

    private String Model;

    private String CitizenShip;

    private String PassportNum;

    public OwnsVehicle(String VIN, String color, String type, String brand,
                       String licensePlate, String model, String citizenShip, String passportNum) {
        this.VIN = VIN;
        Color = color;
        Type = type;
        Brand = brand;
        LicensePlate = licensePlate;
        Model = model;
        CitizenShip = citizenShip;
        PassportNum = passportNum;
    }

    public String getVIN() {
        return VIN;
    }

    public String getColor() {
        return Color;
    }

    public String getType() {
        return Type;
    }

    public String getBrand() {
        return Brand;
    }

    public String getLicensePlate() {
        return LicensePlate;
    }

    public String getModel() {
        return Model;
    }

    public String getCitizenShip() {
        return CitizenShip;
    }

    public String getPassportNum() {
        return PassportNum;
    }
}
