package com.example.fangsheng.myapplication.orm.db;

import com.example.fangsheng.myapplication.orm.field.FieldType;
import com.example.fangsheng.myapplication.orm.field.SqlType;

import java.util.List;

/**
 * Created by fangsheng on 2016/10/25.
 */

public class BaseSqliteDatabaseType extends BaseDatabaseType{
    @Override
    protected String getDatabaseName() {
        return "Amp";
    }

    @Override
    protected void appendLongType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		/*
		 * This is unfortunate. SQLIte requires that a generated-id have the string "INTEGER PRIMARY KEY AUTOINCREMENT"
		 * even though the maximum generated value is 64-bit. See configureGeneratedId below.
		 */
        if (fieldType.getSqlType() == SqlType.LONG && fieldType.isGeneratedId()) {
            sb.append("INTEGER");
        } else {
            sb.append("BIGINT");
        }
    }

    @Override
    protected void configureGeneratedId(String tableName, StringBuilder sb, FieldType fieldType,
                                        List<String> statementsBefore, List<String> statementsAfter, List<String> additionalArgs,
                                        List<String> queriesAfter) {
		/*
		 * Even though the documentation talks about INTEGER, it is 64-bit with a maximum value of 9223372036854775807.
		 * See http://www.sqlite.org/faq.html#q1 and http://www.sqlite.org/autoinc.html
		 */
        if (fieldType.getSqlType() != SqlType.INTEGER && fieldType.getSqlType() != SqlType.LONG) {
            throw new IllegalArgumentException(
                    "Sqlite requires that auto-increment generated-id be integer or long type");
        }
        sb.append("PRIMARY KEY AUTOINCREMENT ");
        // no additional call to configureId here
    }


    @Override
    public boolean isVarcharFieldWidthSupported() {
        return false;
    }

    @Override
    public boolean isCreateIfNotExistsSupported() {
        return true;
    }
}
