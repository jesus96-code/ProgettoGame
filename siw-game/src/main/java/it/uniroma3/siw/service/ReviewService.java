package it.uniroma3.siw.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Console;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ConsoleRepository;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ConsoleRepository consoleRepository;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired 
	private UserService userService;
	
	@Transactional
	public void addReview(Review review) {
		this.reviewRepository.save(review);
	}
	
	@Transactional
	public Review getReviewById(Long id) {
		return this.reviewRepository.findById(id).get();
	}
	
	@Transactional 
	public List<Review> getReviewByRateAsc(){
		return this.reviewRepository.findByOrderByRatingAsc();
	}
	
	/*Le transazioni sono utilizzate per garantire la coerenza dei dati nel 
	 * database, in modo che tutte le operazioni all'interno di un metodo 
	 * vengano eseguite o annullate insieme*/
	@Transactional
    public void deleteReview(Long reviewId) {
            Review review = this.getReviewById(reviewId);
            Console console = review.getConsole(); // ottiene l'oggetto Movie associato alla recensione
            review.getReviewer().getReviews().remove(review); //rimuove la recensione della lista delle recensioni dell'autore della recensione
            review.getConsole().getReviews().remove(review);//rimuove la recensione della lista delle recensioni associate al film
            this.consoleRepository.save(console);
            this.reviewRepository.delete(review);
    }
	
	public Review saveReviewToUser(Long userId, Long reviewId) {
        Review review = this.getReviewById(reviewId);
        User user = this.userService.getUser(userId);
        List<Review> reviews = user.getReviews();
        reviews.add(review);
        user.setReviews(reviews);
        review.setReviewer(user);
        return this.reviewRepository.save(review);
    }
	
	@Transactional
    public Review newReview(@Valid Review review, Long consoleId) {
        Console console = this.consoleRepository.findById(consoleId).get();
        //recupera l'utente attualmente autenticato dal contesto di sicurezza
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //ottengo le credenziali dell'utente autenticato, utilizzando il nome utente
        Credentials credentials = credentialsService.getCredentials(user.getUsername());
        review.setConsole(console); //imposta il film assocciato alla recensione
        review.setReviewer(credentials.getUser()); // imposta l'utente che ha scritto la recensione
        this.reviewRepository.save(review); // salva la recensione nel repository
//        List<Review> reviews = movie.getReviews();
//        movie.getReviews().add(review);
//        this.movieRepository.save(movie);
        return review;
    }
	
	 @Transactional
	    public List<Review> findAllByConsoleId(Long consoleId) {
	    	return this.reviewRepository.findAllByConsoleId(this.consoleRepository.findById(consoleId).get().getId());
	    }

}
