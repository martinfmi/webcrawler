package org.javaknights.crawler.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.javaknights.crawler.Application;
import org.javaknights.crawler.entities.Link;
import org.javaknights.crawler.entities.SitesInput;
import org.javaknights.crawler.entities.Website;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app")
public class ApplicationController {
	
	@Autowired
	private Application application;
	
	// localhost:8080/app/sitecontent?sites=http://abv.bg
//	@RequestMapping(method = RequestMethod.GET, value = "/sitecontent/{index}")
	@GetMapping(produces = "application/json", value = {"/sitecontent", "/sitecontent/{index}"})
	public Website getSiteContent(@RequestParam(value = "sites", required = false) String sites,
			@PathVariable(value = "index", required = false) Optional<Integer> index,
			@RequestHeader("Accept") String acceptHeader) {

		int actualIndex = 1;
		if(index.isPresent()) {
			actualIndex = index.get();
		}
		
		System.out.println(sites);
		System.out.println(acceptHeader);

		String[] sitesList = sites.split(",");
		Website website = application.readWebsite(sitesList[actualIndex-1]);

		return website;
	}

	@RequestMapping(produces = "application/json",
				method = RequestMethod.GET, 
				value = "/sitelinks")
	public ArrayList<Link> getSiteLinks(
			@RequestParam(value = "sites", required = false) String sites) 
					throws NoValidParameterException {

		if(sites == null) {
			throw new NoValidParameterException("sites");
		}
		
		ArrayList<Link> result = new ArrayList<>();
		String[] sitesList = sites.split(",");
		for(String site : sitesList) {
			Website website = application.readWebsite(site);
			application.storeWebsite(website);
			List<String> links = application.parseLinks(website);
			for(String link : links) {
				Link linkEntity = new Link();
				linkEntity.setLink(link);
				result.add(linkEntity);
			}
		}
		
		return result;
	}

//	@Secured("APP_ADMIN")
//	@RolesAllowed("APP_ADMIN")
	@RequestMapping("/start")
	public String generateStart(Model model) {
		SitesInput siteInput = new SitesInput();
//		siteInput.setSites("https://test.com");
		model.addAttribute("siteInput", siteInput);
		return "start";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public String submit() throws NoValidParameterException {
		return "login";
	} 

	@RequestMapping(method = RequestMethod.POST, value = "/submit")
	public String submit(SitesInput siteInput, Model model) throws NoValidParameterException {
		
		ArrayList<Link> siteLinks = getSiteLinks(siteInput.getSites());
		model.addAttribute("links", siteLinks);
		return "response";
	}
	
}
