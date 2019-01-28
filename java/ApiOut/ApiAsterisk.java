package ApiOut;


import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiAsterisk {

    private ManagerConnection managerConnection;

    protected final static Logger logger = Logger.getLogger(String.valueOf(ApiAsterisk.class));


    public ApiAsterisk() throws IOException
    {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
                "192.168.43.224", 5038,"admin", "p4sw0rd");
        this.managerConnection = factory.createManagerConnection();
    }

    public void run() throws IOException, AuthenticationFailedException,
            TimeoutException, org.asteriskjava.manager.TimeoutException {
        OriginateAction originateAction;
        ManagerResponse originateResponse;

        originateAction = new OriginateAction();
        originateAction.setChannel("Local/112@from-internal");
        originateAction.setContext("from-internal");
        originateAction.setExten("110"); //extensio origen
        originateAction.setPriority(new Integer(1));
        originateAction.setTimeout(new Integer(30000));


        // connect to Asterisk and log in
        managerConnection.login();

        originateResponse = managerConnection.sendAction(originateAction, 30000);

        // print out whether the originate succeeded or not
        logger.log(Level.INFO,originateResponse.getResponse());

        // and finally log off and disconnect
        managerConnection.logoff();

    }
}
