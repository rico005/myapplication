package com.example.fangsheng.myapplication.orm.table;

import com.example.fangsheng.myapplication.orm.db.DatabaseType;
import com.example.fangsheng.myapplication.orm.field.DatabaseField;
import com.example.fangsheng.myapplication.orm.field.FieldType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangsheng on 2016/10/24.
 */

public class DatabaseTableConfig<T> {

    private Class<T> dataClass;
    private String tableName;
    private FieldType[] fieldTypes;
    private Constructor<T> constructor;

    /**
     * Extract the DatabaseTableConfig for a particular class by looking for class and field annotations. This is used
     * by internal classes to configure a class.
     */
    public static <T> DatabaseTableConfig<T> fromClass(Class<T> clazz, DatabaseType databaseType)
            throws SQLException {
        String tableName = extractTableName(clazz);
        if (databaseType.isEntityNamesMustBeUpCase()) {
            tableName = databaseType.upCaseEntityName(tableName);
        }
        return new DatabaseTableConfig<T>(clazz, tableName, extractFieldTypes(databaseType, clazz, tableName));
    }

    private DatabaseTableConfig(Class<T> dataClass, String tableName, FieldType[] fieldTypes) {
        this.dataClass = dataClass;
        this.tableName = tableName;
        this.fieldTypes = fieldTypes;
    }

    /**
     * Extract and return the table name for a class.
     */
    public static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name = null;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        }
        if (name == null) {
            // if the name isn't specified, it is the class name lowercased
            name = clazz.getSimpleName().toLowerCase();
        }
        return name;
    }

    private static <T> FieldType[] extractFieldTypes(DatabaseType databaseType, Class<T> clazz, String tableName)
            throws SQLException {
        List<FieldType> fieldTypes = new ArrayList<FieldType>();
        for (Class<?> classWalk = clazz; classWalk != null; classWalk = classWalk.getSuperclass()) {
            for (Field field : classWalk.getDeclaredFields()) {
                FieldType fieldType = FieldType.createFieldType(databaseType, tableName, field, clazz);
                if (fieldType != null) {
                    fieldTypes.add(fieldType);
                }
            }
        }
        if (fieldTypes.isEmpty()) {
            throw new IllegalArgumentException("No fields have a " + DatabaseField.class.getSimpleName()
                    + " annotation in " + clazz);
        }
        return fieldTypes.toArray(new FieldType[fieldTypes.size()]);
    }

    /**
     * Return the constructor for this class. If not constructor has been set on the class then it will be found on the
     * class through reflection.
     */
    public Constructor<T> getConstructor() {
        if (constructor == null) {
            constructor = findNoArgConstructor(dataClass);
        }
        return constructor;
    }

    /**
     * Locate the no arg constructor for the class.
     */
    public static <T> Constructor<T> findNoArgConstructor(Class<T> dataClass) {
        Constructor<T>[] constructors;
        try {
            @SuppressWarnings("unchecked")
            Constructor<T>[] consts = (Constructor<T>[]) dataClass.getDeclaredConstructors();
            // i do this [grossness] to be able to move the Suppress inside the method
            constructors = consts;
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't lookup declared constructors for " + dataClass, e);
        }
        for (Constructor<T> con : constructors) {
            if (con.getParameterTypes().length == 0) {
                if (!con.isAccessible()) {
                    try {
                        con.setAccessible(true);
                    } catch (SecurityException e) {
                        throw new IllegalArgumentException("Could not open access to constructor for " + dataClass);
                    }
                }
                return con;
            }
        }
        if (dataClass.getEnclosingClass() == null) {
            throw new IllegalArgumentException("Can't find a no-arg constructor for " + dataClass);
        } else {
            throw new IllegalArgumentException("Can't find a no-arg constructor for " + dataClass
                    + ".  Missing static on inner class?");
        }
    }

    /**
     * Return the field types associated with this configuration.
     */
    public FieldType[] getFieldTypes() throws SQLException {
        if (fieldTypes == null) {
            throw new SQLException("Field types have not been extracted in table config");
        }
        return fieldTypes;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<T> getDataClass() {
        return dataClass;
    }
}
