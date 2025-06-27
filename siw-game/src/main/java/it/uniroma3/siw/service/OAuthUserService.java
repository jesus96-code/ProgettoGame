package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class OAuthUserService {
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Transactional
    public void createUserIfNotExists(OidcUser oidcUser) {
        String email = oidcUser.getEmail();

        Optional<Credentials> existingCredentials = credentialsRepository.findByUsername(email);
        if (existingCredentials.isPresent()) return;

        User user = new User();
        user.setName(oidcUser.getGivenName());
        user.setSurname(oidcUser.getFamilyName());
        user.setEmail(email);

        user = userRepository.save(user);

        Credentials credentials = new Credentials();
        credentials.setUsername(email);
//        credentials.setEmail(email);
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setUser(user);

        credentialsRepository.save(credentials);
    }
}
