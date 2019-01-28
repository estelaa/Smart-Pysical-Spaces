package Repository;

import Model.Occupancy;
import Model.Space;

import java.util.Date;
import java.util.List;

public class OccupancyDao {
    private GenericDao genericDao;

    public OccupancyDao() {
        this.genericDao = new GenericDao< Occupancy>();
    }

    public boolean insertOccupancy(Space space){
        Date date = new Date();
        Occupancy occupancy = new Occupancy(space.getId(), date.toString(), space.getDcc()); //Millor amb la DCC que LA CC per controlar el edifici
        return genericDao.insert(occupancy);
    }

    public List<Occupancy> listOccupancy(){
        return genericDao.selectAll(new Occupancy());
    }

    //public boolean updateOccupancy(){

    //}

}
