package dev.boom.connect;

import org.hibernate.type.IntegerType;

public class MySQLDialect extends org.hibernate.dialect.MySQLDialect {
	
	public static final String BITWISE_FUNCTION_AND = "bitwise_and";

	public MySQLDialect() {
		super();
		registerFunction(BITWISE_FUNCTION_AND, new MySQLBitwiseSQLFunction(BITWISE_FUNCTION_AND, IntegerType.INSTANCE, MySQLBitwiseSQLFunction.BITWISE_AND));
	}

}
