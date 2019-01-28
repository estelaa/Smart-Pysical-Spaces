package SnapShot;


import SnapShot.Demo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIOutServer {
    /*Aquest es el controlador que REP les peticions a la API*/

    @RequestMapping("/UpdateDevices")
    public DeviceJson devices() {
        DeviceJson deviceJson = new DeviceJson();
        deviceJson.setListDevice(deviceJson.readerDeviceCapture());
        return deviceJson;
    }

    @RequestMapping("/UpdatePeople")
    public PeopleJson people() {
        return new PeopleJson();
    }

    @RequestMapping("/SuperUpdate")
    public SuperJson update() {
        return new SuperJson();
    }

}