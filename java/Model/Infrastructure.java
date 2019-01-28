package Model;

public class Infrastructure {

    private int id;
    private String name;
    private int capacitymax;
    private int dependence;
    private boolean modifiable;
    private int localcapacity;



    public Infrastructure(){

    }

    public Infrastructure(int id, String name, int capacitymax, int dependence, boolean modifiable, int localcapacity) {
        this.id = id;
        this.name= name;
        this.capacitymax = capacitymax;
        this.dependence = dependence;
        this.modifiable = modifiable;
        this.localcapacity = localcapacity;
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

    public int getCapacitymax() {
        return capacitymax;
    }

    public void setCapacitymax(int capacitymax) {
        this.capacitymax = capacitymax;
    }

    public int getDependence() {
        return dependence;
    }

    public void setDependence(int dependence) {
        this.dependence = dependence;
    }

    public boolean getModifiable() {
        return modifiable;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    public int getLocalcapacity() {
        return localcapacity;
    }

    public void setLocalcapacity(int localcapacity) {
        this.localcapacity = localcapacity;
    }

}
