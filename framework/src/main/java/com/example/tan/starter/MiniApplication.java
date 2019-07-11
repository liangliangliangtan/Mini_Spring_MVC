package com.example.tan.starter;

import com.example.tan.beans.BeanFactory;
import com.example.tan.core.ClassScanner;
import com.example.tan.web.handler.HandlerManager;
import com.example.tan.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.util.List;

/**
 * This is the starter of our Mini Spring Framework.
 * It has run method takes two argument. The first one is the
 * Class Object of the client, so the framework can get the Application class information
 */
public class MiniApplication {
    public static void run(Class<?> cls, String[] args){
        System.out.println("Hello, Mini Spring Framework");

        // Start the server when call the method
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            // log the class name
            classList.forEach(it -> System.out.println(it.getName()));
            // add all the MappingHandler to the Controller
            HandlerManager.resolveMappingHandler(classList);

            // register all Spring beans to the Bean factory
            BeanFactory.initBean(classList);
        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
