package Controller;

import Model.Device;
import Services.DeviceService;
import Services.PlannerService;
import SnapShot.Demo.DeviceJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class DeviceController {

    @Autowired private DeviceService deviceService;
    @Autowired private PlannerService plannerService;
    @Autowired
    OccupancyController occupancyControler;

    @RequestMapping(value = "/devicesList", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Device> getAllListInfrastructures() {
        return deviceService.getDevices();
    }


    @RequestMapping(value = "/deviceTest", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView deviceTest() {
        /*PER FER PROVES*/
        RedirectView rv = new RedirectView();
        DeviceJson deviceJson =  occupancyControler.getDeviceCapture();         //llegim el file devices de la API
        if(!deviceService.UpdateCCByDevice(deviceJson.getListDevice())){
            rv.setUrl("/index");
            return rv;
        }
        plannerService.recalculateDCC();
        rv.setUrl("/main");
        return rv;
    }

    @RequestMapping(value = "/createTableDevice", method = RequestMethod.POST)
    public RedirectView createTableInfra(@RequestParam("file") MultipartFile multipartfile){
        RedirectView rv = new RedirectView();
        if (!multipartfile.isEmpty()) {
            if(deviceService.createTableDevice(multipartfile)) {
                rv.setUrl("main");
                return rv;
            }else {
                //TO-DO: client side implementation of the file processing error
                rv.setUrl("createDevice");
                return rv;
            }

        } else {
            //TO-DO: client side implementation of the file uploading error
            rv.setUrl("createDevice");
            return rv;
        }
    }

    @RequestMapping(value = "/insertDevice", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean insertInfrastructure(@RequestBody Device device) {
        return deviceService.insertDevice(device);
    }
}
