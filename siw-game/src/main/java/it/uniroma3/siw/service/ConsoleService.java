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
			console.setGiocoEsclusivo(giocoEsclusivo); //imposta un gioco esclusivo alla console
			this.saveConsole(console); //salva la console
			res = console;
		}
		return res;
	}
	
	public Console saveGameToConsole(Long idC, Long idG) {
		Console console = consoleRepository.findById(idC).get(); //trova la console e estrarlo dalla repository
		Games game = gameRepository.findById(idG).get(); // la stessa cosa qua
		if(console != null && game != null) {
			List<Games> games = console.getGames(); // estrarre tutti i giochi della console
			games.add(game); //aggiungi il giochi estratto
			return this.consoleRepository.save(console); // salva la console nella repository
		}
		return console; // se non compie la condizione allora ritorna la console originale
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
		Console console = this.getConsoleById(consoleId); // prende l'id della console
		List<Review> reviews = console.getReviews(); //prende tutte le recensioni della console
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
		List<Games> games = console.getGames(); //prende la lista di tutti i giochi della console
		games.add(game); //aggiunge il gioco alla lista dei giochi
		this.consoleRepository.save(console); //salva la console aggiornato
		return console;
	}
}
