package Repository;

import Model.Alert;
import Repository.Interfaces.IGenericDao;

import java.util.*;

public class AlertDao {

    private IGenericDao<Alert> genericDao;

    public AlertDao() {
        this.genericDao = new GenericDao<Alert>();
    }

    public boolean insertAlert(String description){
        Date date = new Date();
        Alert alert = new Alert(date.toString(), description);
        return genericDao.insert(alert);
    }

    public List<Alert> getListAllAlerts(){
        return genericDao.selectAll(new Alert());
    }

    public boolean deleteAlert(Alert alert){
        return genericDao.delete(alert, "date", alert.getDate(), "String" );
    }


}
