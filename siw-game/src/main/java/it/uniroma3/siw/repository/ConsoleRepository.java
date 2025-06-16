package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Console;

@Repository
public interface ConsoleRepository extends CrudRepository<Console, Long> {

	public List<Console> findByAnno(int anno);
	
	public List<Console> findByName(String name);
	
	public boolean existsByNameAndAnno(String name, int anno);
	
	//metodo che ordina i film in base all'anno
	public List<Console> findByOrderByAnnoAsc();
	
	//metodo che ordina i film in base al titolo
	public List<Console> findByOrderByNameAsc();
	
}