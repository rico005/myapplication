package com.example.fangsheng.myapplication.orm.field.types;

import com.example.fangsheng.myapplication.orm.field.SqlType;

import java.util.UUID;

/**
 * Type that persists a {@link UUID} object but as a UUID type which is supported by a couple of database-types.
 * 
 * @author graywatson
 */
public class NativeUuidType extends UuidType {

	private static final NativeUuidType singleTon = new NativeUuidType();

	public static NativeUuidType getSingleton() {
		return singleTon;
	}

	private NativeUuidType() {
		super(SqlType.UUID);
	}

	/**
	 * Here for others to subclass.
	 */
	protected NativeUuidType(SqlType sqlType, Class<?>[] classes) {
		super(sqlType, classes);
	}
}
