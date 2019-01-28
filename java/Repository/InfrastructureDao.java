package Repository;



import Model.Space;
import Repository.Interfaces.IGenericDao;
import Repository.Interfaces.IInfrastructureDao;
import Model.Infrastructure;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
public class InfrastructureDao implements IInfrastructureDao {

    private IGenericDao<Infrastructure> genericDao;
    private SpaceDao spaceDao = new SpaceDao();

    public InfrastructureDao() {
        this.genericDao = new GenericDao<Infrastructure>();
    }

    @Override
    public boolean create() {
        /*
        CREATE TABLE `pae`.`infrastructure` (
        `id` INT NULL,
        `name` VARCHAR(45) NULL,
        `capacitymax` INT NULL,
        `dependence` INT NULL,
        `modificable` INT NULL,
        `localcapacity` INT NULL);
        */
        return false;
    }

    @Override
    public boolean insertInfrastructure(Infrastructure infrastructure) {
        /*
        Returns true if it has been correctly saved into the DB,
        returns false if a Infrastructure with this id already exists or is not saved correctly.
        */
        infrastructure.setId(generateId());
        if(isExist(infrastructure)){
            return false;
        }
        recalculateInfra(infrastructure);
        createSpace(infrastructure);
        return genericDao.insert(infrastructure);
    }

    @Override
    public int generateId(){
        Set<Integer> allId = getAllInfrastructures().keySet();
        if(!allId.isEmpty()){
            return Collections.max(allId)+1;
        }else{
            return 0;
        }
    }

    private void recalculateInfra(Infrastructure infra){
        /*
        Si fem un insert de una infrastructure (nova) i aquesta depen de una altre cridem aquest metode
        */
        if(infra.getId()!=infra.getDependence()){                                               //comprovem que te dependencia

            HashMap<Integer, Infrastructure> mapInfra = getAllInfrastructures();
            boolean exit= false;
            SpaceDao spaceDao = new SpaceDao();
            Infrastructure infraTmp = mapInfra.get(infra.getDependence());

            while (!exit){
                int newCapMax = infraTmp.getCapacitymax() + infra.getLocalcapacity();         //calculem la nova capMax
                infraTmp.setCapacitymax(newCapMax);                                           //li assignem aquesta capMax
                updateInfrastructure(infraTmp);                                               //Actualitzem la DB
                mapInfra.put(infraTmp.getId(),infraTmp);                                      //Actualitzem el hashMap
                spaceDao.updateCapacityAllowed(infraTmp.getId(),infraTmp.getLocalcapacity());           //actualitzem el space amb el mateix id
                if((infraTmp.getDependence()==infraTmp.getId())){
                    exit=true;                                                               //Si el id==idDep sortim del while
                }
                infraTmp = mapInfra.get(infraTmp.getDependence());                            //infraTmp es la dependencia superior a infrastructure
            }
        }
    }

    private boolean createSpace(Infrastructure infra){
        /*
        Si fem un insert de una nova infrastrucure creem un nou space amb CC=0 CA=LC idDep=idDep
        */
        Space space = new Space(infra.getId(),infra.getName(),0, infra.getLocalcapacity(), infra.getDependence());
        if(!spaceDao.insertSpace(space)){
            //Si falla el insert del Space que fem?
            return false;
        }
        return true;
    }

    @Override
    public int getMaxCapacityById(int id) {
        /*
        Returns the maxcapacity of the infrastructure with the indicated id.
        */
        List column =  new ArrayList();
        column.add("id");
        column.add("capacitymax");
        String conditionValue = String.valueOf(id);
        List<Infrastructure> maxCapacityById = genericDao.selectByCondition(column, "id", conditionValue, new Infrastructure());
        if(maxCapacityById.size()==1){
            return maxCapacityById.get(0).getCapacitymax();
        }else{
            return 0; // nomes hi ha un id igual
        }
    }

    @Override
    public Infrastructure getInfrastructure(int id) {
        /*
        Returns the Infrastructure with the specified id.
        */
        Infrastructure infrastrucure = new Infrastructure();
        List<String> column = getListAtributsInfrastructure();
        String conditionValue = String.valueOf(id);
        List<Infrastructure> list = genericDao.selectByCondition(column, "id", conditionValue, infrastrucure);
        if(list.size()==1){
            return list.get(0);
        }else{
            return null; // llençar excepcio nomes pot haver un id igual, o no existeix
        }
    }

    @Override
    public List<Infrastructure> getListAllInfrastructure(){
        /*
        Returns all the infrastructure objects saved in the DB
        */
        List <Infrastructure> listInfrastructures = genericDao.selectAll(new Infrastructure());
        return listInfrastructures;

    }

    @Override
    public HashMap< Integer ,Infrastructure> getAllInfrastructures() {
        /*
        Returns all the Infrastructures ordered in a hashMap
        where the key is the id and value is the object Infrastructure.
        */
        List <Infrastructure> listInfrastructures = genericDao.selectAll(new Infrastructure());
        HashMap<Integer, Infrastructure> hashInfra = new HashMap<>();
        if(listInfrastructures!=null){
            for(Infrastructure infra: listInfrastructures) {
                hashInfra.put(infra.getId(), infra);
            }
        }
        return hashInfra;
    }

    @Override
    public boolean updateInfrastructure(Infrastructure infrastructure) {
        /*
        Returns true if the update has worked correctly,
        the method updates the attributes that change from the object we passed to what we have stored in the DB
        */
        HashMap<String,String> columnValue = getHashMapInfrastructure(infrastructure);
        String conditionValue = String.valueOf(infrastructure.getId());
        return genericDao.updateByCondition(columnValue, getListAtributsInfrastructure(),"id", conditionValue, typeValue("id"),infrastructure);
    }

    @Override
    public boolean removeInfrastructure(Infrastructure infrastructure) {
        /*
        Returns true if the Infrastructure is deleted correctly from the DB
        */
        String conditionValue = String.valueOf(infrastructure.getId());
        return genericDao.delete(infrastructure, "id", conditionValue,typeValue("id"));
    }

    @Override
    public boolean isExist(Infrastructure infrastructure) {
        /*
        Returns true if a Infrastrucutre already exists in the DB with the same id or name
        */
        List <Infrastructure> listInfrastructures = genericDao.selectAll(new Infrastructure());
        if(listInfrastructures!=null){
            for(Infrastructure infraTmp: listInfrastructures){
                if(infraTmp.getId()==infrastructure.getId()){
                    return true;
                }
                if(infraTmp.getName().equals(infrastructure.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<Integer> getSDependencies(int id){
    /*
    Els opjectes Space tenen un array de les seves dependencies superiors.
    P.E si tenim el Space 9 que depen del Space 7 i el Space 7 depen del Space 1:
    Al Space 9 indrem un array amb valors 7,1

    El que fa la funció es retornar aquestes dependencies quan es modifica la infrastructura.
     */
        ArrayList<Integer> dependencies = new ArrayList<>();
        int idtmp = id;
        int tmp = 1;
        if(getInfrastructure(idtmp).getDependence()!= idtmp){
            dependencies.add(getInfrastructure(idtmp).getDependence());
            idtmp=getInfrastructure(idtmp).getDependence();
        }else{
            tmp=0;
            dependencies=null;
        }
        while (tmp!=0){
            if(getInfrastructure(idtmp).getDependence()!= idtmp){
                dependencies.add(getInfrastructure(idtmp).getDependence());
                idtmp=getInfrastructure(idtmp).getDependence();
            }else{
                tmp=0;
            }
        }
        return dependencies;
    }

    private List<String> getListAtributsInfrastructure(){
        /*
        Crea una List amb tots els noms dels atributs de la classe Infrastructure
        */
        List<String> column = new ArrayList();
        Infrastructure infra = new Infrastructure();
        Field[] nameVariable = infra.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase user
        for (Field nameVariabletmp: nameVariable) {
            column.add(nameVariabletmp.getName());
        }
        return column;
    }

    private HashMap<String, String> getHashMapInfrastructure(Infrastructure infra) {
        /*
        Crea un hashMap amb HashMap<String, String> amb els valors del infrastructure (necesari per updateInfrastructure())
         */
        HashMap<String, String> hashMapInfrastructure = new HashMap<>();
        //hashMapInfrastructure.put("id", String.valueOf(infra.getId()));      //deixem fer un update del id??
        //hashMapInfrastructure.put("name", infra.getName());             //deixem fer un update del nom??
        hashMapInfrastructure.put("capacitymax", String.valueOf(infra.getCapacitymax()));
        hashMapInfrastructure.put("dependence", String.valueOf(infra.getDependence()));
        hashMapInfrastructure.put("modifiable", String.valueOf(infra.getModifiable()));
        hashMapInfrastructure.put("localcapacity", String.valueOf(infra.getLocalcapacity()));
        return hashMapInfrastructure;
    }


    private String typeValue(String value){
        Infrastructure infrastructure = new Infrastructure();
        Field[] tField = infrastructure.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
            value.equals(tField[j].getName());
            return String.valueOf(tField[j].getType());
        }
        return null;
    }

}
