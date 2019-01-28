package Model;


public class RuleEngine {

    private int id;
    private String name;
    private int idspace;

    private boolean periodical;
    private String option;
    private String weekday; //Dilluns - diumenge o del 1 -31 en cas de que l'option sigui month
    private String startdate;
    private String enddate;
    private String starttime;
    private String endtime;

    public RuleEngine(int id, String name ,int idspace, boolean periodical, String option, String weekday, String startdate, String enddate, String starttime, String endtime) {
        this.idspace = idspace;
        this.id = id;
        this.periodical = periodical;
        this.option = option;
        this.weekday = weekday;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuleEngine() {

    }

    public int getIdspace() {
        return idspace;
    }

    public void setIdspace(int idspace) {
        this.idspace = idspace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getPeriodical() {
        return periodical;
    }
    public void setPeriodical(boolean periodical) {
        this.periodical = periodical;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}

