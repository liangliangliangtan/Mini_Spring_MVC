package com.example.tan.web.server;

import com.example.tan.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * This class is for starting the embed Tomcat Server
 */
public class TomcatServer {

    private Tomcat tomcat;
    private String[] args; // TODO: arguments for starting the tomcat server


    public TomcatServer(String[] args){
        this.args = args;
    }

    public void startServer() throws LifecycleException {

        tomcat = new Tomcat();
        tomcat.setPort(8080);// set listening port
        tomcat.start();

        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener()); //add default listener

        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context,"dispatcherServlet",servlet).setAsyncSupported(true);
        context.addServletMappingDecoded("/","dispatcherServlet");

        tomcat.getHost().addChild(context); // add context container to the host container

        // Create await thread by anomoynous inner class
        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run(){
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
