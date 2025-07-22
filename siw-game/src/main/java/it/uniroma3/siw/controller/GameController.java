package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Games;
import it.uniroma3.siw.repository.GameRepository;
import it.uniroma3.siw.service.GameService;

@Controller
public class GameController {
	
	@Autowired 
	private GameRepository gameRepository;
	
	@Autowired 
	private GameService gameService;

	@GetMapping(value="/admin/formNewGame")
	public String formNewGame(Model model) {
		model.addAttribute("game", new Games());
		return "admin/formNewGame.html";
	}
	
	@GetMapping(value="/admin/indexGame")
	public String indexGame() {
		return "admin/indexGame.html";
	}
	
	@PostMapping("/admin/game")
	public String newGame(@ModelAttribute("game") Games game, Model model) {
		if (!gameRepository.existsByName(game.getName())) {
			this.gameRepository.save(game); 
			model.addAttribute("game", game);
			return "game.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo game esiste già");
			return "admin/formNewGame.html";
		}
	}

	@GetMapping("/game/{id}")
	public String getGame(@PathVariable("id") Long id, Model model) {
		model.addAttribute("game", this.gameService.getGameById(id));
		return "game.html";
	}

	@GetMapping("/game")
	public String getGames(Model model) {
		model.addAttribute("games", this.gameService.findAllGames());
		return "games.html";
	}

}
