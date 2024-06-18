package Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")

public class Player {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplayer", nullable = false)
	
    private Long id;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "age")
    private int age;
	
 // Getter za id
    public Long getId() {
        return id;
    }

    // Setter za id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter za username
    public String getUsername() {
        return username;
    }

    // Setter za username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter za age
    public int getAge() {
        return age;
    }

    // Setter za age
    public void setAge(int age) {
        this.age = age;
    }
    

}
