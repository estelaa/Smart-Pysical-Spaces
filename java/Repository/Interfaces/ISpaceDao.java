package Repository.Interfaces;

import Model.Space;

import java.util.HashMap;
import java.util.List;

public interface ISpaceDao {

    public boolean insertSpace(Space space);
    public Space getSpace(int id);
    public Space getSpaceByName(String name);
    public HashMap<Integer, Space> getAllSpaces();
    public List<Space> getListAllSpace();
    public boolean updateCapacityCurrent(int id, int capacityCurrent);
    public boolean updateCapacityAllowed(int id, int capacityAllowed);
    public boolean removeSpace(Space space);
    public boolean isExist(Space space);
    public boolean updateOpen(int id, boolean isopen);

}
