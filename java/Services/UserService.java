package Services;


import Model.Enum.Role;
import Model.User;
import Repository.UserDao;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component("Services")
@Service
public class UserService extends ReaderService {


    private UserDao userDao = new UserDao();
    private EmailService emailService = new EmailService();

    public boolean singIn(User user){
        /*Metode per quan un Stander User es registre desde la web.xml*/
        user.setId(userDao.generateId());
        user.setRole(Role.STANDARD_USER.name());
        if(userDao.insertUser(user)) {
            String text = "Your username is " + user.getUsername() + "\nyour password is " + user.getPassword();
            return true;
        }
        return false;
    }

    public boolean createUserByAdmin(User user){
        /*Metode perque el Admin pugui crear Authorized Personal o Admin*/
        user.setId(userDao.generateId());
        switch (user.getRole()){
            case "Admin": case "ADMIN":
                user.setRole(Role.ADMIN.name());
                break;
            case "Authorized Personal": case "AUTHORIZED_PERSONAL":
                user.setRole(Role.AUTHORIZED_PERSONAL.name());
                break;
            default:
                user.setRole(Role.STANDARD_USER.name());
        }
        user.setPassword(user.getId()+user.getName()+user.getRole().split(" ")[0]);
        if(userDao.insertUser(user)) {
            String text = "Your username is " + user.getUsername() + "\nyour password is " + user.getPassword();
            emailService.sendSimpleMessage(user.getEmail(), "Welcome to Smart Physical Spaces", text);
            return true;
        }
        return false;//SI EL INSERTUSER RETORNA FALSE ENVIEM MISSATGE A LA WEB??
    }

    public boolean createTableUser(MultipartFile multipartFile){
        List<String> lines= convertMultipartToList(multipartFile);
        for(String lineTmp: lines){
            String[] content = lineTmp.split(",");//(username, password, role, name, email)
            User user = new User();
            user.setUsername(content[0]);   //content[0] = username
            //user.setPassword(content[1]);   //content[1] = password     El password tmb el podria fer el metode createUserByAdmin
            user.setRole(content[1]);       //content[2] = role
            user.setName(content[2]);       //content[3] = name
            user.setEmail(content[3]);      //content[4] = email
            if(!createUserByAdmin(user)){   //el metode createUserByAdmin ja assigna un id, i envia un mail indicant el username i password
                //SI NO FUNCIONA ENVIEM UN MISSATGE A LA WEB???
                return false;
            }
        }
        return true;
    }

    public boolean upadteRoleUser(User user){
        User userTmp = userDao.getUser(user.getUsername());
        userTmp.setRole(user.getRole());
        return userDao.updateUser(userTmp);
    }

    public User getUser(String username){
        return userDao.getUser(username);
    }

    public boolean isExist(String username, String password) {
        // es per la web canviar nom (exemple: checkPass)hi ha un que es diu igual al DAO
        User user = userDao.getUser(username);
        if(user.getUsername().equals(username))
            if (user.getPassword().equals(password)) return true;
        return false;
    }

    public boolean editProfile(User user){
        /*Cambiara solament els valors que no estiguin "buits"*/
        if(userDao.isExist(user)){
            User userTemp = userDao.getUser(user.getUsername());
            if(user.getName()!=null){
                if(!user.getName().equals("")){
                    userTemp.setName(user.getName());
                }
                if(user.getEmail()!=""){
                    userTemp.setEmail(user.getEmail());
                }
                if(user.getPassword()!=""){
                    userTemp.setPassword(user.getPassword());
                }
                userDao.updateUser(userTemp);
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers(){
        return userDao.getListAllUser();
    }


}
