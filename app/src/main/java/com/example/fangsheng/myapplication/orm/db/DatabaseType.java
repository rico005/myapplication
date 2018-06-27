package com.example.fangsheng.myapplication.orm.db;

import com.example.fangsheng.myapplication.orm.field.FieldType;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fangsheng on 2016/10/25.
 */

public interface DatabaseType {

    /**
     * Add a entity-name word to the string builder wrapped in the proper characters to escape it. This avoids problems
     * with table, column, and sequence-names being reserved words.
     */
    public void appendEscapedEntityName(StringBuilder sb, String word);

    /**
     * Returns true if the table creation IF NOT EXISTS syntax is supported.
     */
    public boolean isCreateIfNotExistsSupported();

    /**
     * Takes a {@link FieldType} and appends the SQL necessary to create the field to the string builder. The field may
     * also generate additional arguments which go at the end of the insert statement or additional statements to be
     * executed before or afterwards depending on the configurations. The database can also add to the list of queries
     * that will be performed afterward to test portions of the config.
     */
    public void appendColumnArg(String tableName, StringBuilder sb, FieldType fieldType, List<String> additionalArgs,
                                List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) throws SQLException;

    /**
     * Appends information about primary key field(s) to the additional-args or other lists.
     */
    public void addPrimaryKeySql(FieldType[] fieldTypes, List<String> additionalArgs, List<String> statementsBefore,
                                 List<String> statementsAfter, List<String> queriesAfter) throws SQLException;

    /**
     * Appends information about unique field(s) to the additional-args or other lists.
     */
    public void addUniqueComboSql(FieldType[] fieldTypes, List<String> additionalArgs, List<String> statementsBefore,
                                  List<String> statementsAfter, List<String> queriesAfter) throws SQLException;

    /**
     * Append the SQL necessary to properly finish a CREATE TABLE line.
     */
    public void appendCreateTableSuffix(StringBuilder sb);

    /**
     * Returns true if table and field names should be made uppercase.
     *
     * <p>
     * Turns out that Derby and Hsqldb are doing something wrong (IMO) with entity names. If you create a table with the
     * name "footable" (with the quotes) then it will be created as lowercase footable, case sensitive. However, if you
     * then issue the query 'select * from footable' (without quotes) it won't find the table because it gets promoted
     * to be FOOTABLE and is searched in a case sensitive manner. So for these databases, entity names have to be forced
     * to be uppercase so external queries will also work.
     * </p>
     */
    public boolean isEntityNamesMustBeUpCase();

    /**
     * Return true if the database needs a sequence when you use generated IDs. Some databases (H2, MySQL) create them
     * auto-magically. This also means that the database needs to query for a sequence value <i>before</i> the object is
     * inserted. For old[er] versions of Postgres, for example, the JDBC call-back stuff to get the just-inserted id
     * value does not work so we have to get the next sequence value by hand, assign it into the object, and then insert
     * the object -- yes two SQL statements.
     */
    public boolean isIdSequenceNeeded();

    /**
     * Return the name of an ID sequence based on the tabelName and the fieldType of the id.
     */
    public String generateIdSequenceName(String tableName, FieldType idFieldType);

    /**
     * Returns the uppercase version of an entity name. This is here in case someone wants to override the behavior of
     * the default method because of localization issues.
     */
    public String upCaseEntityName(String entityName);

    /**
     * Does the database support the "CREATE INDEX IF NOT EXISTS" SQL construct. By default this just calls
     * {@link #isCreateIfNotExistsSupported()}.
     */
    public boolean isCreateIndexIfNotExistsSupported();

    /**
     * Takes a {@link FieldType} and adds the necessary statements to the before and after lists necessary so that the
     * dropping of the table will succeed and will clear other associated sequences or other database artifacts
     */
    public void dropColumnArg(FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter);
}
