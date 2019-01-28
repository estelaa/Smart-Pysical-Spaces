package Repository;


import Model.Space;
import Repository.Interfaces.IDeviceDao;
import Repository.Interfaces.IGenericDao;
import Model.Device;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
public class DeviceDao implements IDeviceDao {

    private IGenericDao<Device> genericDao;


    public DeviceDao() {
        this.genericDao = new GenericDao<Device>();
    }

    @Override
    public boolean insertDevice(Device device) {
        /*
        Return true if the device is correctly saved to the DB,
        return false if a device with this id already exists or is not saved correctly
        */
        if(isExist(device)){
            return false;
        }
        return genericDao.insert(device);
    }

    @Override
    public int generateId(){
        Set<Integer> allId = getAllDevice().keySet();
        if(!allId.isEmpty()){
            return Collections.max(allId)+1;
        }else{
            return 0;
        }
    }

    @Override
    public Device getDevice(int id) {
         /*
        Return the Device object with the indicated id
        */
        List<String> column = getListAtributsDevice();
        String condition = "id";
        String conditionValue = String.valueOf(id);
        List<Device> list = genericDao.selectByCondition(column,condition,conditionValue, new Device());
        if(list.size()==1){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<Integer, Device> getAllDevice() {
        /*
        Return a hashMap with all the Device stored in the DB,
        where the key is the id and the value the object Device
        */
        List<Device> listDevice= genericDao.selectAll(new Device());
        HashMap<Integer, Device> hashDevices = new HashMap();
        for(int index = 0;index<listDevice.size();index++) {
            hashDevices.put(listDevice.get(index).getId(), listDevice.get(index));
        }
        return hashDevices;
    }

    @Override
    public List<Device> getListAllDevice() {
        /*
        Return a List with all the Space stored in the DB
        */
        return genericDao.selectAll(new Device());
    }

    @Override
    public boolean updateActiveDevice(Device device) {
        /*
        Return true if the update has worked correctly,
        the method updates the attributes that change from the object we passed to what we have stored in the DB
        */
        HashMap<String,String> columnValue = new HashMap();
        //columnValue.put("priority", String.valueOf(device.getPriority()));  //Fem que tmb es pugui canviar la prioritat amb el mateix metode?
        columnValue.put("", String.valueOf(device.getActive()));
        String conditionValue = String.valueOf(device.getId());
        return genericDao.updateByCondition(columnValue, genericDao.getListTypeAtributs() ,"id", conditionValue, typeValue(conditionValue),device);
    }

    @Override
    public boolean removeDevice(Device device) {
         /*
        Return true if the device is deleted correctly from the DB
        */
        String conditionValue = String.valueOf(device.getId());
        return genericDao.delete(device, "id", conditionValue,typeValue(conditionValue));
    }

    @Override
    public boolean isExist(Device device) {
        /*
        Return true if a device already exists in the DB with the same id or name.
        */
        List <Device> listDevice = genericDao.selectAll(new Device());
        for(Device deviceTmp: listDevice){
            if(deviceTmp.getId()==device.getId()){
                return true;
            }
            if(deviceTmp.getName().equals(device.getName())){
                return true;
            }
        }
        return false;
    }

    public boolean updateOldCapture(int iddevice, int oldCapture) {
        /*
        Return true if the update has worked correctly,
        the method updates the capacityallowed of the Space with the indicated id
        */
        HashMap<String,String> columnValue = new HashMap<>();
        Device device = new Device();
        Field[] nameVariable = device.getClass().getDeclaredFields();     //amb el metode getDeeclaredFields() obtenim el noms dels atributs de la clase user
        String value = String.valueOf(oldCapture);
        columnValue.put(nameVariable[9].getName().toLowerCase(), value);    //nameVariable[8] es el atribut old_capture
        String conditionValue = String.valueOf(iddevice);
        List<String> typeValue = new ArrayList<>();
        typeValue.add(String.valueOf(nameVariable[9].getGenericType()));
        return genericDao.updateByCondition(columnValue, typeValue,"id", conditionValue, typeValue("id"), device);
    }

    private List<String> getListAtributsDevice(){
        /*
        Crea una List amb tots els noms dels atributs de la classe Device
        */
        List<String> column = new ArrayList();
        Device device = new Device();
        Field[] nameVariable = device.getClass().getDeclaredFields();
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
            return String.valueOf(tField[j].getGenericType());
        }
        return null;
    }


}
