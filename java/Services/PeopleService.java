package Services;

import Model.Device;
import Model.People;
import Model.PeopleCapture;
import Model.User;
import Repository.DeviceDao;
import Repository.PeopleDao;
import Repository.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PeopleService {

    private PeopleDao peopleDao = new PeopleDao();
    private UserDao userDao = new UserDao();
    private DeviceDao deviceDao = new DeviceDao();

    public List<People> getPeople(){
        return peopleDao.getListAllPeople();
    }

    public boolean createPeople(User user) {
        User userTmp = userDao.getUser(user.getUsername());
        People people = new People(userTmp.getId(),userTmp.getName(),0);
        //Entenc que no es poden localitzar a fora de la infrastrucure per tant inicialitzo a 0 que es a dalt de tot del arbre
        return peopleDao.insertPeople(people);
    }

    public boolean updatePeople(List<PeopleCapture> peopleList){
        //El JSON que arriba de la API conte un List<PeopleCapture> (idusuari,iddevice)
        //Aqui es on fem el canvi de iddevice a idspace fent una consulta a la DB
        HashMap<Integer, Device> hashDevice = deviceDao.getAllDevice();

        List<People> allPeople = peopleDao.getListAllPeople(); //Reset people space
        for (People people: allPeople) {
            people.setIdspace(0);
            peopleDao.updateIdSpace(people);
        }

        for(PeopleCapture peopleCaptureTmp: peopleList){
            int idusuari = peopleCaptureTmp.getIdusuari();
            int iddevice = peopleCaptureTmp.getIddevice();
            int idspace  = hashDevice.get(iddevice).getIdspace_p();
            People peopleTmp = new People();
            peopleTmp.setId(idusuari);
            peopleTmp.setIdspace(idspace);
            peopleDao.updateIdSpace(peopleTmp);    //Actualitzem la DB
        }
        return true;

    }

}
