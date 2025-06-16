package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Console;
import it.uniroma3.siw.model.Games;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ConsoleRepository;
import it.uniroma3.siw.repository.GameRepository;

@Service
public class ConsoleService {
	
	@Autowired
	private ConsoleRepository consoleRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private ReviewService reviewService;
	
	public Console findConsoleById(Long id) {
		return this.consoleRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public void createNewConsole(Console console) {
		this.consoleRepository.save(console);
	}
	
	public Console getConsoleById(Long id) {
        return this.consoleRepository.findById(id).get();
    } 	
	
	@Transactional
	public void saveConsole(@Valid Console console) {
		this.consoleRepository.save(console);
	}
	
	public Iterable<Console> getConsoles(){
		return this.consoleRepository.findAll();
	}
	
	public Console saveGiocoEsclusivoToConsole(Long consoleId, Long gameId) {
		Console res = null;
		Games giocoEsclusivo = gameRepository.findById(gameId).orElse(null);
		Console console = this.findConsoleById(consoleId);
		if(console != null && giocoEsclusivo != null) {
			console.setGiocoEsclusivo(giocoEsclusivo); //imposta il director al movie
			this.saveConsole(console); //salva il movie
			res = console;
		}
		return res;
	}
	
	public Console saveGameToConsole(Long idC, Long idG) {
		Console console = consoleRepository.findById(idC).get(); //trova il movie e estrarlo dalla repository
		Games game = gameRepository.findById(idG).get(); // la stessa cosa qua
		if(console != null && game != null) {
			List<Games> games = console.getGames(); // estrarre tutti gli attori del film
			games.add(game); //aggiungi l'attore estratto
			return this.consoleRepository.save(console); // salva il film nella repository
		}
		return console; // se non compie la condizione allora ritorna il movie originale
	}
	
	@Transactional
    public Console addReviewToConsole(Long consoleId, Long reviewId) {
        Console console = this.getConsoleById(consoleId);
        Review review = this.reviewService.getReviewById(reviewId);
        console.getReviews().add(review);
//        List<Review> reviews = movie.getReviews();
//        reviews.add(review);
//        movie.setReviews(reviews);
//        review.setMovie(movie);
        return this.consoleRepository.save(console);
    }
	
	public List<Review> findAllReviewInConsole(Long consoleId){
		Console console = this.getConsoleById(consoleId);
		return console.getReviews();
	}
	
	@Transactional
	public void deleteConsole(Long consoleId) {
		Console console = this.getConsoleById(consoleId); // prende l'id del movie
//		List<Artist> actors = movie.getActors(); // prende tutti gli attori del movie
//		for (Artist actor : actors) {
//			actor.getStarredMovies().remove(movie);
//		}
//		if (movie.getDirector()!=null) {
//			movie.getDirector().getDirectedMovies().remove(movie);
//		}
		List<Review> reviews = console.getReviews();
		for (Review review : reviews) {
			review.setConsole(null);
		}
		this.consoleRepository.delete(console);
	}
	
	@Transactional
	public Console updateConsole(Console oldConsole, Console newConsole) {
		oldConsole.setName(newConsole.getName());
		oldConsole.setAnno(newConsole.getAnno());
		return this.consoleRepository.save(oldConsole);
	}

	public List<Console> getAllConsoleByAsc() {
		return this.consoleRepository.findByOrderByAnnoAsc();
	}

	@Transactional
	public Console removeGameFromConsole(Long consoleId, Long gameId) {
		Console console = this.getConsoleById(consoleId); // mi dà il consoleId
		Games game = this.gameService.getGameById(gameId); // mi dà il gameId che voglio rimuovere
		List<Games> games = console.getGames(); // prendi tutti i giochi della console
		games.remove(game); // rimuove il gioco preso prima
		this.consoleRepository.save(console); // salva la console
		return console;
	}

	public List<Games> findGamesNotInConsole(Long consoleId) {
		List<Games> gamesToAdd = new ArrayList<>();

		for (Games a : gameService.findGamesNotInConsole(consoleId)) {
			gamesToAdd.add(a);
		}
		return gamesToAdd;
	}
	
	@Transactional
	public Console addGameToConsole(Long consoleId, Long gameId) {
		Console console = this.consoleRepository.findById(consoleId).get();
		Games game = this.gameRepository.findById(gameId).get();
		List<Games> games = console.getGames(); //prende la lista di tutti gli attori del film
		games.add(game); //aggiunge l'attore alla lista degli attori
		this.consoleRepository.save(console); //salva il movie aggiornato
		return console;
	}
}
