package SnapShot.Demo;

import Model.DeviceCapture;
import Model.PeopleCapture;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperJson extends GenerateFile {

    private List<DeviceCapture> listDevice;
    private List<PeopleCapture> listPeople;

    public SuperJson() {
        /*El Constructor d'quest objecte es a traves del reader del file per enviar una lectura nova cada cop*/
        //run();
        this.listDevice = readerDeviceCapture();
        this.listPeople = readerPeople();

    }

    public List<DeviceCapture> getListDevice() {
        return listDevice;
    }

    public void setListDevice(List<DeviceCapture> listDevice) {
        this.listDevice = listDevice;
    }

    public List<PeopleCapture> getListPeople() {
        return listPeople;
    }

    public void setListPeople(List<PeopleCapture> listPeople) {
        this.listPeople = listPeople;
    }

    public List<PeopleCapture> readerPeople(){
        /*La llista la omplim llegint el file que crea el matlab (id, idroom)
        * passem objectes People amb aquets dos camps */
        List<PeopleCapture> listPeopleCapture = new ArrayList<>();
        List<String> lines= new ArrayList<>();

        File file = new File("peopleCapture.txt");
        lines = convertFileToListDemo(file);
        for(String lineTmp: lines) {
            String[] content = lineTmp.split(",");      //(iddevice, idusuari)
            PeopleCapture peopleCapture = new PeopleCapture();
            peopleCapture.setIddevice(Integer.parseInt(content[0]));
            peopleCapture.setIdusuari(Integer.parseInt(content[1]));
            listPeopleCapture.add(peopleCapture);
        }
        return listPeopleCapture;
    }

    public List<DeviceCapture> readerDeviceCapture(){
        /*La llista la omplim llegint el file que crea el matlab (id, #capture)
         * passem objectes DeviceCapture amb aquets dos camps */
        List<DeviceCapture> listDevice = new ArrayList<>();
        File file = new File("DemoFiles/deviceCapture"+Counter.getCounter()+".txt");
        List<String> lines = convertFileToListDemo(file);
        init();
        for(String lineTmp: lines){
            String[] content = lineTmp.split(",");      //(iddevice, #capture)
            DeviceCapture capture = new DeviceCapture();
            capture.setIddevice(Integer.parseInt(content[0]));
            capture.setCapture(Integer.parseInt(content[1]));
            listDevice.add(capture);
            run(capture.getIddevice(),capture.getCapture(),lineTmp.equals(lines.get(lines.size()-1)));
        }
        Counter.plusCounter();
        if(Counter.getCounter()==8) Counter.resetCounter();
        return listDevice;
    }
}
