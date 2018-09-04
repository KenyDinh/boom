package dev.boom.connect;

import java.util.List;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

public class MySQLBitwiseSQLFunction extends StandardSQLFunction {
	
	public static final String BITWISE_OR = "|";
	public static final String BITWISE_AND = "&";
	
	public String operator;

	public MySQLBitwiseSQLFunction(String name) {
		super(name);
	}

	public MySQLBitwiseSQLFunction(String name, Type registeredType, String operator) {
		super(name, registeredType);
		this.operator = operator;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("MySQLBitwiseSQLFunction requires 2 arguments!");
		}
		if (operator == null) {
			throw new IllegalArgumentException("MySQLBitwiseSQLFunction's operator is required!");
		} 
		StringBuffer buffer = new StringBuffer(arguments.get(0).toString());
		buffer.append(" " + operator + " ").append(arguments.get(1));
		return buffer.toString();
	}

}
