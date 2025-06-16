package it.uniroma3.siw.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Games;

public interface GameRepository extends CrudRepository<Games, Long> {

	public boolean existsByName(String name);	

	@Query(value="select * "
			+ "from games g "
			+ "where g.id not in "
			+ "(select games_id "
			+ "from console_games "
			+ "where console_games.starred_consoles_id = :consoleId)", nativeQuery=true)
	
	public Iterable<Games> findGamesNotInConsole(@Param("consoleId") Long id);
	
	//public int contaAttori(@Param("movieId") Long id);


}