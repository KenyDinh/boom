package dev.boom.dao;

import java.sql.Connection;

@FunctionalInterface
public interface FunctionTransaction {
	public abstract boolean transaction(Connection conn);
}
