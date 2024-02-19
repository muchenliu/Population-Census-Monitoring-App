package tables;

public class Province {
    private String PName;
    private int AvgIncome;
    private int Population;

    public Province (String PName, int AvgIncome, int Population) {
        this.PName = PName;
        this.AvgIncome = AvgIncome;
        this.Population = Population;
    }

    public int getAvgIncome() {
        return AvgIncome;
    }

    public String getPName() {
        return PName;
    }

    public int getPopulation() {
        return Population;
    }
}
