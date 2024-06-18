package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "problem")

public class Problem {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 	@Column(name = "idproblem", nullable = false)
	    private Long id;
	    
	    @Column(name = "description", columnDefinition = "TEXT")
	    private String description;
	    
	    @ManyToOne
	    @JoinColumn(name = "idplayer", nullable = false)
	    private Player player;
	    
	    //Getter za id
	    public Long getId() {
	        return id;
	    }

	    // Setter za id
	    public void setId(Long id) {
	        this.id = id;
	    }

	    // Getter za description
	    public String getDescription() {
	        return description;
	    }

	    // Setter za description
	    public void setDescription(String description) {
	        this.description = description;
	    }

	    // Getter za player
	    public Player getPlayer() {
	        return player;
	    }

	    // Setter za player
	    public void setPlayer(Player player) {
	        this.player = player;
	    }

}
