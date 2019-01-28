package Model;


public class Space {

    private int id;
    private String name;
    private int capacitycurrent;
    private int capacityallowed;
    private int dependence;
    private int dcc;
    private int dca;
    private boolean open;


    public Space(){

    }

    public Space(int id, String name, int capacitycurrent, int capacityallowed, int dependence) {
        this.id = id;
        this.name = name;
        this.capacitycurrent = capacitycurrent;
        this.capacityallowed = capacityallowed;
        this.dependence = dependence;
        this.dcc = capacitycurrent;
        this.dca = capacityallowed;
        this.open = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacitycurrent() { return capacitycurrent; }

    public void setCapacitycurrent(int capacitycurrent) {
        this.capacitycurrent = capacitycurrent;
    }

    public int getCapacityallowed() {
        return capacityallowed;
    }

    public void setCapacityallowed(int capacityallowed) {
        this.capacityallowed = capacityallowed;
    }

    public int getDcc() { return dcc; }

    public void setDcc(int dcc) { this.dcc = dcc; }

    public int getDca() { return dca; }

    public void setDca(int dca) { this.dca = dca; }

    public int getDependence() {
        return dependence;
    }

    public void setDependence(int dependence) {
        this.dependence = dependence;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


}