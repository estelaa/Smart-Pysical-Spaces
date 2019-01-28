package Model;

public class Device {

    private int id; //iddevice
    private String name;
    private int idspace_p;
    private int idspace_n;
    private  int priority;
    private boolean active;
    private boolean door_counter;
    private boolean room_counter;
    private boolean virtual_counter;
    private int old_capture;

    public Device(){
        this.priority = 100;
        this.active = true;
        this.old_capture = 0;
    }

    public Device(int id, String name, int idspace_p, int idspace_n, boolean door_counter, boolean room_counter, boolean virtual_counter) {
        this.id = id;
        this.name = name;
        this.idspace_p = idspace_p;
        this.idspace_n = idspace_n;
        this.priority = 100;
        this.active = true;
        this.door_counter = door_counter;
        this.room_counter =  room_counter;
        this.virtual_counter = virtual_counter;
        this.old_capture = 0;
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

    public int getIdspace_p() {
        return idspace_p;
    }

    public void setIdspace_p(int idspace_p) {
        this.idspace_p = idspace_p;
    }

    public int getIdspace_n() {
        return idspace_n;
    }

    public void setIdspace_n(int idspace_n) {
        this.idspace_n = idspace_n;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getDoor_counter() {
        return door_counter;
    }

    public void setDoor_counter(boolean door_counter) {
        this.door_counter = door_counter;
    }

    public boolean getRoom_counter() {
        return room_counter;
    }

    public void setRoom_counter(boolean room_counter) {
        this.room_counter = room_counter;
    }

    public boolean getVirtual_counter() {
        return virtual_counter;
    }

    public void setVirtual_counter(boolean virtual_counter) {
        this.virtual_counter = virtual_counter;
    }

    public int getOld_capture() {
        return old_capture;
    }

    public void setOld_capture(int old_capture) {
        this.old_capture = old_capture;
    }
}
