package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

// E' un servizio che gestisce l'accesso ai dati relativi alle credenziali dell'utente
@Service
public class CredentialsService {
	
    @Autowired
    protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredentialsRepository credentialsRepository;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public void saveOAuthCredentials(Credentials credentials) {
	    // Nessuna password, nessuna codifica
	    credentialsRepository.save(credentials);
	}

//	@Transactional
//	public Credentials getCredentials(String username) {
//		Credentials result = this.credentialsRepository.findByUsername(username);
//		return result;
//	}
	
	public Credentials getCredentialsByUsername(String username) {
	    return credentialsRepository.findByUsername(username).orElse(null);
	}
	
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
    	if (credentials.getRole() == null) {
            credentials.setRole(Credentials.DEFAULT_ROLE); // assegna DEFAULT se non specificato
        }

        if (credentials.getPassword() != null) {
            credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        }
        return this.credentialsRepository.save(credentials);
    }
}
