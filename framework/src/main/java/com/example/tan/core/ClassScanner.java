package com.example.tan.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Scan all the classes
 */
public class ClassScanner {

    /**
     * Select jar resources and get all classes under the jar resources
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        String path = packageName.replace(".","/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            if(resource.getProtocol().contains("jar")){
                JarURLConnection jarURLConnection = (JarURLConnection )resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath,path));
            }
        }
        return classList;
    }

    /**
     * Get all the CLass objects from the jar file Path
     * @param jarFilePath
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath,String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()){
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName =  jarEntry.getName(); //e.g com/example/tan/Text.class
            if(entryName.startsWith(path) && entryName.endsWith(".class")){
                String classFullName = entryName.replace("/",".").substring(0,entryName.length()-6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
