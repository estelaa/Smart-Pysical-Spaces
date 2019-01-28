package Controller;

import Model.Occupancy;
import Services.OccupancyService;
import SnapShot.Demo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@Configuration
public class OccupancyController {
    /*
    *Aquest es el controlador que ENVIA peticions desde el server cap a la API
    * fa el Request i rep la el Json
    * */

    @Autowired private OccupancyService occupancyService;

    private RestTemplate restTemplate = new RestTemplate();

    public DeviceJson getDeviceCapture() {
        return restTemplate.getForObject("http://localhost:8080/UpdateDevices", DeviceJson.class);

    }

    public PeopleJson getPeopleCapture() {
        return restTemplate.getForObject("http://localhost:8080/UpdatePeople", PeopleJson.class);

    }

    public SuperJson getSuperCapture() {
        return restTemplate.getForObject("http://localhost:8080/SuperUpdate", SuperJson.class);

    }

    @RequestMapping(value = "/listAverageOccupancy", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Occupancy> listAverageOccupancy() {
        return occupancyService.getListAverageOccupancy();
    }

}
