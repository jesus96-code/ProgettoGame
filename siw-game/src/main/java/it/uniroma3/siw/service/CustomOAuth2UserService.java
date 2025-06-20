package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	    OAuth2User oauth2User = super.loadUser(userRequest);

	    String email = (String) oauth2User.getAttributes().get("email");
	    String name = (String) oauth2User.getAttributes().get("given_name");
	    String surname = (String) oauth2User.getAttributes().get("family_name");

	    System.out.println("OAuth2 login: email=" + email + ", name=" + name + ", surname=" + surname);

	    if (email == null) {
	    	throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token"), "Email not found from OAuth2 provider");
	    }

	    Credentials credentials = credentialsService.getCredentials(email);
	    if (credentials == null) {
	        System.out.println("Utente non trovato nel DB, lo creo...");
	        User newUser = new User();
	        newUser.setEmail(email);
	        newUser.setName(name != null ? name : "NoName");
	        newUser.setSurname(surname != null ? surname : "NoSurname");

	        credentials = new Credentials();
	        credentials.setUsername(email);
	        credentials.setPassword("");
	        credentials.setRole(Credentials.DEFAULT_ROLE);
	        credentials.setUser(newUser);

	        Credentials saved = credentialsService.saveCredentials(credentials);
	        System.out.println("Utente salvato con id: " + saved.getId());
	    } else {
	        System.out.println("Utente già presente: " + credentials.getUsername());
	    }

	    return oauth2User;
	}


}
