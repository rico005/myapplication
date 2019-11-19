package com.fangsheng.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("========================");
        System.out.println("hello gradle plugin!");
        System.out.println("========================");

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MyTransform(project))
    }
}