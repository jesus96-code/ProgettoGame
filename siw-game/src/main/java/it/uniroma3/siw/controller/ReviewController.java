package it.uniroma3.siw.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Console;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ConsoleService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ConsoleService consoleService;
	
	@Autowired
	private ReviewValidator reviewValidator;
	
	@Autowired
	private CredentialsService credentialsService;
	
	 @GetMapping("/user/formNewReview/{id}")
	    public String formNewReview(@PathVariable("id") Long id, Model model) {
	        model.addAttribute("console", this.consoleService.getConsoleById(id));
	        model.addAttribute("review", new Review());
	        return "user/formNewReview.html";
	 }
	 
	 @PostMapping("/user/review/{consoleId}")
	    public String newReview(@Valid @ModelAttribute Review review, BindingResult bindingResult,
	                                   @PathVariable("consoleId") Long consoleId, Model model, Authentication authentication) {
	        if (!bindingResult.hasErrors()) {
	        	String email = null;

	            // Ottieni il principal in modo sicuro
	            Object principal = authentication.getPrincipal();

	            if (principal instanceof OidcUser ) {
	            	OidcUser oidcUser = (OidcUser) principal;
	                email = oidcUser.getEmail(); // Google OAuth2
	            } else if (principal instanceof UserDetails) {
	            	 UserDetails userDetails = (UserDetails) principal;
	                email = userDetails.getUsername(); // Login locale
	            }

	            if (email == null) {
	                throw new RuntimeException("Impossibile determinare l’email dell’utente loggato");
	            }
	            
	            // Recupera le credenziali e l’utente associato
	            Credentials credentials = credentialsService.getCredentialsByUsername(email);
	            User user = credentials.getUser();

	            // Associa la review all’utente
	            review.setUser(user);
	            
	            Review newReview = this.reviewService.newReview(review, consoleId);
	            Console console = this.consoleService.addReviewToConsole(consoleId, newReview.getId());
	            model.addAttribute("console", console);
	            model.addAttribute("review", newReview);
	            return "user/reviewSuccessful.html";
	        } else {
	            model.addAttribute("console", this.consoleService.getConsoleById(consoleId));
	            return "user/formNewReview.html";
	        }
	 }
	
//	@GetMapping("/admin/reviewsInMovie/{id}")
//	public String reviewsInMovie(@PathVariable("id")Long id, Model model) {
//		Movie movie = movieService.getMovieById(id);
//		List<Review> reviews = this.reviewService.findAllByMovieId(movie.getId());
//		if(!reviews.isEmpty()) {
//			model.addAttribute("review", reviews);
//			model.addAttribute("movie", movie);
//			return "admin/manageReviews.html";
//		} else {
////			Movie movie = this.movieRepository.findById(movieId).get();
//			model.addAttribute("movie", movie);
//			model.addAttribute("review", new Review());
//			return "user/formNewReview.html";
//		}
//	}
	
	@GetMapping("/reviews/reviewsOrdered/{id}")
    public String orderReviews(@PathVariable("id") Long id, Model model) {
        List<Review> reviews = this.reviewService.getReviewByRateAsc();
        Console console = this.consoleService.getConsoleById(id);
        model.addAttribute("console", console);
        model.addAttribute("reviews", reviews);
        return "reviews.html";
    }

}
