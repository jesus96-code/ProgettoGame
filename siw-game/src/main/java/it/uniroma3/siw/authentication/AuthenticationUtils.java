package it.uniroma3.siw.authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.service.CredentialsService;

@Component
public class AuthenticationUtils {
	
	@Autowired
    private CredentialsService credentialsService;
	
	@Autowired
	private CredentialsRepository credentialsRepository;

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Credentials credentials;

        if (principal instanceof OidcUser) {
        	OidcUser oidcUser = (OidcUser) principal;
            String email = oidcUser.getEmail();
         // Prova a trovare le credenziali
            Optional<Credentials> optionalCred = credentialsRepository.findByUsername(email);

            if (optionalCred.isEmpty()) {
            	User user = new User();
                user.setName(oidcUser.getGivenName());
                user.setSurname(oidcUser.getFamilyName());
                user.setEmail(email);

                credentials = new Credentials();
//                credentials.setEmail(email);
                credentials.setRole(Credentials.DEFAULT_ROLE); // ad esempio "USER"
                credentials.setUser(user);

                // NON impostare password per utenti OAuth2
                credentialsService.saveOAuthCredentials(credentials); // usa un metodo apposito
            } else {
                // Crea utente e credenziali per OAuth2
                User user = new User();
                user.setName(oidcUser.getGivenName());
                user.setSurname(oidcUser.getFamilyName());
                user.setEmail(email);

                credentials = new Credentials();
//                credentials.setEmail(email);
                credentials.setRole(Credentials.DEFAULT_ROLE); // ad esempio "USER"
                credentials.setUser(user);

                credentialsService.saveCredentials(credentials);
            }
        } else if (principal instanceof UserDetails) {
        	UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            credentials = credentialsService.getCredentialsByUsername(username);
        } else {
            throw new RuntimeException("Tipo di autenticazione non supportato.");
        }

        return credentials.getUser();
    }
}
