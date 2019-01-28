package Services;

import Model.RuleEngine;
import Model.Space;
import Repository.InfrastructureDao;
import Repository.RuleEngineDao;
import Repository.SpaceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RuleEngineServices {

    @Autowired SpaceService spaceService;
    RuleEngineDao ruleEngineDao = new RuleEngineDao();
    SpaceDao spaceDao = new SpaceDao();
    InfrastructureDao infrastructureDao = new InfrastructureDao();

    protected final static Logger logger = Logger.getLogger(String.valueOf(RuleEngine.class));

    public RuleEngineServices() {

    }

    public boolean addNewRule(RuleEngine ruleEngine) {
        //check if there the option is day, week or month:
        int id = ruleEngineDao.generateId();
        ruleEngine.setId(id);
        boolean adding = ruleEngineDao.insertRuleEngine(ruleEngine);
        checkIfIsOpen(ruleEngine);
        return adding;
    }

    //change the open to false if the ruleEngine makes a space be closed.
    public boolean checkIfIsOpen(RuleEngine ruleEngine) {
        DateFormat formatDate = new SimpleDateFormat("M/dd/yy HH:mm");
        Date currentDate = new Date(); //Hora actual
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);  //Creem el objecte Calendar per poder compara hores

        Space space = spaceDao.getSpace(ruleEngine.getIdspace());
        try {
            Date endDate = formatDate.parse(ruleEngine.getEnddate()+" "+ruleEngine.getEndtime());
            Date startDate = formatDate.parse(ruleEngine.getStartdate()+" "+ruleEngine.getStarttime());
            space.setOpen(true);
            spaceService.modCapAllowed(space);
            int capacityAllowed = infrastructureDao.getInfrastructure(space.getId()).getLocalcapacity(); //REINICIAR CAPAITYALLOWED
            switch (ruleEngine.getOption()){ //Mirem quina es l'opcio del RuleEngine per saber com tractar la data
                case "day":
                    if(isBetween(currentDate, startDate, endDate)) {
                        space.setOpen(false);
                        capacityAllowed=0;
                    }
                    break;
                case "week":
                    if(isBetween(currentDate,startDate,endDate)){
                        if(Integer.parseInt(ruleEngine.getWeekday())==c.get(Calendar.DAY_OF_WEEK)){ //DAY OF WEEK[0-6] 0=Sunday (el weekDay = dia de la setmana)
                            space.setOpen(false);
                            capacityAllowed=0;
                        }
                    }
                    break;
                case "month": //PER ARA NO S'UTILITZA AQUESTA OPCIO
                    if(isBetween(currentDate,startDate,endDate)){
                        if((Integer.parseInt(ruleEngine.getWeekday()))==c.get(Calendar.DAY_OF_MONTH)){
                            space.setOpen(false);
                            capacityAllowed=0;

                        }
                    }
                    break;
            }
            space.setCapacityallowed(capacityAllowed);
            spaceService.modCapAllowed(space);
            logger.log(Level.INFO,"The capacityAllowed has been change:" +capacityAllowed+" and "+ space.getName() + "is open:" + space.getOpen());
            spaceDao.updateOpen(space.getId(),space.getOpen());
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        return space.getOpen();
    }
    //Returns all the rules from the nameSpace
    public List<RuleEngine> getAllRulesFrom(int idSpace) {
        List<RuleEngine> ruleEnginesOfSpace = ruleEngineDao.getRuleEngineByIdSpace(idSpace);
        return ruleEnginesOfSpace;
    }

    //for every RuleEnggine check if makes a space be closed
    public boolean checkIfSpaceIsOpen(int id) {
        Space space = spaceDao.getSpace(id);
        ArrayList<RuleEngine> ruleEngines = (ArrayList<RuleEngine>) ruleEngineDao.getRuleEngineByIdSpace(space.getId());
        boolean isopen = space.getOpen();
        if(ruleEngines.size()==0||ruleEngines==null){
            spaceDao.updateOpen(space.getId(),true);
            space.setCapacityallowed(infrastructureDao.getInfrastructure(space.getId()).getLocalcapacity()); //REINICIAR CAPAITYALLOWED
            spaceService.modCapAllowed(space);
        }
        for (RuleEngine ruleEngineTemp:ruleEngines) {
            isopen = checkIfIsOpen(ruleEngineTemp);
        }
        return isopen;
    }

    public boolean deleteRule(Integer id) {
        return ruleEngineDao.deleteRuleEngineById(id);
    }

    public boolean isBetween(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)){
                if(isBetweenHour(date,dateStart,dateEnd))return true;
            }
        }
        return false;
    }

    private boolean isBetweenHour(Date currentDate,Date startDate, Date endDate){
        //Camviem el format de les tres dates per poder despres compara la hora de cada una
        String date1 = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentDate);
        String date2 = DateFormat.getTimeInstance(DateFormat.SHORT).format(startDate);
        String date3 = DateFormat.getTimeInstance(DateFormat.SHORT).format(endDate);
        DateFormat formatDate = new SimpleDateFormat("HH:mm"); //Volem solament la hora per poder compara correctament.
        // El dia, mes i l'any, ens el posara per defecte; 01/01/1970
        try {
            Date date11 = formatDate.parse(date1);
            Date date22 = formatDate.parse(date2);
            Date date33 = formatDate.parse(date3);
            if(date11.after(date22)&&date11.before(date33)){
                return true;
            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
