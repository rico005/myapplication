package com.example.fangsheng.myapplication.orm.table;

import com.example.fangsheng.myapplication.orm.db.DatabaseType;
import com.example.fangsheng.myapplication.orm.field.FieldType;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

/**
 * Created by fangsheng on 2016/10/24.
 */

public class TableInfo<T, ID> {

    private final Class<T> dataClass;
    private final String tableName;
    private final FieldType[] fieldTypes;
    private final FieldType idField;
    private final Constructor<T> constructor;
    private final boolean foreignAutoCreate;

    /**
     * Creates a holder of information about a table/class.
     *
     * @param dataClass
     *            Class that we are holding information about.
     */
    public TableInfo(DatabaseType databaseType, Class<T> dataClass)
            throws SQLException {
        this(DatabaseTableConfig.fromClass(dataClass, databaseType));
    }

    /**
     * Creates a holder of information about a table/class.
     *
     * @param tableConfig
     *            Configuration for our table.
     */
    public TableInfo(DatabaseTableConfig<T> tableConfig)
            throws SQLException {
        this.dataClass = tableConfig.getDataClass();
        this.tableName = tableConfig.getTableName();
        this.fieldTypes = tableConfig.getFieldTypes();
        // find the id field
        FieldType findIdFieldType = null;
        boolean foreignAutoCreate = false;
        int foreignCollectionCount = 0;
        for (FieldType fieldType : fieldTypes) {
            if (fieldType.isId() || fieldType.isGeneratedId() || fieldType.isGeneratedIdSequence()) {
                if (findIdFieldType != null) {
                    throw new SQLException("More than 1 idField configured for class " + dataClass + " ("
                            + findIdFieldType + "," + fieldType + ")");
                }
                findIdFieldType = fieldType;
            }
            if (fieldType.isForeignAutoCreate()) {
                foreignAutoCreate = true;
            }
//            if (fieldType.isForeignCollection()) {
//                foreignCollectionCount++;
//            }
        }
        // can be null if there is no id field
        this.idField = findIdFieldType;
        this.constructor = tableConfig.getConstructor();
        this.foreignAutoCreate = foreignAutoCreate;
//        if (foreignCollectionCount == 0) {
//            this.foreignCollections = NO_FOREIGN_COLLECTIONS;
//        } else {
//            this.foreignCollections = new FieldType[foreignCollectionCount];
//            foreignCollectionCount = 0;
//            for (FieldType fieldType : fieldTypes) {
//                if (fieldType.isForeignCollection()) {
//                    this.foreignCollections[foreignCollectionCount] = fieldType;
//                    foreignCollectionCount++;
//                }
//            }
//        }
    }

    /**
     * Return the name of the table associated with the object.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Return the array of field types associated with the object.
     */
    public FieldType[] getFieldTypes() {
        return fieldTypes;
    }

}
