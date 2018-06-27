package com.example.fangsheng.myapplication.orm.field;

import com.example.fangsheng.myapplication.orm.db.DatabaseType;
import com.example.fangsheng.myapplication.orm.field.types.VoidType;
import com.example.fangsheng.myapplication.orm.table.DatabaseTableConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by fangsheng on 2016/10/24.
 */

public class DatabaseFieldConfig {

    public static final DataType DEFAULT_DATA_TYPE = DataType.UNKNOWN;
    public static final boolean DEFAULT_CAN_BE_NULL = true;
    public static final int NO_MAX_FOREIGN_AUTO_REFRESH_LEVEL_SPECIFIED = -1;
    public static final Class<? extends DataPersister> DEFAULT_PERSISTER_CLASS = VoidType.class;

    private String fieldName;
    private String columnName;
    private DataType dataType = DEFAULT_DATA_TYPE;
    private DataPersister dataPersister;
    private String defaultValue;
    private int width;
    private boolean canBeNull = DEFAULT_CAN_BE_NULL;
    private boolean id;
    private boolean generatedId;
    private String generatedIdSequence;
    private boolean foreign;
    private DatabaseTableConfig<?> foreignTableConfig;
    private boolean useGetSet;
    private Enum<?> unknownEnumValue;
    private boolean throwIfNull;
    private boolean persisted = true;
    private String format;
    private boolean unique;
    private boolean uniqueCombo;
    private boolean index;
    private String indexName;
    private boolean uniqueIndex;
    private String uniqueIndexName;
    private boolean foreignAutoRefresh;
    private int maxForeignAutoRefreshLevel = NO_MAX_FOREIGN_AUTO_REFRESH_LEVEL_SPECIFIED;
    private Class<? extends DataPersister> persisterClass = DEFAULT_PERSISTER_CLASS;
    private boolean allowGeneratedIdInsert;
    private String columnDefinition;
    private boolean foreignAutoCreate;
    private boolean version;
    private String foreignColumnName;
    private boolean readOnly;

    /**
     * Create and return a config converted from a {@link Field} that may have one of the following annotations:
     * {@link DatabaseField}, {@link ForeignCollectionField}, or javax.persistence...
     */
    public static DatabaseFieldConfig fromField(DatabaseType databaseType, String tableName, Field field)
            throws SQLException {

        // first we lookup the @DatabaseField annotation
        DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
        if (databaseField != null) {
            if (databaseField.persisted()) {
                return fromDatabaseField(databaseType, tableName, field, databaseField);
            } else {
                return null;
            }
        }

//        // lastly we check for @ForeignCollectionField
//        ForeignCollectionField foreignCollection = field.getAnnotation(ForeignCollectionField.class);
//        if (foreignCollection != null) {
//            return fromForeignCollection(databaseType, field, foreignCollection);
//        }

        return null;
    }

    public static DatabaseFieldConfig fromDatabaseField(DatabaseType databaseType, String tableName, Field field,
                                                        DatabaseField databaseField) {
        DatabaseFieldConfig config = new DatabaseFieldConfig();
        config.fieldName = field.getName();
        if (databaseType.isEntityNamesMustBeUpCase()) {
            config.fieldName = databaseType.upCaseEntityName(config.fieldName);
        }
        config.columnName = valueIfNotBlank(databaseField.columnName());
        config.dataType = databaseField.dataType();
        // NOTE: == did not work with the NO_DEFAULT string
        String defaultValue = databaseField.defaultValue();
        if (!defaultValue.equals(DatabaseField.DEFAULT_STRING)) {
            config.defaultValue = defaultValue;
        }
        config.width = databaseField.width();
        config.canBeNull = databaseField.canBeNull();
        config.id = databaseField.id();
        config.generatedId = databaseField.generatedId();
        config.generatedIdSequence = valueIfNotBlank(databaseField.generatedIdSequence());
        config.foreign = databaseField.foreign();
        config.useGetSet = databaseField.useGetSet();
        config.unknownEnumValue = findMatchingEnumVal(field, databaseField.unknownEnumName());
        config.throwIfNull = databaseField.throwIfNull();
        config.format = valueIfNotBlank(databaseField.format());
        config.unique = databaseField.unique();
        config.uniqueCombo = databaseField.uniqueCombo();

        // add in the index information
        config.index = databaseField.index();
        config.indexName = valueIfNotBlank(databaseField.indexName());
        config.uniqueIndex = databaseField.uniqueIndex();
        config.uniqueIndexName = valueIfNotBlank(databaseField.uniqueIndexName());
        config.foreignAutoRefresh = databaseField.foreignAutoRefresh();
        if (config.foreignAutoRefresh
                || databaseField.maxForeignAutoRefreshLevel() != DatabaseField.DEFAULT_MAX_FOREIGN_AUTO_REFRESH_LEVEL) {
            config.maxForeignAutoRefreshLevel = databaseField.maxForeignAutoRefreshLevel();
        } else {
            config.maxForeignAutoRefreshLevel = NO_MAX_FOREIGN_AUTO_REFRESH_LEVEL_SPECIFIED;
        }
        config.persisterClass = databaseField.persisterClass();
        config.allowGeneratedIdInsert = databaseField.allowGeneratedIdInsert();
        config.columnDefinition = valueIfNotBlank(databaseField.columnDefinition());
        config.foreignAutoCreate = databaseField.foreignAutoCreate();
        config.version = databaseField.version();
        config.foreignColumnName = valueIfNotBlank(databaseField.foreignColumnName());
        config.readOnly = databaseField.readOnly();

        return config;
    }

    private static String valueIfNotBlank(String newValue) {
        if (newValue == null || newValue.length() == 0) {
            return null;
        } else {
            return newValue;
        }
    }

    /**
     * Internal method that finds the matching enum for a configured field that has the name argument.
     *
     * @return The matching enum value or null if blank enum name.
     * @throws IllegalArgumentException
     *             If the enum name is not known.
     */
    public static Enum<?> findMatchingEnumVal(Field field, String unknownEnumName) {
        if (unknownEnumName == null || unknownEnumName.length() == 0) {
            return null;
        }
        for (Enum<?> enumVal : (Enum<?>[]) field.getType().getEnumConstants()) {
            if (enumVal.name().equals(unknownEnumName)) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("Unknwown enum unknown name " + unknownEnumName + " for field " + field);
    }

    /**
     * Find and return the appropriate getter method for field.
     *
     * @return Get method or null (or throws IllegalArgumentException) if none found.
     */
    public static Method findGetMethod(Field field, boolean throwExceptions) throws IllegalArgumentException {
        Method fieldGetMethod;
        if (Locale.ENGLISH.equals(Locale.getDefault())) {
            fieldGetMethod =
                    findMethodFromNames(field, true, throwExceptions, methodFromField(field, "get", null),
                            methodFromField(field, "is", null));
        } else {
            fieldGetMethod =
                    findMethodFromNames(field, true, throwExceptions, methodFromField(field, "get", null),
                            methodFromField(field, "get", Locale.ENGLISH), methodFromField(field, "is", null),
                            methodFromField(field, "is", Locale.ENGLISH));
        }
        if (fieldGetMethod == null) {
            return null;
        }
        if (fieldGetMethod.getReturnType() != field.getType()) {
            if (throwExceptions) {
                throw new IllegalArgumentException("Return type of get method " + fieldGetMethod.getName()
                        + " does not return " + field.getType());
            } else {
                return null;
            }
        }
        return fieldGetMethod;
    }

    /**
     * Find and return the appropriate setter method for field.
     *
     * @return Set method or null (or throws IllegalArgumentException) if none found.
     */
    public static Method findSetMethod(Field field, boolean throwExceptions) throws IllegalArgumentException {
        Method fieldSetMethod;
        if (Locale.ENGLISH.equals(Locale.getDefault())) {
            fieldSetMethod = findMethodFromNames(field, false, throwExceptions, methodFromField(field, "set", null));
        } else {
            fieldSetMethod =
                    findMethodFromNames(field, false, throwExceptions, methodFromField(field, "set", null),
                            methodFromField(field, "set", Locale.ENGLISH));
        }
        if (fieldSetMethod == null) {
            return null;
        }
        if (fieldSetMethod.getReturnType() != void.class) {
            if (throwExceptions) {
                throw new IllegalArgumentException("Return type of set method " + fieldSetMethod.getName()
                        + " returns " + fieldSetMethod.getReturnType() + " instead of void");
            } else {
                return null;
            }
        }
        return fieldSetMethod;
    }

    private static Method findMethodFromNames(Field field, boolean isGetMethod, boolean throwExceptions,
                                              String... methodNames) {
        NoSuchMethodException firstException = null;
        for (String methodName : methodNames) {
            try {
                if (isGetMethod) {
                    // get method has no argument
                    return field.getDeclaringClass().getMethod(methodName);
                } else {
                    // set method has same argument type as field
                    return field.getDeclaringClass().getMethod(methodName, field.getType());
                }
            } catch (NoSuchMethodException nsme) {
                if (firstException == null) {
                    firstException = nsme;
                }
            }
        }
        if (throwExceptions) {
            throw new IllegalArgumentException("Could not find appropriate " + (isGetMethod ? "get" : "set")
                    + " method for " + field, firstException);
        } else {
            return null;
        }
    }

    private static String methodFromField(Field field, String prefix, Locale locale) {
        String name = field.getName();
        String start = name.substring(0, 1);
        if (locale == null) {
            // NOTE: this is not an entity to be capitalized with the database type, we are using default locale here
            start = start.toUpperCase();
        } else {
            start = start.toUpperCase(locale);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(start);
        sb.append(name, 1, name.length());
        return sb.toString();
    }

    /**
     * Process the settings when we are going to consume them.
     */
    public void postProcess() {
        if (foreignColumnName != null) {
            foreignAutoRefresh = true;
        }
        if (foreignAutoRefresh && maxForeignAutoRefreshLevel == NO_MAX_FOREIGN_AUTO_REFRESH_LEVEL_SPECIFIED) {
            maxForeignAutoRefreshLevel = DatabaseField.DEFAULT_MAX_FOREIGN_AUTO_REFRESH_LEVEL;
        }
    }

    /**
     * @see DatabaseField#columnName()
     */
    public String getColumnName() {
        return columnName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    /**
     * @see DatabaseField#foreign()
     */
    public boolean isForeign() {
        return foreign;
    }

    public boolean isForeignAutoRefresh() {
        return foreignAutoRefresh;
    }

    public boolean isAllowGeneratedIdInsert() {
        return allowGeneratedIdInsert;
    }

    /**
     * @see DatabaseField#generatedId()
     */
    public boolean isGeneratedId() {
        return generatedId;
    }

    /**
     * @see DatabaseField#id()
     */
    public boolean isId() {
        return id;
    }

    /**
     * @see DatabaseField#generatedIdSequence()
     */
    public String getGeneratedIdSequence() {
        return generatedIdSequence;
    }

    /**
     * @see DatabaseField#useGetSet()
     */
    public boolean isUseGetSet() {
        return useGetSet;
    }

    public boolean isForeignAutoCreate() {
        return foreignAutoCreate;
    }

    public boolean isVersion() {
        return version;
    }

    public String getColumnDefinition() {
        return columnDefinition;
    }

    public String getUniqueIndexName(String tableName) {
        if (uniqueIndex && uniqueIndexName == null) {
            uniqueIndexName = findIndexName(tableName);
        }
        return uniqueIndexName;
    }

    public String getIndexName(String tableName) {
        if (index && indexName == null) {
            indexName = findIndexName(tableName);
        }
        return indexName;
    }

    private String findIndexName(String tableName) {
        if (columnName == null) {
            return tableName + "_" + fieldName + "_idx";
        } else {
            return tableName + "_" + columnName + "_idx";
        }
    }

    public String getFormat() {
        return format;
    }

    public Enum<?> getUnknownEnumValue() {
        return unknownEnumValue;
    }

    /**
     * @see DatabaseField#width()
     */
    public int getWidth() {
        return width;
    }

    /**
     * @see DatabaseField#dataType()
     */
    public DataType getDataType() {
        return dataType;
    }

    public boolean isUnique() {
        return unique;
    }

    /**
     * @see DatabaseField#canBeNull()
     */
    public boolean isCanBeNull() {
        return canBeNull;
    }

    /**
     * @see DatabaseField#defaultValue()
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isUniqueCombo() {
        return uniqueCombo;
    }
}
