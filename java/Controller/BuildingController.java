package Controller;

import Model.Building;
import Services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.List;


import java.awt.*;

@RestController
public class BuildingController {
    @Autowired BuildingService buildingService;

    @RequestMapping(value = "/insertBuilding", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean insertBuilding(@RequestBody Building building){
        return buildingService.insertBuilding(building);
    }

    @RequestMapping(value = "/listBuilding", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Building> getListBuilding(){
        return buildingService.getListBuilding();
    }

}
