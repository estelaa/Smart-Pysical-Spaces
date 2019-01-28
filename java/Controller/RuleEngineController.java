package Controller;

import Model.RuleEngine;
import Services.RuleEngineServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RuleEngineController {

    @Autowired
    private RuleEngineServices ruleEngineServices;

    @RequestMapping(value = "/admin/newRule", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    boolean createUser(@RequestBody RuleEngine ruleEngine) {
        return ruleEngineServices.addNewRule(ruleEngine);
    }

    @RequestMapping(value = "/admin/deleteRule/{id}", method = RequestMethod.POST)
    public @ResponseBody
    boolean createUser(@PathVariable("id") Integer id) {
        return ruleEngineServices.deleteRule(id);
    }

    @RequestMapping(value = "/admin/showRules/{id}", method = RequestMethod.GET)
    public List<RuleEngine> getRules(@PathVariable("id") int idSpace) {
        return ruleEngineServices.getAllRulesFrom(idSpace);
    }


    @RequestMapping(value = "/spaceIsOpen/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean isOpen(@PathVariable("id") int id) {
        return ruleEngineServices.checkIfSpaceIsOpen(id);
    }

}
