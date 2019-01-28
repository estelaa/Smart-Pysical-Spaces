package SnapShot.Demo;

import Model.*;
import Repository.DeviceDao;
import Repository.InfrastructureDao;
import Repository.PeopleDao;
import Repository.SpaceDao;
import Services.PlannerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GenerateFile {
    private SpaceDao spaceDao = new SpaceDao();
    private PeopleDao peopleDao = new PeopleDao();
    private DeviceDao deviceDao = new DeviceDao();

    StringBuilder stringPeople = new StringBuilder();
    List<People> listAllPeople;



    protected List<String> convertFileToListDemo(File file) {
        List<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void init(){
        stringPeople.append("#iddevice, #idusuari\n");
        listAllPeople = peopleDao.getListAllPeople();
    }
    public void run(int idDevice, int occupancy,boolean last) {
        //Genera el document peopleCapture

        switch (idDevice) {
            case 4: case 5: case 6: case 11: case 12: case 13:
                if(occupancy==1&&listAllPeople.size()>0){
                    double peopleRandom = Math.random() * (listAllPeople.size() - 1);
                    stringPeople.append(idDevice + "," + listAllPeople.get((int) peopleRandom).getId() + "\n");
                    listAllPeople.remove((int)peopleRandom);
                }
                break;
            default:
                break;
        }
        if(last)writeFiles(stringPeople.toString());
    }


    private void writeFiles(String stringPeople) {
        try {
            File filePeople = new File("peopleCapture.txt");
            FileOutputStream fileOutputStreamPeople = new FileOutputStream(filePeople);
            byte[] strPeople = stringPeople.toString().getBytes();
            fileOutputStreamPeople.write(strPeople);
            fileOutputStreamPeople.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}