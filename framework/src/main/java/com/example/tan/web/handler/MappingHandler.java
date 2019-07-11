package com.example.tan.web.handler;

import com.example.tan.beans.BeanFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Handle the URI request Mapping to invoke the method in Controller at run time.
 */
public class MappingHandler {

    private String uri; //Request URI


    private Class<?> controller; // controller

    private Method method; // method needed to be execute in the Mapping Handler

    private String[] args; // args for that method.

    /**
     * 1. Verify the Mapping Handler can deal with the request or not.
     *
     * 2. Get the parameters values from the request.
     *
     * 3.Create the Controller object by reflection and invoke the method with parameters.
     *
     * 4. Write the response to ServletResponse  and return true;
     *
     * @param request
     * @param response
     * @return
     */
    public boolean handle(ServletRequest  request, ServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest)request).getRequestURI();
        if(!requestUri.equals(this.uri)){
            return false;
        }

        Object[] parameters =  new Object[args.length];
        for(int idx = 0; idx < args.length; idx++){
            parameters[idx] = request.getParameter(args[idx]);
        }

        // get the controller from the bean factory.
        Object controller = BeanFactory.getBean(this.controller);

        Object res = this.method.invoke(controller, parameters);
        response.getWriter().println(res.toString());
        return true;
    }

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }
}
