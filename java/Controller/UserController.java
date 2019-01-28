package Controller;

import Model.User;
import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class UserController {


    @Qualifier("Services")
    @Autowired
    private UserService userServices;

    @RequestMapping(value = "/singIn", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean createUser(@RequestBody User user) {
        return userServices.singIn(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean login(@RequestBody User user) {
        return userServices.isExist(user.getUsername(),user.getPassword());
    }

    @RequestMapping(value = "/createUserByAdmin", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView createUserByAdmin(@RequestBody User user) {
        RedirectView rv = new RedirectView();
        if(userServices.createUserByAdmin(user)){
            rv.setUrl("/main");
            return rv;
        }
        rv.setUrl("/createUser");
        return rv;
    }

    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable("username") String username) {
        return userServices.getUser(username);
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public boolean editProfile(@RequestBody User user){
        return userServices.editProfile(user);
    }

    @RequestMapping(value = "/usersList", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userServices.getUsers();
    }

    @RequestMapping(value = "/adminChPrivileges", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean chPrivilegesByAdmin(@RequestBody User user) {
        return userServices.upadteRoleUser(user);
    }

    @RequestMapping(value = "/createTableUser",headers=("content-type=multipart/*"), method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView createTableUser(@RequestParam("file") MultipartFile multipartfile){
        RedirectView rv = new RedirectView();
        if (!multipartfile.isEmpty()) {
            if(userServices.createTableUser(multipartfile)) {
                rv.setUrl("main");
                return rv;
            }else {
                //TO-DO: client side implementation of the file processing error
                System.out.println("processing error");
                rv.setUrl("main");
                return rv;
            }

        } else {
            //TO-DO: client side implementation of the file uploading error
            System.out.println("Uploading error/ file empty");
            rv.setUrl("main");
            return rv;
        }

    }


}
