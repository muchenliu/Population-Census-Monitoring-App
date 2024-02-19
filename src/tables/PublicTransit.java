package tables;

public class PublicTransit {
    private String LineName;
    private String PName;
    private String TransitType;
    private String Departing;
    private String Destination;

    public PublicTransit(String LineName, String PName, String TransitType, String Departing, String Destination) {
        this.LineName = LineName;
        this.PName = PName;
        this.TransitType = TransitType;
        this.Departing = Departing;
        this.Destination = Destination;
    }

    public String getLineName() {
        return LineName;
    }

    public String getPName() {
        return PName;
    }

    public String getTransitType() {
        return TransitType;
    }

    public String getDeparting() {
        return Departing;
    }

    public String getDestination() {
        return Destination;
    }
}
