package Services;


import Model.Infrastructure;
import Model.Space;
import Repository.DeviceDao;
import Repository.InfrastructureDao;
import Repository.MysqlRepository;
import Repository.SpaceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SpaceService {

    @Autowired
    PlannerService plannerService;
    private SpaceDao spaceDao = new SpaceDao();
    private InfrastructureDao infrastructureDao = new InfrastructureDao();
    private DeviceDao deviceDao = new DeviceDao();

    protected final static Logger logger = Logger.getLogger(String.valueOf(SpaceService.class));


    public List<Space> getListSpaces(){
        return spaceDao.getListAllSpace();
    }

    public boolean modCapAllowed(Space space){
        HashMap<Integer, Infrastructure> hashInfra = infrastructureDao.getAllInfrastructures();
        if (hashInfra.get(space.getId()).getModifiable()) {     //Comprobem que es modificable
            if (space.getCapacityallowed() <= hashInfra.get(space.getId()).getLocalcapacity()) {//Comprobem que la nova capacitat sigui correcte <localcapacity
                if (!spaceDao.updateCapacityAllowed(space.getId(), space.getCapacityallowed())) {  //Actualitzem a la DB
                    //ERROR DE UPDATE
                    spaceDao.updateOpen(space.getId(),space.getOpen());
                    logger.log(Level.INFO,space.getName() + " has not been updated");
                    return false;
                }else{
                    spaceDao.updateOpen(space.getId(),space.getOpen());
                    return plannerService.recalculateDCA();
                }
            }else{
                //CA > CL NO POT SER
                logger.log(Level.WARNING,"The Space " + space.getName() + " needs to have a capacity lower than " + hashInfra.get(space.getId()).getLocalcapacity());
                return false;
            }
        }
        //NO ES MODIFICABLE
        logger.log(Level.INFO,"The Space " + space.getName() + " can't be modified");
        return false;
    }

    public boolean modCapCurrent(Space space){
        /*PER FER PROVES*/
        if(!spaceDao.updateCapacityCurrent(space.getId(),space.getCapacitycurrent())){
            //SI FALLA EL UPDATE QUE MOSTREM A LA WEB
            logger.log(Level.INFO,space.getName() + " has not been updated");
            return false;
        }
        int CapAllowed = spaceDao.getSpace(space.getId()).getCapacityallowed();
        if(space.getCapacitycurrent() >= CapAllowed) {
            //IMPORTANT
            //SI LA CAPACITY_CURRENT >= CAPACITY_ALLOWED    Lençar warning
            logger.log(Level.WARNING, space.getName() + " has the current capacity greater than the capacity allowed");
        }
        return plannerService.recalculateDCC();
    }

    public boolean isOpen(String nameSpace){
        Space space=spaceDao.getSpaceByName(nameSpace);
        return space.getOpen();
    }


}