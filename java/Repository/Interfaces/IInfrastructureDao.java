package Repository.Interfaces;


import Model.Infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IInfrastructureDao {

    public boolean create();
    public boolean insertInfrastructure(Infrastructure infrastructure);
    public int generateId();
    public HashMap< Integer , Infrastructure> getAllInfrastructures();
    public List<Infrastructure> getListAllInfrastructure();
    public int getMaxCapacityById(int id);
    public Infrastructure getInfrastructure(int id);
    public boolean removeInfrastructure(Infrastructure infrastructure);
    public boolean updateInfrastructure(Infrastructure infrastructure);
    public boolean isExist(Infrastructure infrastructure);
    public ArrayList<Integer> getSDependencies(int id);


}
