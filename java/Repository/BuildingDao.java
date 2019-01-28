package Repository;

import Model.Building;

import java.lang.reflect.Field;
import java.util.*;

public class BuildingDao {
    private GenericDao<Building> genericDao;

    public BuildingDao() {
        this.genericDao = new GenericDao<Building>();
    }

    public boolean insertBuilding(Building building){
        if(isExist(building)){
            return false;   //si ja existeix un edifici amb el mateix nom retornem false
        }
        return genericDao.insert(building);
    }

    public int generateId(){
        Set<Integer> allId = getAllBuilding().keySet();
        if(!allId.isEmpty()){
            return Collections.max(allId)+1;
        }else{
            return 0;
        }
    }

    public boolean isExist(Building building) {
        /*
        Returns true if a Building already exists in the DB with the same name.
        */
        List<Building> listBuilding = getListAllBuilding();
        for (Building buildingTmp: listBuilding ) {
            /*if(buildingTmp.getId()==buildingTmp.getId()){
                return true;
            }*/
            if(building.getName().equals(buildingTmp.getName())){
                return true;
            }
        }
        return false;
    }


    public List<Building> getListAllBuilding(){
        return genericDao.selectAll(new Building());
    }

    public HashMap<Integer,Building> getAllBuilding(){
        List<Building> list = genericDao.selectAll(new Building());
        HashMap<Integer,Building> hashBuilding = new HashMap<>();
        for(Building building: list){
            hashBuilding.put(building.getId(),building);
        }
        return hashBuilding;
    }

    public Building getBuildingByName(String name){
        List<String> column = getListAtributsBuilding();
        return genericDao.selectByCondition(column, "name",name,new Building()).get(0);
    }

    private List<String> getListAtributsBuilding(){
        /*
        Crea una List amb el nom dels atributs de la clase User
        */
        List<String> column = new ArrayList();
        Building user = new Building();
        Field[] nameVariable = user.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase user
        for (Field nameVariabletmp: nameVariable) {
            column.add(nameVariabletmp.getName());
        }
        return column;
    }
}
