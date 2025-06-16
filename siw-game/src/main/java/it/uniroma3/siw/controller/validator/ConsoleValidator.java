package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Console;
import it.uniroma3.siw.repository.ConsoleRepository;

@Component
public class ConsoleValidator implements Validator {
	
	@Autowired
	private ConsoleRepository consoleRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Console console = (Console)o;
		if (console.getName()!=null && console.getAnno()!=null 
				&& consoleRepository.existsByNameAndAnno(console.getName(), console.getAnno())) {
			errors.reject("movie.duplicate"); //specifica che c'è stato un errore nella validazione e registra un codice di errore
		}
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Console.class.equals(aClass);
	}
}