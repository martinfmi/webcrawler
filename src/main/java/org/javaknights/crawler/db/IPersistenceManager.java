package org.javaknights.crawler.db;

import org.javaknights.crawler.entities.Website;

public interface IPersistenceManager {

	void init();
	
	void executeUpdate(String query);
	
	void executeUpdate(Object object) throws IllegalArgumentException, IllegalAccessException;
	
	void executeUpdateWithJpa(Object object);

	void executeUpdateWithHibernate(Object object);

	void executeUpdateWithSpringData(Website object);
}
