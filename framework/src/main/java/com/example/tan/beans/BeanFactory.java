package com.example.tan.beans;


import com.example.tan.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    //Mapping from Class object to Bean Object
    private static Map<Class<?>,Object> classToBean = new ConcurrentHashMap<>();


    /**
     * Get Bean Object by its class Object.
     * @param cls
     * @return
     */
    public static Object getBean(Class<?> cls){
        return classToBean.getOrDefault(cls,null);
    }

    /**
     * Init Bean,
     * Create those beans which are independent first.
     * @param classList
     * @throws Exception
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);

        while(toCreate.size() != 0){
            int remainSize = toCreate.size();
            for(int idx = 0;  idx < toCreate.size() ; idx++){
                if(finishCreate(toCreate.get(idx))){
                    toCreate.remove(idx);
                }
            }
            if(toCreate.size() == remainSize){
                throw new Exception("dependency Cycle!");
            }
        }
    }

    /**
     * 1. If the Class is annotated by @Bean of @Controller,
     *    create an instance and register it in the bean factory.
     *
     * 2. If the bean has some fields which are @Autowired.
     *    If its dependent bean has been created, we can create the bean and put it into the Map.
     *    Otherwise, don't put the bean into the map. instead, create the dependent bean first
     *
     * @param cls
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static  boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        if(!cls.isAnnotationPresent(Bean.class) && ! cls.isAnnotationPresent(Controller.class)){
            return true ;
        }
        Object bean = cls.newInstance();
        // if the bean class has field which needs to be autowired.(depends on other classes)
        for(Field field : cls.getDeclaredFields()){
            if(field.isAnnotationPresent(Autowired.class)){
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);
                if(reliantBean == null) return false;
                field.setAccessible(true);
                field.set(bean,reliantBean);
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}
