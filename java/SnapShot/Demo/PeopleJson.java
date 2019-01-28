package SnapShot.Demo;

import Model.People;
import Services.ReaderService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeopleJson extends ReaderService {
    /*PER FER PROVES*/

    private List<People> listPeople;

    public PeopleJson() {
        listPeople = readerPeople() ;
    }

    public List<People> getListPeople() {
        return listPeople;
    }

    public void setListPeople(List<People> listPeople) {
        this.listPeople = listPeople;
    }

    public List<People> readerPeople(){
        List<People> listPeople = new ArrayList<>();
        List<String> lines= new ArrayList<>();

        File file = new File("peopleCapture.txt");
        lines = convertFileToList(file);

        for(String lineTmp: lines) {
            String[] content = lineTmp.split(",");      //(id, idroom)
            People people = new People();
            people.setId(Integer.parseInt(content[0]));
            people.setName("");
            people.setIdspace(Integer.parseInt(content[1]));
            listPeople.add(people);
        }

        return listPeople;
    }


}
