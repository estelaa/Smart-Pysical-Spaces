package Services;

import Model.*;
import Repository.*;
import SnapShot.Demo.SuperJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PlannerService {

    @Autowired
    SpaceService spaceService;
    @Autowired
    PeopleService peopleService;
    @Autowired
    DeviceService deviceService;
    private SpaceDao spaceDao = new SpaceDao();
    private InfrastructureDao infrastructureDao = new InfrastructureDao();
    private DeviceDao deviceDao = new DeviceDao();
    private OccupancyDao occupancyDao = new OccupancyDao();
    private BuildingDao buildingDao = new BuildingDao();
    private AlertsService alertsService = new AlertsService();


    private HashMap<Integer, Infrastructure> hashInfra;
    private HashMap<Integer, Space> hashSpaces;
    private static HashMap<Integer, SpaceAux> hashSpaceAux = new HashMap<>();

    public boolean init(){
        //FENT PROVES
        this.hashInfra = infrastructureDao.getAllInfrastructures();  //omplim el hashMap de Infra
        this.hashSpaces = spaceDao.getAllSpaces();                   //omplim el hashMap de Space
        this.hashSpaceAux = createScapesAux();                  //Creem el hashMap amb els SpacesAux
        calculateDepSpacesAux(hashSpaceAux);//Calculem les Dependencies Superiors i Inferiors dels SpacesAux
        HashMap<Integer,Device> hashDevices= deviceDao.getAllDevice();
        for (Device device: hashDevices.values()) {
            deviceDao.updateOldCapture(device.getId(),0);
        }
        if(!recalculateDCA()){                               //Recalculem els Space
            return false;                                       //Si falla return false
        }
        return reinicar();
    }

    public boolean reinicar(){
        hashSpaces = spaceDao.getAllSpaces();
        HashMap<Integer,Device> hashDevices = deviceDao.getAllDevice();
        for(Space space: hashSpaces.values()){
            space.setCapacitycurrent(0);
            space.setDcc(0);
            spaceDao.updateSpace(space);
        }

        StringBuilder stringDevice = new StringBuilder();
        for (Device device: hashDevices.values()){
            deviceDao.updateOldCapture(device.getId(),0);
        }
        return true;
    }

    public List<SpaceAux> getListSpaceAux(){
        List<SpaceAux> spaceList = new ArrayList<>();
        for(SpaceAux spaceAuxTmp: hashSpaceAux.values()){
            spaceList.add(spaceAuxTmp);
        }
        return spaceList;
    }


    public boolean superUpdatePlanner(SuperJson superJson){
        //Aquest metode rep un Json amb dos List una de <DeviceCapture> i una altre de <People>
        List<DeviceCapture> captureList = superJson.getListDevice();
        List<PeopleCapture> peopleCaptureList = superJson.getListPeople();
        if (deviceService.UpdateCCByDevice(captureList)) {   //Executa el Request cap a la API i agafa el resultat.
            if(peopleService.updatePeople(peopleCaptureList)) {
                return recalculateDCC();
            }
        }
        return false;
    }



    public HashMap<Integer, SpaceAux> createScapesAux() {
    /*
    Crearem un SpaceAux per cada Space amb el mateix id
    */
        HashMap<Integer, SpaceAux> listSpaceAxu = new HashMap<>();
        for (Space spaceTmp : hashSpaces.values()) {
            SpaceAux spaceAux = new SpaceAux(spaceTmp.getId());
            listSpaceAxu.put(spaceTmp.getId(), spaceAux);
        }
        return listSpaceAxu;
    }
    public HashMap<Integer, SpaceAux> calculateDepSpacesAux(HashMap<Integer, SpaceAux> hashSpaceAux) {

    /*
    A cada SpaceAux li posem dos arrays: un amb les seves idsDependencesSup i un altre amb les seves idsDependencesInf
    Per printar el esquema de arbre a la web i fer el bucles molt mes facils.
    */

        hashInfra = infrastructureDao.getAllInfrastructures();                        //obtenim la ultima versio de la DB
        hashSpaces = spaceDao.getAllSpaces();                                         //Dels dos hashMaps Spaces i Infras

        for (Infrastructure infraTmp : hashInfra.values()) {                                //iterem els values de hashInfra
            SpaceAux spaceAux =  hashSpaceAux.get(infraTmp.getId());                           //obtenim el SpaceAux amb el mateix id que infraTmp
            List<Integer> listDepTmp = spaceAux.getIdsDependenceSup();                          //obtenim la list de Dep del SpaceAux

            while (infraTmp.getId() != infraTmp.getDependence()) {          //Iterem fins arribar a dalt del "arbre" id==idDep
                listDepTmp.add(infraTmp.getDependence());                   //Afegim la dependencia a la list
                infraTmp = hashInfra.get(infraTmp.getDependence());         //Canviem la InfraTmp per la seva dependencia superior
            }
            spaceAux.setIdsDependenceSup(listDepTmp);                         //Guardem el SpaceAux la list amb les ids de les seves dependencies
            hashSpaceAux.put(spaceAux.getIdspace(),spaceAux);                    //Guardem el SpaceAux en el hashMap del PLANER SERVICE
        }

        for(Space spaceTmp: hashSpaces.values()){                                //Iterem tota la llista de Space
            int idDepSup = spaceTmp.getDependence();                                //obtenim la idDepSup
            if(idDepSup!=spaceTmp.getId()){
                SpaceAux spaceAuxTmp = hashSpaceAux.get(idDepSup);                     //obtenim el spaceAux de la DepSup
                List<Integer> listDepInf = spaceAuxTmp.getIdsDependenceInf();                //obtenim la list de DepInf
                listDepInf.add(spaceTmp.getId());                                    // li apuntem la id del spaceTmp a la list
                spaceAuxTmp.setIdsDependenceInf(listDepInf);                    //guardem la list al spaceTmp
                hashSpaceAux.put(spaceAuxTmp.getIdspace(),spaceAuxTmp);              //guardem el spaceAuxTmp al hashMap
            }
        }
        for(Space spaceTmp: hashSpaces.values()) {
            SpaceAux spaceAux = hashSpaceAux.get(spaceTmp.getId());
            spaceDao.updateSpace(spaceTmp);                                     //Actualitzem la DB
        }

        return hashSpaceAux;
    }

    public boolean updateDB(Space space){
        /*Aquest Metode fa el update de cada space a la DB i comproba la ocupacio de cada un i crea alertas si es necessari*/
        if(space.getName().equals("building")){
            boolean result = occupancyDao.insertOccupancy(space);
        }
        if(space.getDcc()>space.getDca()){
            //LLENÇAR WARNING SOBREOCUPACIO
            Alert alert = new Alert();
            alert.setDescription(space.getName() + " Te una sobreocupacio de " + 100*space.getDcc()/space.getDca() + "%");
            if(!alertsService.insertAlert(alert.getDescription())) { //Aquesta alerta es per la nostre DB
                return false;
            }
            if(space.getName().equals("stair")){
                for(Building building: buildingDao.getListAllBuilding()){
                    String postUrl = "http://" + building.getIp() + ":" + building.getPort() + "/alertNeighbor";
                    if(!alertsService.sendPOST(alert,postUrl)) {  //Aquesta alerta es per enviar a un edifici "proper"
                        return false;
                    }
                }
            }

        }
        if (!spaceDao.updateSpace(space)) {    //Actualitzem la DB
            return false;                       //si falla algun update retornem false
        }
        return true;
    }

    public boolean recalculateDCC() {
        HashMap<Integer, Space> hashSpace = spaceDao.getAllSpaces();
        int dcc = 0;
        //INICIALITZEM LES DCC
        for (Space space : hashSpace.values()) {
            if (space.getCapacityallowed() == 0) {
                space.setDcc(0);    //Si CA = 0 iniciem DCC = 0
            } else {
                space.setDcc(space.getCapacitycurrent());
            }
        }

        for (Space space : hashSpace.values()) {
            //CALCUL DE DCC
            if (space.getCapacityallowed() != 0) {
                SpaceAux spaceAux = hashSpaceAux.get(space.getId());
                for (Integer idDepSup : spaceAux.getIdsDependenceSup()) { //Iterem totes les dependencies superiors utilitzan els SpacesAux
                    Space spaceTmp = hashSpace.get(idDepSup);
                    dcc = spaceTmp.getDcc();
                    dcc = dcc + space.getCapacitycurrent();        //Sumem la CC del Space a totes les seves depSup
                    spaceTmp.setDcc(dcc);
                    hashSpace.put(spaceTmp.getId(), spaceTmp);
                }
            }
        }
        //ACTUALITZEM LA DB
        for (Space space : hashSpace.values()) {
            if (!updateDB(space)) {    //Actualitzem la DB
                return false;                       //si falla algun update retornem false
            }
        }
        return true;
    }
    public boolean recalculateDCA(){
        HashMap<Integer, Space> hashSpace = spaceDao.getAllSpaces();
        //INICIALITZEM LES DCA
        for(Space space:hashSpace.values()){
            space.setDca(space.getCapacityallowed());
        }

        for(Space space:hashSpace.values()) {
            //CALCUL DE DCA
            if (space.getId() != space.getDependence()) {                  //comprovem que te dependencia
                if (space.getCapacityallowed() > 1) {                    //Nomes aumentem la DCA de les superiors si CA>1 per no duplicar  la CA dels "pcs"
                    boolean exit = false;
                    Space spaceTmp = hashSpace.get(space.getDependence());     //El spaceTmp sera la dependenciaSUP del space
                    while (!exit) {
                        int dcaTmp = spaceTmp.getDca() + space.getCapacityallowed();               //calculem la nova dca
                        spaceTmp.setDca(dcaTmp);                                   //li assignem aquesta dca
                        hashSpace.put(spaceTmp.getId(), spaceTmp);             //Actualitzem el hashMap
                        if ((spaceTmp.getDependence() == spaceTmp.getId())) {
                            exit = true;                                       //Si el id==idDep sortim del while
                        }
                        spaceTmp = hashSpace.get(spaceTmp.getDependence());    //canviem spaceTmp per la dependencia superior
                    }
                }
            }
        }
        //ACTUALITZEM LA DB
        for(Space spaceTmp: hashSpace.values()) {
            if(!spaceDao.updateSpace(spaceTmp)){    //Actualitzem la DB
                return false;                       //si falla algun update retornem false
            }
        }
        return true;
    }
}
