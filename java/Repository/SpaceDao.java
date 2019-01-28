package Repository;



import Repository.Interfaces.IGenericDao;
import Repository.Interfaces.ISpaceDao;
import Model.Space;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpaceDao implements ISpaceDao {

    private IGenericDao<Space> genericDao;

    public SpaceDao() {
        this.genericDao = new GenericDao<Space>();
    }

    @Override
    public boolean insertSpace(Space space) {
        /*
        Return true if the space is correctly saved to the DB,
        return false if a Space with this id already exists or is not saved correctly
        */
        if(isExist(space)){
            return false;
        }
        return genericDao.insert(space);
    }

    @Override
    public Space getSpace(int id) {
        /*
        Return the Space object with the indicated id
         */
        Space space = new Space();
        List<String> column = getListAtributsSpace();
        String conditionValue = String.valueOf(id);
        List<Space> list = genericDao.selectByCondition(column, "id", conditionValue, space);
        if(list.size()==1){
            return list.get(0);
        }else{
            return null; // llençar excepcio nomes pot haver un id igual
        }
    }

    @Override
    public Space getSpaceByName(String name) {
        Space space = new Space();
        List<String> column = getListAtributsSpace();
        List<Space> list = genericDao.selectByCondition(column, "name", name, space);
        if(list.size()==1){
            return list.get(0);
        }else{
            return null; // llençar excepcio nomes pot haver un id igual
        }
    }

    @Override
    public HashMap<Integer, Space> getAllSpaces() {
        /*
        Return a hashMap with all the Space stored in the DB,
        where the key is the id and the value the object Space
        */
        List<Space> listSpaces = genericDao.selectAll(new Space());
        HashMap<Integer, Space> hashMapSpaces = new HashMap();
        for(int index = 0;index<listSpaces.size();index++) {
            hashMapSpaces.put(listSpaces.get(index).getId(), listSpaces.get(index));
        }
        return hashMapSpaces;
    }

    @Override
    public List<Space> getListAllSpace(){
        /*
        Return a List with all the Space stored in the DB.
        */
        return genericDao.selectAll(new Space());
    }

    //@Override
    public boolean updateSpace(Space space) {
        /*
        Returns true if the update has worked correctly,
        the method updates the attributes that change from the object we passed to what we have stored in the DB
        */
        HashMap<String,String> columnValue = getHashMapSpace(space);
        String conditionValue = String.valueOf(space.getId());
        List<String> typeValue = genericDao.getListTypeAtributs();
        return genericDao.updateByCondition(columnValue, typeValue,"id", conditionValue, typeValue("id"), space);
    }

    @Override
    public boolean updateCapacityCurrent(int id, int capacityCurrent) {
        /*
        Return true if the update has worked correctly,
        the method updates the capacitycurrent of the Space with the indicated id
        */
        HashMap<String,String> columnValue = new HashMap<>();
        Space space = new Space();
        Field[] nameVariable = space.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase
        String value = String.valueOf(capacityCurrent);
        columnValue.put(nameVariable[2].getName().toLowerCase(), value);    //nameVariable[2] es el atribut capacityCurrent
        String conditionValue = String.valueOf(id);
        List<String> typeValue = new ArrayList<>();
        typeValue.add(String.valueOf(nameVariable[2].getGenericType()));
        return genericDao.updateByCondition(columnValue,typeValue,"id", conditionValue, typeValue("id"), space);
    }

    @Override
    public boolean updateCapacityAllowed(int id, int capacityAllowed) {
        /*
        Return true if the update has worked correctly,
        the method updates the capacityallowed of the Space with the indicated id
        */
        HashMap<String,String> columnValue = new HashMap<>();
        Space space = new Space();
        Field[] nameVariable = space.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase user
        String value = String.valueOf(capacityAllowed);
        columnValue.put(nameVariable[3].getName().toLowerCase(), value);    //nameVariable[3] es el atribut capacityAllowed
        String conditionValue = String.valueOf(id);
        List<String> typeValue = new ArrayList<>();
        typeValue.add(String.valueOf(nameVariable[3].getGenericType()));
        return genericDao.updateByCondition(columnValue, typeValue, "id", conditionValue, typeValue("id"), space);
    }

    @Override
    public boolean removeSpace(Space space) {
        /*
        Return true if the space is deleted correctly from the DB
        */
        String conditionValue = String.valueOf(space.getId());
        return genericDao.delete(space, "id", conditionValue,typeValue("id"));
    }

    @Override
    public boolean isExist(Space space) {
        /*
        Return true if a space already exists in the DB with the same id or name
        */
        List <Space> listSpace = genericDao.selectAll(new Space());
        if(listSpace!=null){
            for(Space spaceTmp: listSpace){
                if(spaceTmp.getId()==space.getId()){
                    return true;
                }
                if(spaceTmp.getName().equals(space.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateOpen(int id, boolean open) {
        HashMap<String,String> columnValue = new HashMap<>();
        Space space = new Space();
        Field[] nameVariable = space.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase user
        String value = String.valueOf(open);
        columnValue.put(nameVariable[7].getName().toLowerCase(), value);    //nameVariable[3] es el atribut capacityAllowed
        String conditionValue = String.valueOf(id);
        List<String> typeValue = new ArrayList<>();
        typeValue.add(String.valueOf(nameVariable[7].getGenericType()));
        return genericDao.updateByCondition(columnValue, typeValue, "id", conditionValue, typeValue("id"), space);
    }


    private HashMap<String, String> getHashMapSpace(Space space) {
        /*
        Crea un hashMap amb HashMap<String, String> amb els valors del infrastructure (necesari per updateInfrastructure())
         */
        HashMap<String, String> hashMapSpace = new HashMap<>();
        //hashMapInfrastructure.put("id", String.valueOf(infra.getId()));      //deixem fer un update del id??
        //hashMapInfrastructure.put("name", infra.getName());             //deixem fer un update del nom??
        hashMapSpace.put("id", String.valueOf(space.getId()));
        hashMapSpace.put("name", space.getName());
        hashMapSpace.put("capacitycurrent", String.valueOf(space.getCapacitycurrent()));
        hashMapSpace.put("capacityallowed", String.valueOf(space.getCapacityallowed()));
        hashMapSpace.put("dependence", String.valueOf(space.getDependence()));
        hashMapSpace.put("dcc", String.valueOf(space.getDcc()));
        hashMapSpace.put("dca", String.valueOf(space.getDca()));
        hashMapSpace.put("open", String.valueOf(space.getOpen()));
        return hashMapSpace;
    }



    private List<String> getListAtributsSpace(){
        /*
        Create a List with all the names of the attributes of the Space class (necessary for SpaceDao)
        */
        List<String> column = new ArrayList();
        Space space = new Space();
        Field[] tField = space.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
           column.add(tField[j].getName());
        }
        return column;
    }

    private String typeValue(String value){
        Space space = new Space();
        Field[] tField = space.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
            value.equals(tField[j].getName());
            return String.valueOf(tField[j].getGenericType());
        }
        return null;
    }



}
