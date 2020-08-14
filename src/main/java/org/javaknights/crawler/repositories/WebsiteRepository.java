package org.javaknights.crawler.repositories;

import java.util.List;

import org.javaknights.crawler.entities.Website;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends CrudRepository<Website, Integer>{
	/* extends JpaRepository<Website, Integer> */
	
	/* select * from website where url = <url> and id = <id> */
	public List<Website> findByUrlAndId(String url, Integer id);
	
	public List<Website> findByUrlContainingIgnoreCase(String text);
	
	@Query("select w from Website w where w.url like 'https%'")
	public List<Website> findByHttpsUrlCustom();
}
