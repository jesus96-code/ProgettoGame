package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Console;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;


public interface ReviewRepository extends CrudRepository<Review, Long>{
	
//	@Query(value="select * "
//			+ "from recensione r "
//			+ "where r.id not in "
//			+ "(select rencesione_id "
//			+ "from movie_recensione "
//			+ "where movie_recensione.starred_movies_id = :movieId)", nativeQuery=true)
//	public Iterable<Recensione> findRecensioniNotInMovie(@Param("movieId") Long id);

	public List<Review> findByOrderByRatingAsc();

	public List<Review> findAllByConsoleId(Long id);

	public boolean existsByReviewerAndConsole(User reviewer, Console console);
	


}
