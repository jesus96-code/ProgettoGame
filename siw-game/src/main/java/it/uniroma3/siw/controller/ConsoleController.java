package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.ConsoleValidator;
import it.uniroma3.siw.model.Console;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Games;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ConsoleRepository;
import it.uniroma3.siw.repository.GameRepository;
import it.uniroma3.siw.service.ConsoleService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.GameService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ConsoleController {
	
	@Autowired 
	private ConsoleRepository consoleRepository;
	
	@Autowired 
	private GameRepository gameRepository;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private ConsoleService consoleService;

	@Autowired 
	private ConsoleValidator consoleValidator;
	
	@Autowired 
	private CredentialsService credentialsService;
	
	@Autowired
	private ReviewService reviewService;

	@GetMapping(value="/admin/formNewConsole")
	public String formNewConsole(Model model) {
		model.addAttribute("console", new Console());
		return "admin/formNewConsole.html"; //il valore di ritorno che verrà mostrato quando viene soddisfata la richiesta
	}

	@GetMapping(value="/admin/formUpdateConsole/{id}")
	public String formUpdateConsole(@PathVariable("id") Long id, Model model) {
		//aggiunge al modello una console con un certo id
		model.addAttribute("console", consoleService.getConsoleById(id));
		return "admin/formUpdateConsole.html";
	}
	
	@GetMapping(value="/admin/conteggioGiochi/{id}")
	public String formConteggioGames(@PathVariable("id") Long id, Model model) {
		Console console = consoleService.findConsoleById(id);
		if(console != null) {
			model.addAttribute("gameCount", console.getGames().size());
			model.addAttribute("console", console);
		}
		else {
			model.addAttribute("console", null);
		}
		return "admin/conteggio.html";
	}

	@GetMapping(value="/admin/indexConsole")
	public String indexConsole() {
		return "admin/indexConsole.html";
	}
	
//	@GetMapping(value = "/admin/adminConsole/{id}")
//	public String adminMovie(@PathVariable("id") Long id, Model model) {
//		Console console = this.consoleService.getConsoleById(id);
//		model.addAttribute("console", console);
//		return "admin/adminConsole.html";
//	}
	
	@GetMapping(value="/admin/manageConsoles")
	public String manageConsoles(Model model) {
		model.addAttribute("consoles", this.consoleService.getConsoles());
		return "admin/manageConsoles.html";
	}
	
	@GetMapping(value="/admin/setGiocoEsclusivoToConsole/{giocoEsclusivoId}/{consoleId}")
	public String setGiocoEsclusivoToConsole(@PathVariable("giocoEsclusivoId") Long giocoEsclusivoId, @PathVariable("consoleId") Long consoleId, Model model) {	
		Console console =  this.consoleService.saveGiocoEsclusivoToConsole(consoleId, giocoEsclusivoId);		
		model.addAttribute("console", console);
		return "admin/formUpdateConsole.html";
	}
	
	
	@GetMapping(value="/admin/addGame/{id}")
	public String addGame(@PathVariable("id") Long id, Model model) {
		model.addAttribute("games", gameService.findAllGames());
		model.addAttribute("console", consoleService.getConsoleById(id));
		return "admin/gameToAdd.html";
	}

	@PostMapping(value="/console")
	public String newConsole(@Valid @ModelAttribute("console") Console console, 
			BindingResult bindingResult, Model model) {
		
		this.consoleValidator.validate(console, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.consoleService.createNewConsole(console);
			model.addAttribute("console", console);
			return "console.html";
		} else {
			return "admin/formNewConsole.html";
		}
	}
	
	@GetMapping("/admin/updateGames/{id}")
	public String updateGames(@PathVariable("id") Long id, Model model) {

		List<Games> gamesToAdd = this.gamesToAdd(id); //recupera la lista di giochi da aggiungere
		model.addAttribute("gamesToAdd", gamesToAdd);
		model.addAttribute("console", this.consoleService.getConsoleById(id));
		return "admin/gamesToAdd.html";
	}
	
	private List<Games> gamesToAdd(Long consoleId) {
		List<Games> gamesToAdd = new ArrayList<>();

		for (Games a : gameRepository.findGamesNotInConsole(consoleId)) {
			gamesToAdd.add(a);
		}
		return gamesToAdd;
	}
	
	@PostMapping(value="/admin/updateConsole/{consoleId}")
	public String updateConsole(@ModelAttribute("console") Console newConsole, @PathVariable("consoleId")Long consoleId, Model model) {
		Console oldConsole = this.consoleService.getConsoleById(consoleId);
		Console console = this.consoleService.updateConsole(oldConsole, newConsole);
		this.consoleService.saveConsole(console);
		model.addAttribute("console", console);
		return "admin/formUpdateConsole.html";
	}
	
	@GetMapping(value="/admin/addGameToConsole/{gameId}/{consoleId}")
	public String addGameToConsole(@PathVariable("gameId") Long gameId, @PathVariable("consoleId") Long consoleId, Model model) {
		Console console = this.consoleService.addGameToConsole(consoleId, gameId);
		List<Games> gamesToAdd = this.consoleService.findGamesNotInConsole(consoleId);

		model.addAttribute("console", console);
		model.addAttribute("gamesToAdd", gamesToAdd);

		return "admin/gamesToAdd.html";
	}
	
	@GetMapping(value="/admin/removeGameFromConsole/{gameId}/{consoleId}")
	public String removeActorFromConsole(@PathVariable("gameId") Long gameId, @PathVariable("consoleId") Long consoleId, Model model) {
		Console console = this.consoleService.removeGameFromConsole(consoleId, gameId);
		List<Games> gamesToAdd = this.consoleService.findGamesNotInConsole(consoleId);
		
		model.addAttribute("console", console);
		model.addAttribute("gamesToAdd", gamesToAdd);

		return "admin/gamesToAdd.html";
	}
	
	@GetMapping(value = "/admin/reviewsInConsole/{consoleId}")
	public String addRecensioneToConsole(@PathVariable("consoleId") Long consoleId, Model model) {
		Console console = this.consoleService.getConsoleById(consoleId);
		model.addAttribute("console", console);
		model.addAttribute("review", new Review());
		return "admin/newReview.html";
	}
	
	@GetMapping(value = "/admin/deleteConsole/{consoleId}")
	public String deleteConsole(@PathVariable("consoleId") Long consoleId, Model model) {
		this.consoleService.deleteConsole(consoleId);
		model.addAttribute("consoles", this.consoleService.getConsoles());
		return "admin/manageConsoles.html";
	}
	
	@GetMapping(value="/admin/deleteReview/{reviewId}/{consoleId}")
	public String deleteReview(@PathVariable("reviewId") Long reviewId, @PathVariable("consoleId")Long consoleId, Model model) {
		this.reviewService.deleteReview(reviewId); //cancella il review
		Console console = this.consoleService.getConsoleById(consoleId); //prende la console
		model.addAttribute("console", console); //aggiungi la console al modello
		model.addAttribute("reviews", console.getReviews()); //aggiungi la lista delle recensioni della console al modello
		return "admin/formUpdateConsole.html";
	}

	@GetMapping("/console/{id}")
	public String getConsole(@PathVariable("id") Long id, Model model) {
		Console console = this.consoleService.getConsoleById(id);
		model.addAttribute("console", console);
		return "console.html";
	}

	@GetMapping("/console")
	public String getConsoles(Model model) {
		
//    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		model.addAttribute("consoles", this.consoleService.getAllConsoleByAsc());
//		model.addAttribute("user", credentials.getUser());
		return "consoles.html";
	}
	
	@GetMapping("/formSearchConsoles")
	public String formSearchConsoles() {
		return "formSearchConsoles.html";
	}
	
	@PostMapping("/searchConsoles")
	public String searchConsoles(Model model, @RequestParam(required = false) Integer anno, @RequestParam(required = false) String iniziale) {
		if(anno != null) {
			List<Console> consoleYear = this.consoleRepository.findByAnno(anno);
			model.addAttribute("consoles", consoleYear);
		}else {
			model.addAttribute("consoles", new ArrayList<>());
		}
		return "foundConsoles.html";
	}
	
	@GetMapping(value = "/user/consolesUser")
	public String getUserConsoles(Model model) {
				//SecurityContextHolder per ottenere l'oggetto che rappresenta l'utente attualmente loggato
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			    String email = null;

			    if (principal instanceof OidcUser) {
			        email = ((OidcUser) principal).getEmail(); // login con Google
			    } else if (principal instanceof UserDetails) {
			        email = ((UserDetails) principal).getUsername(); // login classico
			    } else {
			        throw new IllegalStateException("Tipo di autenticazione non riconosciuto: " + principal.getClass());
			    }
			    //va a cercare le credenziali in base all'email
			    Credentials credentials = credentialsService.getCredentials(email); //recupera le credenziali dal DB
			    if (credentials == null) {
			        return "redirect:/registerGoogle"; // reindirizza alla pagina di registrazione
			    }

			    model.addAttribute("user", credentials.getUser());
			    model.addAttribute("consoles", this.consoleRepository.findAll());
				return "user/consolesUser.html";		
	}
	
	@GetMapping("user/indexUser")
	public String indexUser(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String email = null;

	    if (principal instanceof OidcUser) {
	        email = ((OidcUser) principal).getEmail(); // login con Google
	    } else if (principal instanceof UserDetails) {
	        email = ((UserDetails) principal).getUsername(); // login classico
	    } else {
	        throw new IllegalStateException("Tipo di utente non supportato: " + principal.getClass());
	    }

	    Credentials credentials = credentialsService.getCredentials(email);
	    if (credentials == null) {
	        return "redirect:/registerGoogle"; // se utente Google non è ancora nel DB
	    }

	    model.addAttribute("user", credentials.getUser());
	    model.addAttribute("username", credentials.getUsername());
	    return "user/indexUser.html";
	}

	@GetMapping("/user/consoleUser/{id}")
	public String getUserConsole(@PathVariable("id") Long id, Model model) {
		Console console = this.consoleService.getConsoleById(id);
		model.addAttribute("console", console);
		return "user/consoleUser.html";
	}

}
