package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.GameRepository;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private GameRepository gameRepository;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}
	
	@GetMapping(value = "/chiSiamo")
	public String chiSiamo(Model model) {
		return "chiSiamo";
	}
	
	@GetMapping(value = "/contattaci")
	public String contattaci() {
		return "contattaci";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}
		Object principal = authentication.getPrincipal();
	    String username;

	    if (principal instanceof UserDetails) {
	        username = ((UserDetails) principal).getUsername();
	    } else if (principal instanceof OidcUser) {
	        username = ((OidcUser) principal).getEmail(); // oppure getPreferredUsername()
	    } else {
	        return "index.html"; // fallback
	    }

	    Credentials credentials = credentialsService.getCredentialsByUsername(username);

	    if (credentials != null && Credentials.ADMIN_ROLE.equals(credentials.getRole())) {
	        return "admin/indexAdmin.html";
	    }

	    return "user/indexUser.html";
	}
		
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        
        if (principal instanceof UserDetails) {
	        username = ((UserDetails) principal).getUsername();
	    } else if (principal instanceof OidcUser) {
	        username = ((OidcUser) principal).getEmail(); // oppure getPreferredUsername()
	    } else {
	        return "index.html"; // fallback
	    }

	    Credentials credentials = credentialsService.getCredentials(username);

        if (credentials != null && Credentials.ADMIN_ROLE.equals(credentials.getRole())) {
            return "admin/indexAdmin.html";
        }

        return "user/indexUser.html";
    }

	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model) {

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "registerUser";
    }
	
	
}