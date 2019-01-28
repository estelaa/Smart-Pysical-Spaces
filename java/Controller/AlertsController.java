package Controller;

import Model.Alert;
import Services.AlertsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class AlertsController {
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired AlertsService alertsService;

    /*public Alert getAlert(String URL){
        //Per fer un GET preguntant a un altre edifici si te una alerta??
        return restTemplate.getForObject(URL, Alert.class);
    }*/

    @RequestMapping(value = "/alertNeighbor", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean alertNeighbor(@RequestBody Alert alert){
        /*
        Per si un edifici del "costat" ens envia una Alerta guardar-la a la nostre DB
        */
        alert.setDescription("Neighbor: " + alert.getDescription());
        HttpHeaders responseHeaders = new HttpHeaders();
        if(!alertsService.insertAlert(alert.getDescription())){
            return false;
        }
        return true;
    }




    @RequestMapping(value = "/alertsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Alert> getAllListAlerts() {
        return alertsService.getListAlerts();
    }


    @RequestMapping(value = "/addAlert", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean insertAlert(@RequestBody Alert alert) {
        return alertsService.insertAlert(alert.getDescription());
    }


}
