package Repository.Interfaces;


import Model.People;

import java.util.HashMap;
import java.util.List;

public interface IPeopleDao {

    public boolean insertPeople(People people);
    public People getPeople(int id);
    public int getPeopleRoom(int id);
    public HashMap<Integer, People> getAllPeopleInRoom(int idRoom);
    public List<People> getListAllPeople();
    public boolean updateIdSpace(People people);
    public boolean removePeople(People people);
    public boolean isExist(People people);
}
