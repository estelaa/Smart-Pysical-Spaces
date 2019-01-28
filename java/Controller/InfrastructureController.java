package Controller;


import Model.Infrastructure;
import Services.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import Services.InfrastructureService;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;


@RestController
public class InfrastructureController {

    @Autowired
    private InfrastructureService infraServices;

    @Autowired
    private PlannerService plannerService;

    @RequestMapping(value = "/createInfra", method = RequestMethod.POST)
    public RedirectView createTableInfra(@RequestParam("file") MultipartFile multipartfile){
        RedirectView rv = new RedirectView();
        if (!multipartfile.isEmpty()) {
            if(infraServices.createTableInfra(multipartfile)) {
                plannerService.init();
                rv.setUrl("main");
                return rv;
            }else {
                //TO-DO: client side implementation of the file processing error
                rv.setUrl("main");
                return rv;
            }
        } else {
            //TO-DO: client side implementation of the file uploading error
            rv.setUrl("main");
            return rv;
        }

    }

     @RequestMapping(value = "/insertInfra", method = RequestMethod.POST,
     produces = MediaType.APPLICATION_JSON_VALUE,
     consumes = MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody boolean insertInfrastructure(@RequestBody Infrastructure infrastructure) {
        infrastructure.setLocalcapacity(infrastructure.getCapacitymax());
        return infraServices.insertInfrastructure(infrastructure);
     }


    @RequestMapping(value = "/infrastructuresList", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Infrastructure> getAllListInfrastructures() {
        return infraServices.getListAllInfrastructure();
    }

    @RequestMapping(value = "/infrastructuresListModificable", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Infrastructure> getAllListInfrastructuresModificable() {
        List<Infrastructure> infrastructureList = infraServices.getListAllInfrastructure();
        List<Infrastructure> infrastructuresModificables = new ArrayList<>();
        for (Infrastructure infrastruture: infrastructureList) {
            if(infrastruture.getModifiable()) infrastructuresModificables.add(infrastruture);
        }
        return infrastructuresModificables;
    }

}
