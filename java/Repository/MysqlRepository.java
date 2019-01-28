package Repository;


import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;


public class MysqlRepository<T> {

    protected final static Logger logger = Logger.getLogger(String.valueOf(MysqlRepository.class));
    private final static String FILENAME = "C:\\Users\\estel\\Documents\\mysql-database.txt";

    private Connection getConnection(){
        Connection con= null;
        try{
            String JDBC_DRIVER ="com.mysql.jdbc.Driver";
            String DB_URL = "jdbc:mysql://localhost:6800/pae";
            Properties properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "root");
            properties.setProperty("useSSL", "false");
            properties.setProperty("serverTimezone","UTC");
            Class.forName(JDBC_DRIVER);
            con= DriverManager.getConnection(DB_URL,properties);
        }catch (Exception e){
            System.out.println(e);
        }
        return con;
    }

    List<T> select(String query, T t){
        Connection connection = getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();
            List<T> tList = getMap(t,rs);
            preparedStatement.close();
            connection.close();
            return tList;
        }catch (SQLException sqle){
            logger.info(sqle.getMessage());
            return null;
        }
    }

    protected boolean executeQuery(String query){
        Connection connection = getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
            return true;
        } catch (SQLException sqle) {
            logger.info(sqle.getMessage());
            return false;
        }
    }

    private List<T> getMap(T t ,ResultSet resultSet){
        List<T> list = new ArrayList<>();
        Field[] tField = t.getClass().getDeclaredFields();
        try{
            T temp;
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData(); //
            while(resultSet.next()){
                temp = (T) t.getClass().newInstance();
                for(int i=1; i<= resultSetMetaData.getColumnCount(); i++){
                    String columnName = resultSetMetaData.getColumnName(i); //Extreu el nom de la columna de la taula
                    String columnType = resultSetMetaData.getColumnTypeName(i); //Extreu el tipus de la columna de la taula
                    for (int j=0; j<tField.length; j++){
                        if(columnName.equals(tField[j].getName())){ //Compara el nom de la variable
                            Object var = getConvert(columnType, resultSet, i); //Converteix el String al tipus de la variable
                            if(var!=null){
                                Method method = temp.getClass().getMethod(setMethodName(tField[j].getName()),tField[j].getType());
                                method.invoke(temp,var); //invocar al metode set de la variable
                            }
                        }
                    }
                }
                list.add(temp);
            }
            return list;
        }catch (Exception e){
            logger.info("Error en la Ejecucion MySQLRepository.getMap(): " + e.getMessage());
            return null;
        }
    }


    private Object getConvert(String Type, ResultSet resultSet, int index){ //converteix variables tipus sql a variables tipus java
        try {
            switch (Type) {
                case "VARCHAR":
                    return resultSet.getString(index);
                case "INT":
                    return resultSet.getInt(index);
                case "TINYINT":
                    return resultSet.getBoolean(index);
                default:
                    return null;
            }
        }catch (SQLException sqle){
            logger.info("Error en el MySQLRepository.getConvert: "+ sqle.getMessage());
            return null;
        }
    }

    protected String setMethodName(String name){ //Genera un string setName
        String tmp = name.substring(0,1).toUpperCase();
        return "set"+tmp+name.substring(1);
    }

    protected String getMethodName(String name){ //genera un string getName
        String tmp = name.substring(0,1).toUpperCase();
        return "get"+tmp+name.substring(1);
    }
}
