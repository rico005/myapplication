package com.example.fangsheng.myapplication.decorate;

import android.support.annotation.Keep;


import com.example.fangsheng.myapplication.decorate.decorator.BaseDecorator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fangsheng on 2016/11/29.
 */
@Keep
public class DecorateBuilder implements Serializable{

    private static final long serialVersionUID = 1L;

    public static transient Map<Long, ClassLoader> classLoaderMap = new ConcurrentHashMap<>();

    private long mGeneratedId = -1;
    private transient ClassLoader mClassLoader = null;
    private List<Class> mDecoratorList = new ArrayList<>();

    public DecorateBuilder writeClassLoader(long generatedId, ClassLoader classLoader){
        mGeneratedId = generatedId;
        if (classLoader != null){
            classLoaderMap.put(generatedId, classLoader);
        }else { //不传入classLoader的时候，默认使用mDecoratorList里第一个Class<T>里的classLoader
            if (mDecoratorList != null && !mDecoratorList.isEmpty()){
                Class clazz = mDecoratorList.get(0);
                if (clazz != null){
                    classLoaderMap.put(generatedId, clazz.getClassLoader());
                }
            }
        }
        return this;
    }

    public DecorateBuilder readClassLoader(){
        mClassLoader = classLoaderMap.get(mGeneratedId);
        classLoaderMap.remove(mGeneratedId);
        return this;
    }

    public synchronized  <T extends BaseDecorator> DecorateBuilder addDecorator(Class<T> decoratorClazz){
        mDecoratorList.add(decoratorClazz);
        return this;
    }

    public synchronized  <T extends BaseDecorator> T getDecoratorInstance(Class<T> decoratorClazz, Object context){
        for (Class clazz : mDecoratorList){
            if (clazz != null && BaseDecorator.class.isAssignableFrom(clazz)
                    && decoratorClazz.isAssignableFrom(clazz)){
                try{
                    Class clazzReal = null;
                    //不同的classLoader会造成无法实例化具体的类，如果拿全局的classLoader，又会由于遍历所有bundle可能造成anr
                    if (mClassLoader != null){
                        clazzReal = mClassLoader.loadClass(clazz.getName());
                    }else {
                        clazzReal = clazz;
                    }

                    T instance = (T)clazzReal.newInstance();
                    instance.init(context);
                    return instance;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
