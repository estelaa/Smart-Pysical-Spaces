package Controller;

import Model.People;
import Model.User;
import Services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PeopleController {

    @Autowired PeopleService peopleService;
    @Autowired OccupancyController occupancyControler;

    @RequestMapping(value = "/peopleList", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<People> getAllListInfrastructures() {
        return peopleService.getPeople();
    }

    @RequestMapping(value = "/insertPeople", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    boolean insertPeople(@RequestBody User user) {
        return peopleService.createPeople(user);
    }

}
