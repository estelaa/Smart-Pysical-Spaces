package Model;

public class DeviceCapture {

    private int iddevice;
    private int capture;

    public DeviceCapture() {
    }

    public DeviceCapture(int idspace, int capture) {
        this.iddevice = idspace;
        this.capture = capture;
    }

    public int getIddevice() {
        return iddevice;
    }

    public void setIddevice(int iddevice) {
        this.iddevice = iddevice;
    }

    public int getCapture() {
        return capture;
    }

    public void setCapture(int capture) {
        this.capture = capture;
    }
}
