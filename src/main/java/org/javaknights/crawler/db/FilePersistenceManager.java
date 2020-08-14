package org.javaknights.crawler.db;

import java.util.Properties;

import org.javaknights.crawler.entities.Website;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier("filepersistencemanager")
@Component
public class FilePersistenceManager implements IPersistenceManager {

	@Override
	public void init() {
		System.out.println("file manager init ...");
	}

	@Override
	public void executeUpdate(String query) {
		System.out.println("file manager execute query ...");
	}

	@Override
	public void executeUpdate(Object object) throws IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeUpdateWithJpa(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeUpdateWithHibernate(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeUpdateWithSpringData(Website object) {
		// TODO Auto-generated method stub
		
	}

}
