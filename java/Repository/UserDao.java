package Repository;



import Repository.Interfaces.IGenericDao;
import Repository.Interfaces.IUserDao;
import Model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

@Component("Repository")
@Repository
public class UserDao extends MysqlRepository implements IUserDao {

    private IGenericDao<User> genericDao = new GenericDao<User>();
    protected final static Logger logger = Logger.getLogger(String.valueOf(UserDao.class));

    @Override
    public boolean insertUser(User user) {
        /*
        Returns true if the User is correctly saved to the DB,
        returns false if a User with this username already exists or is not saved correctly
        */
        if(isExist(user)){
            return false;   //si el user ja existeix a la db retornem false i no fem el insert
        }
        return genericDao.insert(user);
    }

    @Override
    public int generateId(){
        Set<Integer> allId = getAllUser().keySet();
        if(!allId.isEmpty()){
            return Collections.max(allId)+1;
        }else{
            return 0;
        }
    }

    @Override
    public User getUser(String username) {
        /*
        Returns the User object with the indicated username.
        */
        List<String> column = getListAtributsUser();
        String conditionValue = username;
        List <User> usersTmp = genericDao.selectByCondition(column, "username", conditionValue, new User());
        if(usersTmp.size()==1){
            return usersTmp.get(0);
        }else {
            //llençem una excepcio? si tenim dos users amb el mateix id
            logger.info("No tenim a cap user amb aquest username");
            return null;
        }
    }

    public int getIdByUsername(String username){
        /*
        Returns the id of username that we passed
        */
        List<String> id = new ArrayList<>();
        id.add("id");
        List<User> users= genericDao.selectByCondition(id,"username",username,new User());
        return users.get(0).getId();
    }

    @Override
    public HashMap<Integer, User> getAllUser() {
        /*
        Returns a hashMap with all the users stored in the DB, where the key is the id and the value the object User
        */
        List<User> listUsers = genericDao.selectAll(new User());
        HashMap<Integer, User> hashMapUsers = new HashMap();
        for(int index = 0;index<listUsers.size();index++) {
            hashMapUsers.put(listUsers.get(index).getId(), listUsers.get(index));
        }
        return hashMapUsers;
    }

    @Override
    public List<User> getListAllUser(){
        /*
        Returns a List <User> with all the Users that we have stored in the DB
        */
        List<User> listUsers = genericDao.selectAll(new User());
        return listUsers;
    }

    @Override
    public boolean updateUser(User user) {
        /*
        It returns true if the update has worked correctly,
        the method updates the attributes that change from the object we passed
        to what we have stored in the DB
        */
        HashMap<String,String> columnValue = getHashMapUser(user);
        String conditionValue = String.valueOf(user.getId());
        return genericDao.updateByCondition(columnValue, genericDao.getListTypeAtributs(),"id", conditionValue, typeValue(conditionValue),user);
    }

    @Override
    public boolean removeUser(User user) {
        /*
        Returns true if the user is deleted correctly from the DB
        */
        String conditionValue = String.valueOf(user.getId());
        return genericDao.delete(user, "id", conditionValue,typeValue(conditionValue));
    }

    @Override
    public boolean isExist(User user) {
        /*
        Returns true if a User already exists in the DB with the same username or id.
        */
        List<User> listUser = getListAllUser();
        for (User userTmp: listUser ) {
            if(userTmp.getId()==user.getId()){
                return true;
            }
            if(userTmp.getUsername().equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    private HashMap<String, String> getHashMapUser(User user) {
        /*
        Crea un hashMap amb HashMap<String, String> amb els valors del user (necesari per updateUser())
         */
        HashMap<String, String> hashMapUser = new HashMap<>();
        //hashMapUser.put("id", String.valueOf(user.getId()));    //es pot fer update del id??
        hashMapUser.put("username", user.getUsername());        //es pot fer update del username?
        hashMapUser.put("password", user.getPassword());
        hashMapUser.put("role", user.getRole());
        hashMapUser.put("name", user.getName());
        hashMapUser.put("email", user.getEmail());
        return hashMapUser;
    }

    private List<String> getListAtributsUser(){
        /*
        Crea una List amb el nom dels atributs de la clase User
        */
        List<String> column = new ArrayList();
        User user = new User();
        Field[] nameVariable = user.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase user
        for (Field nameVariabletmp: nameVariable) {
            column.add(nameVariabletmp.getName());
        }
        return column;
    }


    private String typeValue(String value){
        User user = new User();
        Field[] tField = user.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
            value.equals(tField[j].getName());
            return String.valueOf(tField[j].getType());
        }
        return null;
    }




}
