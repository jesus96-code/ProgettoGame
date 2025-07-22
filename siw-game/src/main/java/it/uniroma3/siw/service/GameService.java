package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Games;
import it.uniroma3.siw.repository.GameRepository;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Transactional
	public Iterable<Games> findAllGames() {
		return gameRepository.findAll();
	}
	
	@Transactional
	public List<Games> findGamesNotInConsole(Long consoleId){
		List<Games> gamesToAdd = new ArrayList<>();
		
		for(Games a : gameRepository.findGamesNotInConsole(consoleId)) {
			gamesToAdd.add(a);
		}
		return gamesToAdd;
	}
	
	@Transactional
    public Games getGameById(Long id) {
        return gameRepository.findById(id).get();
    }

}
