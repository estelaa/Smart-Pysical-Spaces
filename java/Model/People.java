package Model;

public class People {

    private int id; //iduser
    private String name;    //username o name del user
    private int idspace; //idspace on el localitzem

    public People(){}

    public People(int id, String name, int idspace) {
        this.id = id;
        this.name = name;
        this.idspace = idspace;
    }

    public People(int id, int idspace){
        this.id = id;
        this.idspace = idspace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdspace() {
        return idspace;
    }

    public void setIdspace(int idspace) {
        this.idspace = idspace;
    }
}
