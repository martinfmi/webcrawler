package org.javaknights.crawler;

import java.util.List;

import org.javaknights.crawler.entities.Website;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationTest {

	@Test
	public void testParseLinks( ) {
		
		Application application = new Application();
		Website website = new Website();
		website.setHtml("<body><a href=\"http://test.com\" /></body>");
		List<String> links = application.parseLinks(website);
		Assertions.assertEquals(links.size(), 1);
		Assertions.assertEquals(links.get(0), "http://test.com");
	}
	
}
