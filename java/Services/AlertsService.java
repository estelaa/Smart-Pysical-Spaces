package Services;

import Model.Alert;
import Model.ResponseAlert;
import Repository.AlertDao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;

@Service
public class AlertsService {

    private AlertDao alertDao = new AlertDao();
    private static final long hour = 3600000/12;    //5 min
    private final String USER_AGENT = "Mozilla/5.0";


    public boolean insertAlert(String description){
        return alertDao.insertAlert(description);
    }

    public boolean sendPOST(Alert alert, String url){
        /*Per fer un POST a un edifici neighbur amb una alerta "important"*/
/*
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "\"description\": \"" + alert.getDescription() + "\"";//enviem nomes la descripcio perque la data es posa quan entra a la DB
            //"date"=&

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();

            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            //print result
            System.out.println(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            //Millor sense el retornar el false sino falla tot el update
            //return false;
        }
*/


        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Alert> request = new HttpEntity<>(alert);
        ResponseEntity<ResponseAlert> response = restTemplate.exchange(url, HttpMethod.POST, request, ResponseAlert.class);

        //assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        ResponseAlert ResponseAlert = response.getBody();

        //assertThat(alertTmp, notNullValue());
        //assertThat(alertTmp.getDescription(), is(alert.getDescription()));

        return true;
    }

    public List<Alert> getListAlerts(){
        List<Alert> listAlertTmp =  alertDao.getListAllAlerts();
        List<Alert> listAlertsR = new ArrayList<>();
        Date currentDate = new Date();
        DateFormat formatDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            for (Alert alert : listAlertTmp) {
                Date date = formatDate.parse(alert.getDate());
                long dif = currentDate.getTime() - date.getTime();  //Mirem la diferencia en milisegons desde January 1, 1970, 00:00:00 GMT
                if(dif>hour) {
                    //alertDao.deleteAlert(alert);   //Les borrem de la DB o no fa falta?
                }else {
                    listAlertsR.add(alert); //Llista amb les alertas menors de una 5 min
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listAlertsR;
    }

}



