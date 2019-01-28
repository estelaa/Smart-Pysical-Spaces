package Services;

import Model.Building;
import Repository.BuildingDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    private BuildingDao buildingDao = new BuildingDao();

    public boolean insertBuilding(Building building){
        building.setId(buildingDao.generateId());       //generem un nou id
        return buildingDao.insertBuilding(building);
    }

    public List<Building> getListBuilding(){
        return buildingDao.getListAllBuilding();
    }
}
