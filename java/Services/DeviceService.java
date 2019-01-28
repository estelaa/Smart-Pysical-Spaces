package Services;

import Model.Device;
import Model.DeviceCapture;
import Model.Infrastructure;
import Model.Space;
import Repository.DeviceDao;
import Repository.InfrastructureDao;
import Repository.SpaceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DeviceService extends ReaderService {

    @Autowired private DeviceDao deviceDao;
    private SpaceDao spaceDao = new SpaceDao();


    public List<Device> getDevices(){
        return deviceDao.getListAllDevice();
    }

    public boolean createTableDevice(MultipartFile multipartFile) {
        /*Aquest metode li arriba el file.csv amb la info de la taula infrastructures, i omple la taula a la db*/
        List<String> lines = convertMultipartToList(multipartFile);
        InfrastructureDao infraDao = new InfrastructureDao();
        List<Infrastructure> listInfras = infraDao.getListAllInfrastructure();

        for(String lineTmp: lines){
            //Posem en un array tots els parametres de cada linia
            int id = deviceDao.generateId();
            String[] content = lineTmp.split(",");      //(name,idspace_p,idspace_n,door_counter,room_counter,virtual_counter)
            Device device = new Device();
            device.setId(id);
            device.setName(content[0]);                          //content[0] = name

            String idspace_p = content[1];                         //content[1] = idspace_p
            for(Infrastructure infraTmp: listInfras) {
                if(infraTmp.getName().equals(idspace_p)){
                    device.setIdspace_p(infraTmp.getId());          //Podem tenir errors si no esta ben escrit el csv
                }
            }

            String idspace_n = content[2];                         //content[2] = idspace_n
            boolean virtual = true;
            for(Infrastructure infraTmp: listInfras) {
                if(infraTmp.getName().equals(idspace_n)){
                    device.setIdspace_n(infraTmp.getId());          //Podem tenir errors si no esta ben escrit el csv
                    virtual = false;
                }
            }if(virtual){
                //FENT PROVES DE DEVICE VIRTUAL
                String iddevice = content[2].split("/")[1];
                List<Device> listDevice = deviceDao.getListAllDevice();
                for(Device deviceTmp: listDevice) {
                    if(deviceTmp.getName().equals(iddevice)){
                        device.setIdspace_n(deviceTmp.getId());
                    }
                }
            }

            if (content[3].equals("0")) {                             //content[3] = door_counter
                device.setDoor_counter(false);
            } else {
                device.setDoor_counter(true);
            }

            if (content[4].equals("0")) {                             //content[4] = room_counter
                device.setRoom_counter(false);
            } else {
                device.setRoom_counter(true);
            }

            if (content[5].equals("0")) {                             //content[5] = spot_counter
                device.setVirtual_counter(false);
            } else {
                device.setVirtual_counter(true);
            }

            if(!deviceDao.insertDevice(device)){      //fem el insert a la DB
                return false;
            }
        }
        return true;
    }

    public boolean insertDevice(Device device) {
        device.setId(deviceDao.generateId());
        return deviceDao.insertDevice(device);
    }

    public boolean UpdateCCByDevice(List<DeviceCapture> listCapture){

        for(DeviceCapture deviceCapture:listCapture){
            int iddevice = deviceCapture.getIddevice();
            int capture = deviceCapture.getCapture();
            Device device = deviceDao.getDevice(iddevice);
            int old_capture = device.getOld_capture();
            Space spaceP = spaceDao.getSpace(device.getIdspace_p());
            Space spaceN = spaceDao.getSpace(device.getIdspace_n());
            int dif = capture - old_capture;
            int newCC = 0;

            if(device.getDoor_counter()){
                //al spaceP li sumarem la dif de voltes que aporti el contador desde la ultima lectura
                newCC = spaceP.getCapacitycurrent() + dif;
                spaceDao.updateCapacityCurrent(spaceP.getId(),newCC);
                //al spaceN li resterem la dif de voltes que aporti el contador desde la ultima lectura
                newCC = spaceN.getCapacitycurrent() - dif;
                spaceDao.updateCapacityCurrent(spaceN.getId(),newCC);
                if(!deviceDao.updateOldCapture(iddevice,capture)){   //Actualitzem el valor de old_capture per la seguent lectura
                    return false;
                }
            }else if(device.getRoom_counter()){
                //al spaceP li sumarem la dif de voltes que aporti el contador desde la ultima lectura
                newCC = spaceP.getCapacitycurrent() + dif;
                spaceDao.updateCapacityCurrent(spaceP.getId(),newCC);
                if(!deviceDao.updateOldCapture(iddevice,capture)){   //Actualitzem el valor de old_capture per la seguent lectura
                    return false;
                }
            }else if(device.getVirtual_counter()){

                //DE MOMENT UTILITZAT COM A DEVICE VIRTUAL FENT PROVES
                int iddeviceVirtual = device.getIdspace_n();    //idspace_n en realtiat es el id del device que volem virtualitzar
                int captureV = deviceDao.getDevice(iddeviceVirtual).getOld_capture();//Utilitzem la old_capture del device que volem virtualitzar
                old_capture = device.getOld_capture();
                dif = captureV - old_capture;
                if(capture>0) {                                     //SI EL VALOR DE CAPTURE ES POSITIU SUMEM
                    newCC = spaceP.getCapacitycurrent() + dif;
                    spaceDao.updateCapacityCurrent(spaceP.getId(), newCC);
                }else if(capture<0){                                //SI EL VALOR DE CAPTURE ES NEGATIU RESTEM
                    newCC = spaceP.getCapacitycurrent() - dif;
                    spaceDao.updateCapacityCurrent(spaceP.getId(), newCC);
                }
                if(!deviceDao.updateOldCapture(iddevice,captureV)){   //Actualitzem el valor de old_capture per la seguent lectura
                    return false;
                }

            }else{
                //TORNAR A PENSAR
                //APLICAR EL NON_DEVICE
            }
        }
        return true;
    }




}
