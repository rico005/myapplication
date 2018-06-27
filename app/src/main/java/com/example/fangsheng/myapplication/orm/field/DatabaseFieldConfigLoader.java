package com.example.fangsheng.myapplication.orm.field;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by fangsheng on 2016/10/25.
 */

public class DatabaseFieldConfigLoader {
    private static final String TAG = "DatabaseFieldConfigLoader";

    public static DataPersister lookupForField(Field field, DataType type) {

        //外部没有设置dataType的时候
        if (type == null || DataType.UNKNOWN.equals(type)){
            String typeValue = DataType.STRING.toString();

            String typeName = field.getType().getSimpleName();
            Log.d(TAG, "lookupForField typeName=" + typeName);
            if ("String".equals(typeName)){
                typeValue = DataType.STRING.toString();
            }else if ("Integer".equals(typeName)
                    || "int".equals(typeName)
                    || "Long".equals(typeName)
                    || "long".equals(typeName)
                    || "Double".equals(typeName)
                    || "double".equals(typeName)){
                typeValue = DataType.INTEGER.toString();
            }else if ("boolean".equals(typeName)
                    || "Boolean".equals(typeName)){
                typeValue = DataType.BOOLEAN.toString();
            }

            return DataType.valueOf(typeValue).getDataPersister();
        }

        return type.getDataPersister();
    }
}
