package com.example.fangsheng.myapplication.orm.field.types;

import com.example.fangsheng.myapplication.orm.field.FieldType;
import com.example.fangsheng.myapplication.orm.field.SqlType;
import com.example.fangsheng.myapplication.orm.support.DatabaseResults;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Type that persists a byte[] object.
 * 
 * @author graywatson
 */
public class ByteArrayType extends BaseDataType {

	private static final ByteArrayType singleTon = new ByteArrayType();

	public static ByteArrayType getSingleton() {
		return singleTon;
	}

	private ByteArrayType() {
		super(SqlType.BYTE_ARRAY);
	}

	/**
	 * Here for others to subclass.
	 */
	protected ByteArrayType(SqlType sqlType, Class<?>[] classes) {
		super(sqlType, classes);
	}

	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) {
		if (defaultStr == null) {
			return null;
		} else {
			return defaultStr.getBytes();
		}
	}

	@Override
	public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
		return (byte[]) results.getBytes(columnPos);
	}

	@Override
	public boolean isArgumentHolderRequired() {
		return true;
	}

	@Override
	public boolean dataIsEqual(Object fieldObj1, Object fieldObj2) {
		if (fieldObj1 == null) {
			return (fieldObj2 == null);
		} else if (fieldObj2 == null) {
			return false;
		} else {
			return Arrays.equals((byte[]) fieldObj1, (byte[]) fieldObj2);
		}
	}

	@Override
	public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) {
		return stringValue.getBytes();
	}

	@Override
	public Class<?> getPrimaryClass() {
		return byte[].class;
	}
}
