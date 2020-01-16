package rest;

import logger.CSLogger;
import org.apache.catalina.LifecycleException;
import server.TCServer;

public class TCLauncher {


    public static void main(String args[]) throws LifecycleException {

        TCServer TCLauncher = new TCServer().getTomcatInstance().setTomcatConfigs();
        HbaseServlet metaDataServletLauncher = new HbaseServlet();
        metaDataServletLauncher.callHbase(TCLauncher.tomcat);
        TCLauncher.launchTomcat();
        CSLogger.l.info("Tomcat server launched.");
    }
}
