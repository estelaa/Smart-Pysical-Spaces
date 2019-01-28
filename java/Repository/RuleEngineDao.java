package Repository;

import Model.Device;
import Model.RuleEngine;
import Repository.Interfaces.IGenericDao;
import Repository.Interfaces.IRuleEngineDao;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
public class RuleEngineDao implements IRuleEngineDao {

    IGenericDao<RuleEngine> genericDao = new GenericDao<>();

    @Override
    public boolean insertRuleEngine(RuleEngine ruleEngine) {
        return genericDao.insert(ruleEngine);
    }

    @Override
    public int generateId() {
        List<RuleEngine> ruleEngineList = getAllRuleEngine();
        List<Integer> allId = new ArrayList<>();
        if(ruleEngineList!=null) {
            for (RuleEngine ruleEngine : ruleEngineList) {
                allId.add(ruleEngine.getId());
            }
        }
        if(!allId.isEmpty()){
            return Collections.max(allId)+1;
        }else{
            return 0;
        }
    }

    @Override
    public RuleEngine getRuleEngine(int id) {
        List<String> column = getListAtributsRuleEngine();
        String condition = "id";
        String conditionValue = String.valueOf(id);
        List<RuleEngine> list = genericDao.selectByCondition(column,condition,conditionValue, new RuleEngine());
        if(list.size()==1){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<RuleEngine> getAllRuleEngine() {
        List<RuleEngine> ruleEngines= genericDao.selectAll(new RuleEngine());
        return ruleEngines;
    }

    @Override
    public List<RuleEngine> getRuleEngineByIdSpace(int idSpace) {
        List<String> column = getListAtributsRuleEngine();
        String condition = "idspace";
        String conditionValue = String.valueOf(idSpace);
        List<RuleEngine> list = genericDao.selectByCondition(column,condition,conditionValue, new RuleEngine());
        return list;
    }

    @Override
    public boolean deleteRuleEngineById(int id) {
        RuleEngine ruleEngine = new RuleEngine();
        String conditionValue = String.valueOf(id);
        return genericDao.delete(ruleEngine,"id",conditionValue,typeValue("id"));
    }

    private List<String> getListAtributsRuleEngine(){
        /*
        Crea una List amb tots els noms dels atributs de la classe Device
        */
        List<String> column = new ArrayList();
        RuleEngine ruleEngine = new RuleEngine();
        Field[] nameVariable = ruleEngine.getClass().getDeclaredFields();
        for (Field nameVariabletmp: nameVariable) {
            column.add(nameVariabletmp.getName());
        }
        return column;
    }

    private String typeValue(String value){
        RuleEngine ruleEngine = new RuleEngine();
        Field[] tField = ruleEngine.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
            value.equals(tField[j].getName());
            return String.valueOf(tField[j].getGenericType());
        }
        return null;
    }
}
