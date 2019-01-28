package Controller;

import Model.Space;
import Services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @RequestMapping(value = "/spacesList", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Space> getSpaces() {
        return spaceService.getListSpaces();
    }

    @RequestMapping(value = "/modCapAllowed", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean modCapAllowed(@RequestBody Space space) {
        return spaceService.modCapAllowed(space);
    }
    @RequestMapping(value = "/modCapCurrent", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean modCapCurrent(@RequestBody Space space) {
        return spaceService.modCapCurrent(space);
    }

}

