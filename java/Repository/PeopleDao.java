package Repository;



import Model.Device;
import Model.User;
import Repository.Interfaces.IPeopleDao;
import Model.People;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class PeopleDao implements IPeopleDao {

    private GenericDao<People> genericDao;

    public PeopleDao() {
        this.genericDao = new GenericDao<People>();
    }

    @Override
    public boolean insertPeople(People people) {
        /*
        Return true if the people is correctly saved to the DB,
        return false if a people with this id already exists or is not saved correctly.
        */
        if(isExist(people)){
            return false;
        }
        return genericDao.insert(people);
    }

    @Override
    public People getPeople(int id){
        /*
        Return the People object with the indicated id
         */
        People people = new People();
        List<String> column = getListAtributsPeople();
        String conditionValue = String.valueOf(id);
        List<People> list = genericDao.selectByCondition(column, "id", conditionValue, people);
        if(list.size()==1){
            return list.get(0);
        }else{
            return null; // llençar excepcio nomes pot haver un id igual
        }
    }

    @Override
    public int getPeopleRoom(int id) {
        /*
        We pass the id of a person and returns the id of the room to which it is.
        */
        List<String> column  = new ArrayList();
        column.add("id");
        column.add("idspace");
        String conditionValue = String.valueOf(id);
        List<People> list = genericDao.selectByCondition(column, "id", conditionValue, new People());
        if(list.size()==1){
            return list.get(0).getIdspace();
        }else{
            return 0;
        }
    }

    @Override
    public HashMap<Integer, People> getAllPeopleInRoom(int idRoom) {
        /*
        Returns a HashMap <Integer, People> that contains all the people in the room with idRoom,
        where the key is the id and the value is the People object
        */
        String conditionValue = String.valueOf(idRoom);
        List<People> list = genericDao.selectAllByCondition("idspace", conditionValue, new People());
        HashMap<Integer, People> hashPeople = new HashMap<>();
        for(int index = 0;index<list.size();index++) {
            hashPeople.put(list.get(index).getId(), list.get(index));
        }
        return hashPeople;
    }

    @Override
    public List<People> getListAllPeople() {
        /*
        Return a List with all the People stored in the DB.
        */
        return genericDao.selectAll(new People());
    }

    @Override
    public boolean updateIdSpace(People people) {
        /*
        Return true if the update has worked correctly,
        the method updates the idRoom of the People object that passes it.
        */
        HashMap<String, String> columnValue = getHashPeople(people);
        String conditionValue = String.valueOf(people.getId());
        return genericDao.updateByCondition(columnValue,genericDao.getListTypeAtributs(),"id", conditionValue, typeValue(conditionValue),people);
    }

    @Override
    public boolean removePeople(People people) {
         /*
        Return true if the people is deleted correctly from the DB.
        */
         String conditionValue = String.valueOf(people.getId());
        return genericDao.delete(people, "id", conditionValue, typeValue(conditionValue));
    }

    @Override
    public boolean isExist(People people) {
        /*
        Return true if a space already exists in the DB with the same id.
        */
        List<People> listPeople = getListAllPeople();
        for(People peopleTmp: listPeople){
            if (peopleTmp.getId()==people.getId() ) {
                return true;
            }
            if(peopleTmp.getName().equals(people.getName())){
                return true;
            }
        }
        return false;
    }

    private HashMap<String, String> getHashPeople(People people){
        /*
        (necessary for updateSpace())
        */
        HashMap<String, String> columnValue = new HashMap<>();
        //columnValue.put("id", String.valueOf(people.getId()));
        //columnValue.put("name", people.getName());
        columnValue.put("idspace", String.valueOf(people.getIdspace()));
        return columnValue;
    }

    private List<String> getListAtributsPeople(){
        /*
        Create a List with all the names of the attributes of the People class (necessary for SpaceDao)
        */
        List<String> column = new ArrayList();
        People people = new People();
        Field[] nameVariable = people.getClass().getDeclaredFields();
        for (Field nameVariabletmp: nameVariable) {
            column.add(nameVariabletmp.getName());
        }
        return column;
    }


    private String typeValue(String value){
        Device device = new Device();
        Field[] tField = device.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
            value.equals(tField[j].getName());
            return String.valueOf(tField[j].getType());
        }
        return null;
    }
}
