package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Games {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String name;
//	private String surname;
	
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private LocalDate dateOfBirth;
	private String urlOfPicture;
	
	@ManyToMany(mappedBy="games")
	private Set<Console> starredConsoles;
	
	@OneToMany(mappedBy="giocoEsclusivo")
	private List<Console> directedConsoles;
	
	public Games(){
		this.starredConsoles= new HashSet<>();
		this.directedConsoles = new LinkedList<>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
//	public String getSurname() {
//		return surname;
//	}
//	
//	public void setSurname(String surname) {
//		this.surname = surname;
//	}
	
//	public LocalDate getDateOfBirth() {
//		return dateOfBirth;
//	}
//	
//	public void setDateOfBirth(LocalDate dateOfBirth) {
//		this.dateOfBirth = dateOfBirth;
//	}
	
	public String getUrlOfPicture() {
		return urlOfPicture;
	}
	
	public void setUrlOfPicture(String urlOfPicture) {
		this.urlOfPicture = urlOfPicture;
	}
	
	public Set<Console> getGameOf() {
		return starredConsoles;
	}

	public void setGameOf(Set<Console> starredConsoles) {
		this.starredConsoles = starredConsoles;
	}

	public List<Console> getDirectorOf() {
		return directedConsoles;
	}

	public void setDirectorOf(List<Console> directedConsoles) {
		this.directedConsoles = directedConsoles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public Set<Console> getStarredConsoles() {
		return starredConsoles;
	}

	public void setStarredGames(Set<Console> starredConsoles) {
		this.starredConsoles = starredConsoles;
	}

	public List<Console> getDirectedConsoles() {
		return directedConsoles;
	}

	public void setDirectedConsoles(List<Console> directedConsoles) {
		this.directedConsoles = directedConsoles;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Games other = (Games) obj;
		return Objects.equals(name, other.name);
	}

}
