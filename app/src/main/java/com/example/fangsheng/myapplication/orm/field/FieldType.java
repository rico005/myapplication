package com.example.fangsheng.myapplication.orm.field;

import com.example.fangsheng.myapplication.orm.db.DatabaseType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * Created by fangsheng on 2016/10/24.
 */

public class FieldType {

    /** default suffix added to fields that are id fields of foreign objects */
    public static final String FOREIGN_ID_FIELD_SUFFIX = "_id";

    private final String tableName;
    private final Field field;
    private final Class<?> parentClass;
    private final boolean isId;
    private final boolean isGeneratedId;
    private final String generatedIdSequence;
    private final String columnName;
    private final DatabaseFieldConfig fieldConfig;
    private final Method fieldGetMethod;
    private final Method fieldSetMethod;
    private DataPersister dataPersister;
    private Object defaultValue;

    /**
     * You should use {@link FieldType#createFieldType} to instantiate one of these field if you have a {@link Field}.
     */
    public FieldType(DatabaseType databaseType, String tableName, Field field, DatabaseFieldConfig fieldConfig,
                     Class<?> parentClass) throws SQLException {
        this.tableName = tableName;
        this.field = field;
        this.parentClass = parentClass;

        // post process our config settings
        fieldConfig.postProcess();

        Class<?> clazz = field.getType();
        /*
        DataPersister dataPersister;
        if (fieldConfig.getDataPersister() == null) {
            Class<? extends DataPersister> persisterClass = fieldConfig.getPersisterClass();
            if (persisterClass == null || persisterClass == VoidType.class) {
                dataPersister = DataPersisterManager.lookupForField(field);
            } else {
                Method method;
                try {
                    method = persisterClass.getDeclaredMethod("getSingleton");
                } catch (Exception e) {
                    throw SqlExceptionUtil
                            .create("Could not find getSingleton static method on class " + persisterClass, e);
                }
                Object result;
                try {
                    result = method.invoke(null);
                } catch (InvocationTargetException e) {
                    throw SqlExceptionUtil.create("Could not run getSingleton method on class " + persisterClass,
                            e.getTargetException());
                } catch (Exception e) {
                    throw SqlExceptionUtil.create("Could not run getSingleton method on class " + persisterClass, e);
                }
                if (result == null) {
                    throw new SQLException(
                            "Static getSingleton method should not return null on class " + persisterClass);
                }
                try {
                    dataPersister = (DataPersister) result;
                } catch (Exception e) {
                    throw SqlExceptionUtil
                            .create("Could not cast result of static getSingleton method to DataPersister from class "
                                    + persisterClass, e);
                }
            }
        } else {
            dataPersister = fieldConfig.getDataPersister();
            if (!dataPersister.isValidForField(field)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Field class ").append(clazz.getName());
                sb.append(" for field ").append(this);
                sb.append(" is not valid for type ").append(dataPersister);
                Class<?> primaryClass = dataPersister.getPrimaryClass();
                if (primaryClass != null) {
                    sb.append(", maybe should be " + primaryClass);
                }
                throw new IllegalArgumentException(sb.toString());
            }
        }
        */

        this.dataPersister = DatabaseFieldConfigLoader.lookupForField(field, fieldConfig.getDataType());

        String foreignColumnName = fieldConfig.getForeignColumnName();
        String defaultFieldName = field.getName();
        /*
        if (fieldConfig.isForeign() || fieldConfig.isForeignAutoRefresh() || foreignColumnName != null) {
            if (dataPersister != null && dataPersister.isPrimitive()) {
                throw new IllegalArgumentException(
                        "Field " + this + " is a primitive class " + clazz + " but marked as foreign");
            }
            if (foreignColumnName == null) {
                defaultFieldName = defaultFieldName + FOREIGN_ID_FIELD_SUFFIX;
            } else {
                defaultFieldName = defaultFieldName + "_" + foreignColumnName;
            }
            if (ForeignCollection.class.isAssignableFrom(clazz)) {
                throw new SQLException("Field '" + field.getName() + "' in class " + clazz + "' should use the @"
                        + ForeignCollectionField.class.getSimpleName() + " annotation not foreign=true");
            }
        } else if (fieldConfig.isForeignCollection()) {
            if (clazz != Collection.class && !ForeignCollection.class.isAssignableFrom(clazz)) {
                throw new SQLException("Field class for '" + field.getName() + "' must be of class "
                        + ForeignCollection.class.getSimpleName() + " or Collection.");
            }
            Type type = field.getGenericType();
            if (!(type instanceof ParameterizedType)) {
                throw new SQLException("Field class for '" + field.getName() + "' must be a parameterized Collection.");
            }
            Type[] genericArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (genericArguments.length == 0) {
                // i doubt this will ever be reached
                throw new SQLException("Field class for '" + field.getName()
                        + "' must be a parameterized Collection with at least 1 type.");
            }
        } else if (dataPersister == null && (!fieldConfig.isForeignCollection())) {
            if (byte[].class.isAssignableFrom(clazz)) {
                throw new SQLException("ORMLite does not know how to store " + clazz + " for field '" + field.getName()
                        + "'. byte[] fields must specify dataType=DataType.BYTE_ARRAY or SERIALIZABLE");
            } else if (Serializable.class.isAssignableFrom(clazz)) {
                throw new SQLException("ORMLite does not know how to store " + clazz + " for field '" + field.getName()
                        + "'.  Use another class, custom persister, or to serialize it use "
                        + "dataType=DataType.SERIALIZABLE");
            } else {
                throw new IllegalArgumentException("ORMLite does not know how to store " + clazz + " for field "
                        + field.getName() + ". Use another class or a custom persister.");
            }
        }
        */
        if (fieldConfig.getColumnName() == null) {
            this.columnName = defaultFieldName;
        } else {
            this.columnName = fieldConfig.getColumnName();
        }
        this.fieldConfig = fieldConfig;
        if (fieldConfig.isId()) {
            if (fieldConfig.isGeneratedId() || fieldConfig.getGeneratedIdSequence() != null) {
                throw new IllegalArgumentException(
                        "Must specify one of id, generatedId, and generatedIdSequence with " + field.getName());
            }
            this.isId = true;
            this.isGeneratedId = false;
            this.generatedIdSequence = null;
        } else if (fieldConfig.isGeneratedId()) {
            if (fieldConfig.getGeneratedIdSequence() != null) {
                throw new IllegalArgumentException(
                        "Must specify one of id, generatedId, and generatedIdSequence with " + field.getName());
            }
            this.isId = true;
            this.isGeneratedId = true;
            if (databaseType.isIdSequenceNeeded()) {
                this.generatedIdSequence = databaseType.generateIdSequenceName(tableName, this);
            } else {
                this.generatedIdSequence = null;
            }
        } else if (fieldConfig.getGeneratedIdSequence() != null) {
            this.isId = true;
            this.isGeneratedId = true;
            String seqName = fieldConfig.getGeneratedIdSequence();
            if (databaseType.isEntityNamesMustBeUpCase()) {
                seqName = databaseType.upCaseEntityName(seqName);
            }
            this.generatedIdSequence = seqName;
        } else {
            this.isId = false;
            this.isGeneratedId = false;
            this.generatedIdSequence = null;
        }
        if (this.isId && (fieldConfig.isForeign() || fieldConfig.isForeignAutoRefresh())) {
            throw new IllegalArgumentException("Id field " + field.getName() + " cannot also be a foreign object");
        }
        if (fieldConfig.isUseGetSet()) {
            this.fieldGetMethod = DatabaseFieldConfig.findGetMethod(field, true);
            this.fieldSetMethod = DatabaseFieldConfig.findSetMethod(field, true);
        } else {
            if (!field.isAccessible()) {
                try {
                    this.field.setAccessible(true);
                } catch (SecurityException e) {
                    throw new IllegalArgumentException("Could not open access to field " + field.getName()
                            + ".  You may have to set useGetSet=true to fix.");
                }
            }
            this.fieldGetMethod = null;
            this.fieldSetMethod = null;
        }
        if (fieldConfig.isAllowGeneratedIdInsert() && !fieldConfig.isGeneratedId()) {
            throw new IllegalArgumentException(
                    "Field " + field.getName() + " must be a generated-id if allowGeneratedIdInsert = true");
        }
        if (fieldConfig.isForeignAutoRefresh() && !fieldConfig.isForeign()) {
            throw new IllegalArgumentException(
                    "Field " + field.getName() + " must have foreign = true if foreignAutoRefresh = true");
        }
        if (fieldConfig.isForeignAutoCreate() && !fieldConfig.isForeign()) {
            throw new IllegalArgumentException(
                    "Field " + field.getName() + " must have foreign = true if foreignAutoCreate = true");
        }
        if (fieldConfig.getForeignColumnName() != null && !fieldConfig.isForeign()) {
            throw new IllegalArgumentException(
                    "Field " + field.getName() + " must have foreign = true if foreignColumnName is set");
        }
        /*
        if (fieldConfig.isVersion() && (dataPersister == null || !dataPersister.isValidForVersion())) {
            throw new IllegalArgumentException(
                    "Field " + field.getName() + " is not a valid type to be a version field");
        }
        assignDataType(databaseType, dataPersister);
        */

        String defaultStr = fieldConfig.getDefaultValue();
        if (defaultStr == null) {
            this.defaultValue = null;
        } else if (this.isGeneratedId) {
            throw new SQLException("Field '" + field.getName() + "' cannot be a generatedId and have a default value '"
                    + defaultStr + "'");
        } else {
            this.defaultValue = this.dataPersister.parseDefaultString(this, defaultStr);
        }
    }

    /**
     * Return An instantiated {@link FieldType} or null if the field does not have a {@link DatabaseField} annotation.
     */
    public static FieldType createFieldType(DatabaseType databaseType, String tableName, Field field,
                                            Class<?> parentClass) throws SQLException {
        DatabaseFieldConfig fieldConfig = DatabaseFieldConfig.fromField(databaseType, tableName, field);
        if (fieldConfig == null) {
            return null;
        } else {
            return new FieldType(databaseType, tableName, field, fieldConfig, parentClass);
        }
    }

    /**
     * Return whether the field is an id field. It is an id if {@link DatabaseField#id},
     * {@link DatabaseField#generatedId}, OR {@link DatabaseField#generatedIdSequence} are enabled.
     */
    public boolean isId() {
        return isId;
    }

    /**
     * Return whether the field is a generated-id field. This is true if {@link DatabaseField#generatedId} OR
     * {@link DatabaseField#generatedIdSequence} are enabled.
     */
    public boolean isGeneratedId() {
        return isGeneratedId;
    }

    /**
     * Return whether the field is a generated-id-sequence field. This is true if
     * {@link DatabaseField#generatedIdSequence} is specified OR if {@link DatabaseField#generatedId} is enabled and the
     * {@link DatabaseType#isIdSequenceNeeded} is enabled. If the latter is true then the sequence name will be
     * auto-generated.
     */
    public boolean isGeneratedIdSequence() {
        return generatedIdSequence != null;
    }

    /**
     * Call through to {@link DatabaseFieldConfig#isForeignAutoCreate()}
     */
    public boolean isForeignAutoCreate() {
        return fieldConfig.isForeignAutoCreate();
    }

    public String getColumnName() {
        return columnName;
    }

    /**
     * Call through to {@link DatabaseFieldConfig#getColumnDefinition()}
     */
    public String getColumnDefinition() {
        return fieldConfig.getColumnDefinition();
    }

    public String getUniqueIndexName() {
        return fieldConfig.getUniqueIndexName(tableName);
    }

    public String getIndexName() {
        return fieldConfig.getIndexName(tableName);
    }

    /**
     * Return the format of the field.
     */
    public String getFormat() {
        return fieldConfig.getFormat();
    }

    /**
     * Return the class of the field associated with this field type.
     */
    public Class<?> getType() {
        return field.getType();
    }

    public Enum<?> getUnknownEnumVal() {
        return fieldConfig.getUnknownEnumValue();
    }

    public int getWidth() {
        return fieldConfig.getWidth();
    }

    public DataPersister getDataPersister() {
        return dataPersister;
    }

    public boolean isUnique() {
        return fieldConfig.isUnique();
    }

    public boolean isUniqueCombo() {
        return fieldConfig.isUniqueCombo();
    }

    public boolean isCanBeNull() {
        return fieldConfig.isCanBeNull();
    }

    /**
     * Call through to {@link DataPersister#isSelfGeneratedId()}
     */
    public boolean isSelfGeneratedId() {
        return dataPersister.isSelfGeneratedId();
    }

    /**
     * Return the default value as parsed from the {@link DatabaseFieldConfig#getDefaultValue()} or null if no default
     * value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Call through to {@link DataPersister#isEscapedDefaultValue()}
     */
    public boolean isEscapedDefaultValue() {
        return dataPersister.isEscapedDefaultValue();
    }

    public SqlType getSqlType() {
        return dataPersister.getSqlType();
    }
}
