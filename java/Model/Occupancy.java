package Model;

public class Occupancy {

    private int idspace;
    private String date;
    private int capacitycurrent;

    public Occupancy() {
    }

    public Occupancy(int idspace, String date, int capacitycurrent) {
        this.idspace = idspace;
        this.date = date;
        this.capacitycurrent = capacitycurrent;
    }

    public int getIdspace() {
        return idspace;
    }

    public void setIdspace(int idspace) {
        this.idspace = idspace;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCapacitycurrent() {
        return capacitycurrent;
    }

    public void setCapacitycurrent(int capacitycurrent) {
        this.capacitycurrent = capacitycurrent;
    }
}
