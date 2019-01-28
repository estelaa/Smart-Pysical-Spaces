package Services;

import Model.Infrastructure;
import Repository.InfrastructureDao;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Service
public class InfrastructureService extends ReaderService {

    protected final static Logger logger = Logger.getLogger(String.valueOf(UserService.class));

    private InfrastructureDao infrastructureDao = new InfrastructureDao();

    public boolean createTableInfra(MultipartFile multipartFile){
        /*Aquest metode li arriba el file.csv amb la info de la taula infrastructures, i omple la taula a la db*/
        List<String> lines = convertMultipartToList(multipartFile);
        int id;
        HashMap<String, Integer> mapId = new HashMap();

        for(String lineTmp: lines){
            //Split each line into comas (name, capacityMax, dependence, modifiable, localCapacity)
            //Posem en un array tots els parametres de cada linia
            id = infrastructureDao.generateId();
            String[] content = lineTmp.split(",");      //(name, capacitymax, dependence)

            Infrastructure infra = new Infrastructure();
            //infra.setId(id);
            infra.setName(content[0]);                          //content[0] = name
            infra.setCapacitymax(Integer.parseInt(content[1])); //content[1] = capacitymax

            //Fer un hasMap per guardar els id amb la key
            if (mapId.containsKey(content[2])) {                //content[2] = dependence
                infra.setDependence((int) mapId.get(content[2]));
            }else{
                //QUE PASSA SI NO ESTA MOLT IMPORTANT EL ORDRE AMB QUE ENTRA EL CSV
            }
            if (infra.getCapacitymax()==0) {
                infra.setModifiable(false);
            } else {
                infra.setModifiable(true);
            }
            infra.setLocalcapacity(infra.getCapacitymax());
            if(!infrastructureDao.insertInfrastructure(infra)){
                return false;
            }
            mapId.put(content[0], id);
        }
        return true;
    }

    public boolean insertInfrastructure(Infrastructure infrastructure) {
        infrastructure.setId(infrastructureDao.generateId());
        return infrastructureDao.insertInfrastructure(infrastructure);
    }

    public List<Infrastructure> getListAllInfrastructure(){
        return infrastructureDao.getListAllInfrastructure();
    }


}
