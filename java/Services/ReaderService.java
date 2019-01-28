package Services;

import Model.Infrastructure;
import Model.People;
import Model.Space;
import Repository.InfrastructureDao;
import Repository.PeopleDao;
import Repository.SpaceDao;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public abstract class ReaderService {
    private SpaceDao spaceDao = new SpaceDao();
    private PeopleDao peopleDao = new PeopleDao();
    private InfrastructureDao infrastructureDao = new InfrastructureDao();

    protected List<String> convertMultipartToList(MultipartFile multipartFile){
        InputStream inputStream = null;
        List<String> lines = new ArrayList<>();
        try {
            inputStream = multipartFile.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine(); //The first line is not interested
            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }
            inputStream.close();
            bufferedReader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected List<String> convertFileToList(File file){
        List<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();
            while((line=br.readLine())!=null){
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
}
