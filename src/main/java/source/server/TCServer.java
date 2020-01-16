package server;

import configs.ConfigInstances;
import logger.CSLogger;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class TCServer {

    private final int port = ConfigInstances.TCConfigs.PORT;

    public Tomcat tomcat = null;

    public TCServer getTomcatInstance() throws LifecycleException {
         this.tomcat = new Tomcat();
         return this;
    }

    public TCServer setTomcatConfigs() {
        this.tomcat.setPort(port);
        CSLogger.l.info("Tomcat port set to: "+port);
        return this;
    }

    public TCServer launchTomcat() throws LifecycleException {
        try{
            CSLogger.l.info("Starting tomcat.");
            this.tomcat.start();
            CSLogger.l.info("Tomcat successfully started");
        }catch(LifecycleException e){
            CSLogger.l.error("Error in starting tomcat. "+ e.fillInStackTrace());
            System.err.println("Error in starting tomcat. "+ e.fillInStackTrace());
            throw new LifecycleException();
        }
        this.tomcat.getServer().await();
        return this;
    }


}
