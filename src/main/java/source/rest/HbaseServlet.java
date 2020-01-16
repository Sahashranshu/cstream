package rest;

import logger.CSLogger;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import pojo.PojoJsonConverter;
import pojo.cstreamPojo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class HbaseServlet extends HttpServlet{

    private String JSON_HEADER = "CLICK_POINT";


    public boolean callHbase(Tomcat tomcat)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     {
        Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());
        tomcat.addServlet(ctx, "Embedded", new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                long start = System.currentTimeMillis();
                Writer w = resp.getWriter();
                w.write("Ingesting clickstream values.\n");
                CSLogger.l.info("Ingesting clickstream value to Hbase.");
                String requestedJson = req.getParameter(JSON_HEADER);
                CSLogger.l.info("Requested Json for parsing: "+ requestedJson);
                if(requestedJson == null){
                    requestedJson = req.getReader().readLine();
                    System.out.println("Raw Json text: " + requestedJson);
                }
                cstreamPojo pojo = new cstreamPojo();

                cstreamPojo[] cdataArray = (cstreamPojo[]) PojoJsonConverter
                        .getcstreamPojoArrayFromJson(requestedJson);

                for(cstreamPojo cdata: cdataArray){

                    /*
                    Call Hbase through this for loop.
                     */


                }

                w.flush();
                w.close();
            }
        });
        ctx.addServletMapping("/*", "Embedded");
        return true;
    }
}