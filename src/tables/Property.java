package tables;

public class Property {
    private String PostalCode;
    private String StreetAddress;
    private double PropSize;
    private int NumBedroom;
    private int NumBathroom;

    public Property(String PostalCode, String StreetAddress, double PropSize, int NumBedroom, int NumBathroom) {
        this.PostalCode = PostalCode;
        this.StreetAddress = StreetAddress;
        this.PropSize = PropSize;
        this.NumBedroom = NumBedroom;
        this.NumBathroom = NumBathroom;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public double getPropSize() {
        return PropSize;
    }

    public int getNumBedroom() {
        return NumBedroom;
    }

    public int getNumBathroom() {
        return NumBathroom;
    }
}
