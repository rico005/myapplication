package com.example.fangsheng.myapplication.orm.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fangsheng.myapplication.orm.db.DatabaseType;
import com.example.fangsheng.myapplication.orm.field.FieldType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fangsheng on 2016/10/24.
 */

public class TableUtils {

    private static final String TAG = "TableUtils";

    /**
     * Return an ordered collection of SQL statements that need to be run to create a table. To do the work of creating,
     * you should call {@link #getCreateTableStatements}.
     *
     * @param databaseType
     *            Our connect source which is used to get the database type, not to apply the creates.
     * @param dataClass
     *            The class for which a table will be created.
     * @return The collection of table create statements.
     */
    public static <T, ID> List<String> getCreateTableStatements(DatabaseType databaseType, Class<T> dataClass)
            throws SQLException {
        TableInfo<T, ID> tableInfo = new TableInfo<T, ID>(databaseType, dataClass);
        return addCreateTableStatements(databaseType, tableInfo, false);
    }

    private static <T, ID> List<String> addCreateTableStatements(DatabaseType databaseType,
                                                                 TableInfo<T, ID> tableInfo, boolean ifNotExists) throws SQLException {
        List<String> statements = new ArrayList<String>();
        List<String> queriesAfter = new ArrayList<String>();
        addCreateTableStatements(databaseType, tableInfo, statements, queriesAfter, ifNotExists);
        return statements;
    }

    /**
     * Generate and return the list of statements to create a database table and any associated features.
     */
    private static <T, ID> void addCreateTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo,
                                                         List<String> statements, List<String> queriesAfter, boolean ifNotExists) throws SQLException {
        StringBuilder sb = new StringBuilder(256);
        sb.append("CREATE TABLE ");
        if (ifNotExists && databaseType.isCreateIfNotExistsSupported()) {
            sb.append("IF NOT EXISTS ");
        }
        databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
        sb.append(" (");
        List<String> additionalArgs = new ArrayList<String>();
        List<String> statementsBefore = new ArrayList<String>();
        List<String> statementsAfter = new ArrayList<String>();
        // our statement will be set here later
        boolean first = true;
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
//            // skip foreign collections
//            if (fieldType.isForeignCollection()) {
//                continue;
//            } else
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            String columnDefinition = fieldType.getColumnDefinition();
            if (columnDefinition == null) {
                // we have to call back to the database type for the specific create syntax
                databaseType.appendColumnArg(tableInfo.getTableName(), sb, fieldType, additionalArgs, statementsBefore,
                        statementsAfter, queriesAfter);
            } else {
                // hand defined field
                databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
                sb.append(' ').append(columnDefinition).append(' ');
            }
        }
        // add any sql that sets any primary key fields
        databaseType.addPrimaryKeySql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter,
                queriesAfter);
        // add any sql that sets any unique fields
        databaseType.addUniqueComboSql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter,
                queriesAfter);
        for (String arg : additionalArgs) {
            // we will have spat out one argument already so we don't have to do the first dance
            sb.append(", ").append(arg);
        }
        sb.append(") ");
        databaseType.appendCreateTableSuffix(sb);
        statements.addAll(statementsBefore);
        statements.add(sb.toString());
        statements.addAll(statementsAfter);
        addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, false);
        addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, true);
    }

    private static <T, ID> void addCreateIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo,
                                                         List<String> statements, boolean ifNotExists, boolean unique) {
        // run through and look for index annotations
        Map<String, List<String>> indexMap = new HashMap<String, List<String>>();
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            String indexName;
            if (unique) {
                indexName = fieldType.getUniqueIndexName();
            } else {
                indexName = fieldType.getIndexName();
            }
            if (indexName == null) {
                continue;
            }

            List<String> columnList = indexMap.get(indexName);
            if (columnList == null) {
                columnList = new ArrayList<String>();
                indexMap.put(indexName, columnList);
            }
            columnList.add(fieldType.getColumnName());
        }

        StringBuilder sb = new StringBuilder(128);
        for (Map.Entry<String, List<String>> indexEntry : indexMap.entrySet()) {
            Log.d(TAG, "creating index '{" + indexEntry.getKey() + "}' for table '{" + tableInfo.getTableName() + "}");
            sb.append("CREATE ");
            if (unique) {
                sb.append("UNIQUE ");
            }
            sb.append("INDEX ");
            if (ifNotExists && databaseType.isCreateIndexIfNotExistsSupported()) {
                sb.append("IF NOT EXISTS ");
            }
            databaseType.appendEscapedEntityName(sb, indexEntry.getKey());
            sb.append(" ON ");
            databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
            sb.append(" ( ");
            boolean first = true;
            for (String columnName : indexEntry.getValue()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                databaseType.appendEscapedEntityName(sb, columnName);
            }
            sb.append(" )");
            statements.add(sb.toString());
            sb.setLength(0);
        }
    }

    public static <T, ID> List<String> getDropTableStatements(DatabaseType databaseType, Class<T> dataClass)
            throws SQLException {
        TableInfo<T, ID> tableInfo = new TableInfo<T, ID>(databaseType, dataClass);
        return addDropTableStatements(databaseType, tableInfo, true);
    }

    private static <T, ID> List<String> addDropTableStatements(DatabaseType databaseType,
                                                                 TableInfo<T, ID> tableInfo, boolean ifNotExists) throws SQLException {
        List<String> statements = new ArrayList<String>();
        addDropTableStatements(databaseType, tableInfo, statements, ifNotExists);
        return statements;
    }

    private static <T, ID> void addDropTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo,
                                                         List<String> statements, boolean ifNotExists) throws SQLException {
        List<String> statementsBefore = new ArrayList<String>();
        List<String> statementsAfter = new ArrayList<String>();
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            databaseType.dropColumnArg(fieldType, statementsBefore, statementsAfter);
        }
        StringBuilder sb = new StringBuilder(64);
        sb.append("DROP TABLE ");
        if (ifNotExists) {
            sb.append("IF EXISTS ");
        }
        databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
        sb.append(' ');
        statements.addAll(statementsBefore);
        statements.add(sb.toString());
        statements.addAll(statementsAfter);

        addDropIndexStatements(databaseType, tableInfo, statements);
    }

    private static <T, ID> void addDropIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo,
                                                       List<String> statements) {
        // run through and look for index annotations
        Set<String> indexSet = new HashSet<String>();
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            String indexName = fieldType.getIndexName();
            if (indexName != null) {
                indexSet.add(indexName);
            }
            String uniqueIndexName = fieldType.getUniqueIndexName();
            if (uniqueIndexName != null) {
                indexSet.add(uniqueIndexName);
            }
        }

        StringBuilder sb = new StringBuilder(48);
        for (String indexName : indexSet) {
            Log.d(TAG, "dropping index '{" + indexName + "}' for table '{" + tableInfo.getTableName() + "}");
            sb.append("DROP INDEX ");
            databaseType.appendEscapedEntityName(sb, indexName);
            statements.add(sb.toString());
            sb.setLength(0);
        }
    }

    /**
     * 获取表的列名
     * @param db
     * @param tableName
     * @return
     */
    private static String[] getColumnNames(SQLiteDatabase db, String tableName){
        String[] columnNames = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info("+tableName+")",null);
            if(cursor != null){
                int columnIndex = cursor.getColumnIndex("name");
                if(columnIndex == -1){
                    return null;
                }

                int index = 0;
                columnNames = new String[cursor.getCount()];
                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    columnNames[index] = cursor.getString(columnIndex);
                    index++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return columnNames;
    }

    /**
     * 获取表名
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> String extractTableName(Class<T> clazz) {
        return DatabaseTableConfig.extractTableName(clazz);
    }
}
