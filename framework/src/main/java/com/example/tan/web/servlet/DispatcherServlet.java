package com.example.tan.web.servlet;

import com.example.tan.web.handler.HandlerManager;
import com.example.tan.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Handler;

public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        for(MappingHandler handler: HandlerManager.mappingHandlerList){
            try {
                if(handler.handle(request,response)){
                    return ;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
