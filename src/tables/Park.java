package tables;

public class Park {

    private String ParkName;
    private String CName;
    private String PName;
    private int NumPlaygrounds;
    private int NumDogParks;
    private int NumTrails;

    public Park(String ParkName, String CName, String PName, int NumPlaygrounds, int NumDogParks, int NumTrails) {
        this.ParkName = ParkName;
        this.CName = CName;
        this.PName = PName;
        this.NumPlaygrounds = NumPlaygrounds;
        this.NumDogParks = NumDogParks;
        this.NumTrails = NumTrails;
    }

    public String getParkName() {
        return ParkName;
    }

    public String getCName() {
        return CName;
    }

    public String getPName() {
        return PName;
    }

    public int getNumPlaygrounds() {
        return NumPlaygrounds;
    }

    public int getNumDogParks() {
        return NumDogParks;
    }

    public int getNumTrails() {
        return NumTrails;
    }
}
