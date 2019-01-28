package Repository.Interfaces;

import Model.RuleEngine;

import java.util.HashMap;
import java.util.List;

public interface IRuleEngineDao {

    public boolean insertRuleEngine(RuleEngine ruleEngine);
    public int generateId();
    public RuleEngine getRuleEngine(int id);
    public List<RuleEngine> getAllRuleEngine();
    public List<RuleEngine> getRuleEngineByIdSpace(int idSpace);
    public boolean deleteRuleEngineById(int id);


}
