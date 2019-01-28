package Repository.Interfaces;


import Model.Device;

import java.util.HashMap;
import java.util.List;

public interface IDeviceDao {
    public boolean insertDevice(Device device);
    public int generateId();
    public Device getDevice(int id);
    public HashMap<Integer, Device> getAllDevice();
    public List<Device> getListAllDevice();
    public boolean updateActiveDevice(Device device);
    public boolean removeDevice(Device device);
    public boolean isExist(Device device);
}
