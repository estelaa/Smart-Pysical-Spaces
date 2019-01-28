package Controller;

import Model.SpaceAux;
import Services.PlannerService;
import Services.SpaceService;
import SnapShot.Demo.SuperJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class PlannerController extends Thread implements Runnable{

    @Autowired
    PlannerService plannerService;
    @Autowired
    SpaceService spaceService;
    @Autowired
    OccupancyController occupancyController;


    @RequestMapping(value = "/PlanerTest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView testPlaner() {
        /*PER FER PROVES*/
        RedirectView rv = new RedirectView();
        if(plannerService.init()){
            rv.setUrl("/main");
            return rv;
        }
        rv.setUrl("/index");
        return rv;
    }


    @RequestMapping(value = "/SuperTest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView testDevicesAndPeople() {
        /*PER FER PROVES*/
        RedirectView rv = new RedirectView();
        rv.setUrl("/main");
        /*Per crear una rutina crearem un new Thread i sobrescriurem el metode run amb la rutina que nosaltres volem, despres fem .start*/
        SuperJson superJson = occupancyController.getSuperCapture();      //Request a la API per llegir els 2 files
        if(plannerService.superUpdatePlanner(superJson)) {
            rv.setUrl("/tree");
        }
        return rv;
    }

    @RequestMapping(value = "/ListSpacesAux", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SpaceAux> getListSpaceAux() {
        return plannerService.getListSpaceAux();
    }



}

