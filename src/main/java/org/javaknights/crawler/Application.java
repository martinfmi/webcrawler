package org.javaknights.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javaknights.crawler.db.IPersistenceManager;
import org.javaknights.crawler.db.PersistenceManager;
import org.javaknights.crawler.entities.Website;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
	
	private static final String DELIMITER_WEBSITE = ",";

	private static final String COMMAND_FILE = "file";

	private static final String COMMAND_SITES = "sites";

	private static Logger LOGGER = Logger.getLogger(Application.class.getName());

	private Map<String, Command> commands = new HashMap<>();
	
	@Autowired
	private ApplicationContext context;
	
	@Qualifier("dbpersistencemanager")
	@Autowired
	private IPersistenceManager manager;
	
	@Autowired
	private ThreadPool threadPool;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	private void executeCommand(String command, String params) {
		Command commandMethod = commands.get(command);
		if (commandMethod == null) {
			LOGGER.severe("Invalid command: " + command);
		} else {
			commandMethod.execute(params);
		}
	}

	private void initCommands() {
		LOGGER.info("Initializing commands ...");
		commands.put(COMMAND_SITES, this::handleCommandSites);
		commands.put(COMMAND_FILE, this::handleCommandFile);
	}

	private void validateArguments(String[] args, final int numArguments) {
		if (args.length != numArguments) {
			LOGGER.severe(String.format("Invalid number of arguments: %d, expected %d", args.length, numArguments));
//			System.exit(1);
		}
	}

	public void handleCommandSites(String arguments) {

		for (String website : arguments.split(DELIMITER_WEBSITE)) {

			threadPool.execute(() -> {
				Website websiteEntity = readWebsite(website);
				if (website != null) {
					parseLinks(websiteEntity);
					storeWebsite(websiteEntity);
				}
			});

		}
		
		try {
			threadPool.shutdown();
		} catch (InterruptedException e) {
		}

	}

	public void storeWebsite(Website websiteEntity) {

		try {
//			String insertQuery = PersistenceManager.generateInsertQuery(websiteEntity);
//			manager.executeUpdate(websiteEntity);
//			manager.executeUpdateWithHibernate(websiteEntity);
//			manager.executeUpdateWithJpa(websiteEntity);
			manager.executeUpdateWithSpringData(websiteEntity);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public List<String> parseLinks(Website websiteEntity) {

		ArrayList<String> links = new ArrayList<>();
		String html = websiteEntity.getHtml();

		Pattern pattern = Pattern.compile("(?:\")(http(s)?[^\"]+)(?:\")");
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			String link = matcher.group(1);
			links.add(link);
			LOGGER.info(String.format("[%s] %s", websiteEntity.getUrl(), link));
		}

		return links;
	}
	
	public Website readWebsite(String website) {
		BufferedReader reader = null;
		try {
			URL url = new URL(website);
			URLConnection connection = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(connection.getInputStream())));

			char[] buffer = new char[256];
			StringBuilder html = new StringBuilder();
			while (reader.read(buffer) != -1) {
				html.append(buffer);
			}

			Website websiteEntity = new Website();
			websiteEntity.setUrl(website);
			websiteEntity.setHtml(html.toString());
			return websiteEntity;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public void handleCommandFile(String arguments) {
		// ...
	}

}
