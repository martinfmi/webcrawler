package org.javaknights.crawler.db;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.javaknights.crawler.ApplicationProperties;
import org.javaknights.crawler.entities.Website;
import org.javaknights.crawler.repositories.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

public class PersistenceManager implements IPersistenceManager
	/* ,InitializingBean, DisposableBean */ {
	
	private static final Logger LOGGER = Logger.getLogger(PersistenceManager.class.getName());
	
	@Autowired
	private ApplicationProperties properties;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
	private LocalSessionFactoryBean sessionFactory;

//	@Autowired
	private LocalEntityManagerFactoryBean entityManager;
	
	@Autowired
	private WebsiteRepository websiteRepository;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Initializing database ...");
	}
	
	@PreDestroy
	public void destroy() {
		LOGGER.info("Closing connection to database ...");
	}
	
	public void executeUpdate(String query) {
		jdbcTemplate.update(query);
	}
	
	public static String generateInsertQuery(Object entity) throws IllegalArgumentException, IllegalAccessException {
		
		String table = entity.getClass().getSimpleName().toLowerCase();
		
		StringBuilder query = new StringBuilder("insert into ");
		query.append(table).append("(");

		Field[] fields = entity.getClass().getDeclaredFields();
		for(int index = 0; index < fields.length; index++) {
			Field field = fields[index];
			field.setAccessible(true);
			if(!"id".equals(field.getName())) {
				query.append(field.getName());
				if(index != fields.length - 1) {
					query.append(",");
				}
			}
		}
		query.append(") values (");
		
		for(int index = 0; index < fields.length; index++) {
			Field field = fields[index];
			if(!"id".equals(field.getName())) {
				if(field.get(entity) == null) {
					query.append("NULL");
				} else {
					if("html".equals(field.getName())) {
						
						String value = Base64.getEncoder().encodeToString(field.get(entity).toString().getBytes());
						query.append("'"). append(value).append("'");
					} else {
						query.append("'"). append(field.get(entity).toString()).append("'");
					}
				}
				if(index != fields.length - 1) {
					query.append(",");
				}
			}
		}
		query.append(")");
		return query.toString();
	}

	@Override
	public void executeUpdate(Object object) throws IllegalArgumentException, IllegalAccessException {
		String table = object.getClass().getSimpleName().toLowerCase();
		
		SimpleJdbcInsert insertQuery = new SimpleJdbcInsert(dataSource)
				.withTableName(table);
		Map<String, Object> params = new HashMap<>();
		
		Field[] fields = object.getClass().getDeclaredFields();
		for(int index = 0; index < fields.length; index++) {
			Field field = fields[index];
			field.setAccessible(true);
			if(!"id".equals(field.getName())) {
				
				String key = field.getName();
				String value = null;
				if(field.get(object) == null) {
					value = "NULL";
				} else {
					if("html".equals(field.getName())) {
						value = Base64.getEncoder().encodeToString(field.get(object).toString().getBytes());
					} else {
						value = field.get(object).toString();
					}
				}
				params.put(key, value);
			}
		}
		
		insertQuery.execute(params);
	}

	@Override
//	@Transactional
	public void executeUpdateWithJpa(Object object) {
		EntityManager manager = entityManager.getObject().createEntityManager();
		manager.persist(object);
	}
	 
	@Override
//	@Transactional
	public void executeUpdateWithHibernate(Object object) {
		Session session = sessionFactory.getObject().openSession();
		session.save(object);
	}

	@Override
	public void executeUpdateWithSpringData(Website object) {
		
		List<Website> websites = 
				websiteRepository.findByUrlContainingIgnoreCase("http");
		System.out.println(websites);
		
		websiteRepository.save(object);
	}
	
}
