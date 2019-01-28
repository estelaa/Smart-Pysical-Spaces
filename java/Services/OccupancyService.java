package Services;

import Model.Occupancy;
import Repository.OccupancyDao;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OccupancyService {

    OccupancyDao occupancyDao = new OccupancyDao();

    public List<Occupancy> getListAverageOccupancy(){
        List<Occupancy> listOccupancy = omplirOccupancy();
        //List<Occupancy> listOccupancy = occupancyDao.listOccupancy();
        List<Occupancy> listAverage = new ArrayList<>();
        Date currentDate = new Date();
        DateFormat formatDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        //Mon May 28 13:04:45 CEST 2018
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTime(currentDate);

        int CC = 0;
        int div = 0;
        int hour = 0;
        int idspace = 0;
        boolean exit = false;


        //FER UNA MITJA DE CADA HORA
        while (!exit){
            Calendar inici = Calendar.getInstance();
            inici.set(Calendar.HOUR_OF_DAY, hour);
            inici.set(Calendar.MINUTE, 0);
            inici.set(Calendar.SECOND, 0);

            Calendar fi = Calendar.getInstance();
            fi.set(Calendar.HOUR_OF_DAY, hour+1);
            fi.set(Calendar.MINUTE, 0);
            fi.set(Calendar.SECOND, 0);
            try {
                for(Occupancy occup: listOccupancy){
                    Calendar calendarTmp = Calendar.getInstance();
                    calendarTmp.setTime(formatDate.parse(occup.getDate()));

                    if (calendarTmp.after(inici) && calendarTmp.before(fi)) {
                        idspace=occup.getIdspace();
                        CC = CC + occup.getCapacitycurrent();
                        div = div + 1;
                    }
                }
                Occupancy occupancy = new Occupancy(idspace, formatDate.parse(inici.getTime().toString()).toString(), CC/div);
                listAverage.add(occupancy);
                hour = hour + 1;
                CC=0;
                div=0;
                if(hour==24){
                    exit=true;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return listAverage;
    }


    public List<Occupancy> omplirOccupancy(){
        //METODE PER OMPLIR AMB NUMEROS ALEATORIS PER PROVAR
        int CC=0;
        Date currentDate = new Date();
        DateFormat formatDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Calendar calendarCurrent = Calendar.getInstance();
        try {
            calendarCurrent.setTime(formatDate.parse(currentDate.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);


        List<Occupancy> listOccupancy = new ArrayList<Occupancy>();
        for(int hour=0; hour<24; hour++){
            for(int minute=0; minute<60;minute=minute+10){

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                if(hour==8){
                    CC = CC + 5;
                    if(45<minute && minute<60){
                        CC = CC - 3;
                    }
                }else if(9<hour && hour<13){
                    CC = CC + 8;
                    if(45<minute && minute<60){
                        CC = CC - 3;
                    }
                }else if(13<hour && hour<15){
                    CC = CC - 15;
                    if(45<minute && minute<60){
                        CC = CC - 7;
                    }
                }else if(15<hour && hour<18){
                    CC = CC + 2;
                    if(45<minute && minute<60){
                        CC = CC - 7;
                    }
                }else if(18<hour && hour<20){
                    CC = CC -15;
                    if(45<minute && minute<60){
                        CC = CC - 7;
                    }
                }

                if(CC<0){
                    CC=0;
                }


                try {
                    Occupancy occupancy = new Occupancy(0, formatDate.parse(calendar.getTime().toString()).toString(),CC);
                    listOccupancy.add(occupancy);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return listOccupancy;
    }


}
