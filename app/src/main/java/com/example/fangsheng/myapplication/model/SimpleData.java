package com.example.fangsheng.myapplication.model;

import com.example.fangsheng.myapplication.Constants;
import com.example.fangsheng.myapplication.orm.field.DatabaseField;

import java.lang.reflect.Field;

/**
 * Created by fangsheng on 16/10/10.
 */

public class SimpleData {
    @DatabaseField(columnName = Constants.colum_name)
    String aa = Constants.colum_name;

    Field bb = null;
}
