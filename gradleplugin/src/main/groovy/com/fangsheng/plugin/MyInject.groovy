package com.fangsheng.plugin

import javassist.ByteArrayClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor

import java.lang.reflect.Modifier

public class MyInject {

    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "System.out.println(\"I Love TaoBao\" ); ";

    public static void injectDir(String path, String packageName) {
        pool.appendClassPath(path)
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    // 判断当前目录是否是在我们的应用包里面
                    int index = filePath.indexOf(packageName);
                    boolean isMyPackage = index != -1;
                    System.out.println("========================");
                    System.out.println("isMyPackage=" + isMyPackage + "\n packageName=" + packageName + "\n filePath=" + filePath);
                    System.out.println("========================");
                    if (isMyPackage) {
                        int end = filePath.length() - 6 // .class = 6
                        String className = filePath.substring(index, end)
                                .replace('\\', '.').replace('/', '.')
                        //开始修改class文件
                        CtClass c = pool.getCtClass(className)

                        System.out.println("isFrozen=" + c.isFrozen());
                        if (c.isFrozen()) {
                            c.defrost()
                        }
                        System.out.println("========================");
                        System.out.println("c=" + c.getSimpleName() + ", isAbstract=" + Modifier.isAbstract(c.getModifiers()) + ", isInterface=" + c.isInterface());
                        System.out.println("========================");

                        if (!c.isInterface()) {
                            CtConstructor[] cts = c.getDeclaredConstructors()
                            System.out.println("cts=" + cts);
                            if (cts == null || cts.length == 0) {
                                //手动创建一个构造函数
                                CtConstructor constructor = new CtConstructor(new CtClass[0], c)
                                System.out.println("step1");
//                                constructor.insertBeforeBody(injectStr)
//                                System.out.println("step2");
//                                c.addConstructor(constructor)
                            } else {
                                cts[0].insertBeforeBody(injectStr)
                            }
                            c.writeFile(path)
                        }


                        c.detach()
                    }
                }
            }
        }
    }

    public static void prepareSystemPath(String[] packageNames){
        packageNames.each { String packageName ->
            CtClass systemClass = pool.makeClass(packageName)
            byte[] systemBytes = systemClass.toBytecode()
            systemClass.defrost()
            pool.insertClassPath(new ByteArrayClassPath(packageName, systemBytes))
        }
    }
}