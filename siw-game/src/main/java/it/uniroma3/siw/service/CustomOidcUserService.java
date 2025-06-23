package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;
    
    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    	OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail(); // oppure: (String) oidcUser.getAttributes().get("email")
        
        Credentials existingCredentials = credentialsRepository.findByUsername(email);

        if (existingCredentials == null) {
            // Primo login: crea utente e credenziali
            User user = new User();
            user.setName(oidcUser.getGivenName());
            user.setSurname(oidcUser.getFamilyName());

            Credentials newCredentials = new Credentials();
            newCredentials.setUsername(email);
            newCredentials.setRole(Credentials.DEFAULT_ROLE);
            newCredentials.setUser(user);

            credentialsRepository.save(newCredentials);
        }

        return oidcUser;
    }
}
