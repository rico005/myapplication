package com.example.fangsheng.myapplication.decorate;

import android.content.Intent;

/**
 * Created by fangsheng on 2016/11/29.
 */

public class DecorateHelper {

    public static final String BUILDER_KEY = "decorate_builder";

    public static DecorateBuilder build(){
        return new DecorateBuilder();
    }

    public static DecorateBuilder resolve(Intent intent){
        DecorateBuilder decorateBuilder = null;
        if (intent != null){
            try {
                Object decorateBuilderObj = intent.getSerializableExtra(BUILDER_KEY);
                if (decorateBuilderObj instanceof DecorateBuilder){
                    decorateBuilder = (DecorateBuilder)decorateBuilderObj;
                    decorateBuilder.readClassLoader();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return decorateBuilder;
    }

    public static Intent assembleIntent(Intent intent, ClassLoader classLoader, DecorateBuilder builder){
        if (intent != null){
            if (builder == null){
                builder = build();
            }
            builder.writeClassLoader(System.currentTimeMillis(), classLoader);
            intent.putExtra(BUILDER_KEY, builder);
        }
        return intent;
    }
}
