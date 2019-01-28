package SnapShot.Demo;

import Model.DeviceCapture;
import Services.ReaderService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceJson extends ReaderService {
    /*PER FER PROVES*/

    private List<DeviceCapture> listDevice;

    public DeviceJson() {
        listDevice = readerDeviceCapture();
    }

    public List<DeviceCapture> getListDevice() {
        return listDevice;
    }

    public void setListDevice(List<DeviceCapture> listDevice) {
        this.listDevice = listDevice;
    }

    public List<DeviceCapture> readerDeviceCapture(){
        List<DeviceCapture> listDevice = new ArrayList<>();
        List<String> lines= new ArrayList<>();

        File file = new File("deviceCapture.txt");
        lines = convertFileToList(file);
        for(String lineTmp: lines){
            String[] content = lineTmp.split(",");      //(id, #persones)
            DeviceCapture capture = new DeviceCapture();
            capture.setIddevice(Integer.parseInt(content[0]));
            capture.setCapture(Integer.parseInt(content[1]));
            listDevice.add(capture);
        }
        return listDevice;
    }
}
