package Model;

public class PeopleCapture {

    private int idusuari;
    private int iddevice;

    public PeopleCapture() {

    }

    public PeopleCapture(int idusuari, int iddevice) {
        this.idusuari = idusuari;
        this.iddevice = iddevice;
    }

    public int getIdusuari() {
        return idusuari;
    }

    public void setIdusuari(int idusuari) {
        this.idusuari = idusuari;
    }

    public int getIddevice() {
        return iddevice;
    }

    public void setIddevice(int iddevice) {
        this.iddevice = iddevice;
    }
}
