package Repository.Interfaces;

import java.util.HashMap;
import java.util.List;

public interface IGenericDao<T> {

    public List<T> selectAll(T t);
    public List<T> select(List<String> columne, T t);
    public List<T> selectByCondition(List<String> column, String condition, String conditionValue, T t);
    public List<T> selectAllByCondition(String condition, String conditionValue, T t);
    public boolean insert(T t);
    public boolean insertAll(List<String> value, List<String> type,T t);
    public boolean updateByCondition(HashMap<String,String> columnValue, List<String> typeValue,String condition, String conditionValue,String conditionType, T t);
    public boolean createTable();
    public boolean delete(T t, String condition, String conditionValue, String conditionType );
    public boolean insertB(List<String> column, T t, List<String> values);
    public List<String> getListTypeAtributs();
}
