package com.bihang.seaya.reflect;

import com.bihang.seaya.annotation.Interceptor;
import com.bihang.seaya.annotation.SeayaAction;
import com.bihang.seaya.bean.SeayaBeanFactory;
import com.bihang.seaya.bean.impl.SeayaDefaultBean;
import com.bihang.seaya.configuration.AbstractSeayaConfiguration;
import com.bihang.seaya.configuration.ApplicationConfiguration;
import com.bihang.seaya.enums.StatusEnum;
import com.bihang.seaya.exception.SeayaException;
import com.bihang.seaya.log.SeayaLog;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    private static Map<String, Class<?>> actionMap = null;
    private static Map<Class<?>, Integer> interceptorMap = null;

    private static Set<Class<?>> classes = null;
    private static Set<Class<?>> seaya_classes = null;

    private static List<Class<?>> configurationList = null;


    /**
     * get Configuration
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getConfiguration(String packageName) throws Exception {

        if (configurationList == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            // Manually add ApplicationConfiguration
            clsList.add(ApplicationConfiguration.class) ;

            if (clsList == null || clsList.isEmpty()) {
                return configurationList;
            }

            configurationList = new ArrayList<>(16);
            for (Class<?> cls : clsList) {

                if (cls.getSuperclass() != AbstractSeayaConfiguration.class) {
                    continue;
                }

                configurationList.add(cls) ;
            }
        }
        return configurationList;
    }
    /**
     * get @SeayaAction
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<String, Class<?>> getSeayaAction(String packageName) throws Exception {

        if (actionMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return actionMap;
            }

            actionMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {

                Annotation annotation = cls.getAnnotation(SeayaAction.class);
                if (annotation == null) {
                    continue;
                }

                SeayaAction seayaAction = (SeayaAction) annotation;
                actionMap.put(seayaAction.value() == null ? cls.getName() : seayaAction.value(), cls);

            }
        }
        return actionMap;
    }

    /**
     * get @Interceptor
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<Class<?>, Integer> getSeayaInterceptor(String packageName) throws Exception {

        if (interceptorMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return interceptorMap;
            }

            interceptorMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {
                Annotation annotation = cls.getAnnotation(Interceptor.class);
                if (annotation == null) {
                    continue;
                }

                Interceptor interceptor = (Interceptor) annotation;
                interceptorMap.put(cls, interceptor.order());

            }
        }

        return interceptorMap;
    }

    /**
     * get All classes
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception {

        if (classes == null){
            classes = new HashSet<>(32) ;

            baseScanner(packageName,classes);
        }

        return classes;
    }

    private static void baseScanner(String packageName,Set set) {
        boolean recursive = true;

        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;
        try {
            //通过包名获取到target下的指定文件夹(target文件夹下存放.class文件)
            //Jar文件中同样可能存在packageDirName
            // 例如jar:file:/E:/Own/%e6%af%95%e4%b8%9a%e8%ae%be%e8%ae%a1/seaya-ioc-1.0-SNAPSHOT.jar!/com/bihang/seaya
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                System.out.println(url);
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, set);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            set.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        SeayaLog.error("IOException");
                    }
                }
            }
        } catch (IOException e) {
            SeayaLog.error("IOException");
        }
    }


    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        for (File file : files) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    SeayaLog.error("ClassNotFoundException");
                }
            }
        }
    }


    private static final String BASE_PACKAGE = "com.bihang.seaya";

    /**
     * get custom route bean
     * @return
     * @throws Exception
     */
    public static Class<?> getCustomRouteBean() throws Exception {
        List<Class<?>> classList = new ArrayList<>();


        Set<Class<?>> classes = getCustomRouteBeanClasses(BASE_PACKAGE) ;
        for (Class<?> aClass : classes) {

            if (aClass.getInterfaces().length == 0){
                continue;
            }
            if (aClass.getInterfaces()[0] != SeayaBeanFactory.class){
                continue;
            }
            classList.add(aClass) ;
        }

        if (classList.size() > 2){
            throw new SeayaException(StatusEnum.DUPLICATE_IOC);
        }

        if (classList.size() == 2){
            Iterator<Class<?>> iterator = classList.iterator();
            while (iterator.hasNext()){
                if (iterator.next()== SeayaDefaultBean.class){
                    iterator.remove();
                }
            }
        }
        SeayaLog.log("BeanFactory"+classList.get(0));
        return classList.get(0);
    }


    public static Set<Class<?>> getCustomRouteBeanClasses(String packageName) throws Exception {

        if (seaya_classes == null){
            seaya_classes = new HashSet<>(32) ;

            baseScanner(packageName,seaya_classes);
        }

        return seaya_classes;
    }
}
