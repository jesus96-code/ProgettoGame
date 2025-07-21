package it.uniroma3.siw.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
//		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String usernameOrEmail;

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		    usernameOrEmail = ((UserDetails) principal).getUsername();
		} else if (principal instanceof OidcUser) {
		    usernameOrEmail = ((OidcUser) principal).getEmail(); // oppure getAttribute("email")
		} else {
		    throw new IllegalStateException("Tipo utente non supportato");
		}
		Credentials credentials = credentialsService.getCredentials(usernameOrEmail);
		review.setConsole(console);
		review.setReviewer(credentials.getUser());
		this.reviewRepository.save(review);
		return review;
    }
	
	 @Transactional
	    public List<Review> findAllByConsoleId(Long consoleId) {
	    	return this.reviewRepository.findAllByConsoleId(this.consoleRepository.findById(consoleId).get().getId());
	    }

}
