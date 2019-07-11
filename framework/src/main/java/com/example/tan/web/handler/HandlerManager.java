package com.example.tan.web.handler;

import com.example.tan.web.mvc.Controller;
import com.example.tan.web.mvc.RequestMapping;
import com.example.tan.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for All Handler Manager
 */
public class HandlerManager {

    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    public static void resolveMappingHandler(List<Class<?>> classList){
        classList.forEach(cls -> {
            // if the class has the annotation Controller, create the Mapping Handler Instance
            // and add to the container - MappingHandlerList
            if(cls.isAnnotationPresent(Controller.class)){
                parseHandlerFromController(cls);
            }
        });
    }

    private static void parseHandlerFromController(Class<?> cls){
        Method[] methods = cls.getDeclaredMethods();
        for(Method method: methods){
            if(!method.isAnnotationPresent(RequestMapping.class)){
                continue;
            }
            // Get the request URI  by @RequestMapping
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();

            // Get the parameters list with annotation @RequestParam
            List<String> paramNameList = new ArrayList<>();
            for(Parameter parameter:method.getParameters()){
                if(parameter.isAnnotationPresent(RequestParam.class)){
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                    System.out.println(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            // deep copy into an array of String
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            // register the mappingHandler in HandlerManager
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);

            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
