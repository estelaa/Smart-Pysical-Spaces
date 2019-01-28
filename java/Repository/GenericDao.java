package Repository;

import Model.Space;
import Repository.Interfaces.IGenericDao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class GenericDao<T> extends MysqlRepository<T> implements IGenericDao<T> {

    /*La clase GenericDao s'encarrega de crear els Strings(query) en format SQL,
    per afegir, borrar, extreure,... de la base de dades */

    @Override
    public List<T> selectAll(T t) {
        Class nameClass = t.getClass();
        String nameTable = nameClass.getSimpleName().toLowerCase();
        String query = ("SELECT * FROM "+ nameTable);
        GenericDao.logger.info(query);
        return select(query,t);
    }

    @Override
    public List<T> select(List<String> column, T t) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        for (String temp: column) {
            query.append(temp+", ");
        }
        query.delete(query.length()-2,query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
        query.append(" FROM "+t.getClass().getSimpleName().toLowerCase());
        GenericDao.logger.info(query.toString());
        return select(query.toString(),t);
    }

    @Override
    public List<T> selectByCondition(List<String> column, String condition, String conditionValue, T t) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        for (String temp: column){
            query.append(temp+", ");
        }
        query.delete(query.length()-2,query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
        query.append(" FROM "+ t.getClass().getSimpleName().toLowerCase()+" WHERE ");
        query.append(condition+"='"+conditionValue+"'");
        GenericDao.logger.info(query.toString());
        return select(query.toString(),t);
    }

    @Override
    public List<T> selectAllByCondition(String condition, String conditionValue, T t) {
        StringBuilder query= new StringBuilder();
        query.append("SELECT * FROM "+ t.getClass().getSimpleName()+" WHERE (");
        query.append(condition+" = '"+conditionValue+"'");
        query.append(" )");
        GenericDao.logger.info(query.toString());
        return select(query.toString(),t);
    }


    @Override
    public boolean insertAll(List<String> value,List<String> type,T t) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO "+t.getClass().getSimpleName()+" VALUES ");
        int i = 0;
        for (String valueTmp: value) {
            query.append("'"+booleanConvert(valueTmp,type.get(i))+"', ");
            i++;
        }
        query.delete(query.length()-2,query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
        query.append(")");
        GenericDao.logger.info(query.toString());
        return executeQuery(query.toString());
    }

    @Override
    public boolean insert(T t) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO " + t.getClass().getSimpleName().toLowerCase() + " (");
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field fieldTmp : fields) {
                query.append(fieldTmp.getName() + ", ");
            }
            query.delete(query.length()-2,query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
            query.append(") VALUES (");
            Method[] methods = t.getClass().getMethods();
            for (Field fieldTmp : fields) {
                for (Method methodTmp : methods) {
                    if (methodTmp.getName().equals(getMethodName(fieldTmp.getName()))) {
                        Object var = methodTmp.invoke(t);
                        if(var!=null) {
                            query.append("'" + booleanConvert(String.valueOf(var),String.valueOf(fieldTmp.getGenericType())) + "', ");
                        }else{
                            query.append("'', ");
                        }
                    }
                }
            }
            query.delete(query.length()-2,query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
            query.append(")");
            GenericDao.logger.info(query.toString());
            return executeQuery(query.toString());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    public boolean delete(T t , String condition, String conditionValue, String conditionType) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM " + t.getClass().getSimpleName().toLowerCase()+ " WHERE ");
        query.append(condition+" = '"+booleanConvert(conditionValue,conditionType)+"'");
        GenericDao.logger.info(query.toString());
        return executeQuery(query.toString());
    }

    @Override
    public boolean updateByCondition(HashMap<String,String> columnValue, List<String> typeValue,String condition, String conditionValue, String conditionType, T t) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE "+t.getClass().getSimpleName().toLowerCase()+" SET ");
        Iterator iteratorColumnValue = columnValue.entrySet().iterator();
        int i=0;
        while (iteratorColumnValue.hasNext()){
            HashMap.Entry temp = (HashMap.Entry)iteratorColumnValue.next();
            query.append(temp.getKey()+" = '"+booleanConvert((String) temp.getValue(),typeValue.get(i))+"', ");
            i++;
        }
        query.delete(query.length()-2,query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
        query.append(" WHERE ");
        query.append(condition+" = '"+booleanConvert(conditionValue,conditionType)+"'");
        GenericDao.logger.info(query.toString());
        return executeQuery(query.toString());
    }

    private String booleanConvert(String value, String type){
        if(type.equals("boolean")){
            if(value.equals("true")) return "1";
            if(value.equals("false")) return "0";
        }
        return  value;
    }

    public List<String> getListTypeAtributs(){
        List<String> list = new ArrayList<>();
        Space space = new Space();
        Field[] tField = space.getClass().getDeclaredFields();
        for (int j=0; j<tField.length; j++){
            list.add(String.valueOf(tField[j].getGenericType()));
        }
        return list;
    }

    @Override
    public boolean insertB(List<String> column, T t, List<String> values) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO " + t.getClass().getSimpleName().toLowerCase() + " (");

            for (String temp : column) {
                query.append(temp + ", ");
            }
            query.delete(query.length() - 2, query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
            query.append(") VALUES (");

            for (String temp : values) {
                query.append(temp + ", ");
            }
            query.delete(query.length() - 2, query.length()); //eliminar la ultima ',' de la query perque el SQL sigui correcte
            query.append(")");
            GenericDao.logger.info(query.toString());
            return executeQuery(query.toString());
        } catch (Exception e) {
            return false;
        }

    }
}
