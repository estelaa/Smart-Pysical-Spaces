package Repository.Interfaces;



import Model.User;

import java.util.HashMap;
import java.util.List;

public interface IUserDao {

    public boolean insertUser(User user);
    public int generateId();
    public User getUser(String username);
    public HashMap<Integer, User> getAllUser();
    public List<User> getListAllUser();
    public boolean removeUser(User user);
    public boolean updateUser(User user);
    public int getIdByUsername(String username);
    public boolean isExist(User user);
    //public boolean loginUser(String username, String password); //el propi login user pot retornar el role??
    //public String getRoleUser(String username);
    //public boolean register(List<User>);

}
