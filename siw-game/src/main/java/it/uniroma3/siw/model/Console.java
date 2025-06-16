package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Console {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
	private String name;
    
    @NotNull
    @Min(1900)
    @Max(2023)
	private Integer anno;
    
    private String urlImage;
////	private byte[] image;
	
	@ManyToOne
	private Games giocoEsclusivo;
	
	//con EAGER vengono caricati subito anche gli oggetti collegati
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Games> games;
	
	//mappedBy per esprimere la reciprocità dei riferimenti di una associazione bidirezionale
	@OneToMany(mappedBy = "console")
	private List<Review> reviews;
	
	public Console() {
		this.games = new LinkedList<>();
		this.reviews = new ArrayList<>();
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

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	
//	@Nullable
//	public byte[] getImage() {
//		return image;
//	}
//
//	public void setImage(@Nullable byte[] image) {
//		this.image = image;
//	}

	public Games getGiocoEsclusivo() {
		return giocoEsclusivo;
	}

	public void setGiocoEsclusivo(Games giocoEsclusivo) {
		this.giocoEsclusivo = giocoEsclusivo;
	}

	public List<Games> getGames() {
		return games;
	}

	public void setGames(List<Games> games) {
		this.games = games;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, anno);
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Console other = (Console) obj;
		return Objects.equals(name, other.name) && anno.equals(other.anno);
	}

}
