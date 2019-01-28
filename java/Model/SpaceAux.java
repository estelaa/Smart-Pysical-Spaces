package Model;

import java.util.ArrayList;
import java.util.List;

public class SpaceAux {

    private int idspace;
    private List<Integer> idsDependenceSup;
    private List<Integer> idsDependenceInf;

    public SpaceAux(int idspace) {
        this.idspace = idspace;
        idsDependenceSup = new ArrayList<>();
        idsDependenceInf = new ArrayList<>();
    }

    public int getIdspace() {
        return idspace;
    }

    public void setIdspace(int idspace) {
        this.idspace = idspace;
    }

    public List<Integer> getIdsDependenceSup() {
        return idsDependenceSup;
    }

    public void setIdsDependenceSup(List<Integer> idsDependenceSup) {
        this.idsDependenceSup = idsDependenceSup;
    }

    public List<Integer> getIdsDependenceInf() {
        return idsDependenceInf;
    }

    public void setIdsDependenceInf(List<Integer> idsDependenceInf) {
        this.idsDependenceInf = idsDependenceInf;
    }
}
