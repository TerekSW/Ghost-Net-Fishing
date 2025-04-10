package main.java.model;

import java.io.Serializable;
import jakarta.persistence.*;

//Entität für Person in Datenbank
@Entity
public class Person implements Serializable {

    @Id // Primärschlüssel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // automatische Generierung der ID
    private long id;

    private String name; // Name der Person
    private String number; // Telefonnummer
    private boolean anonym; // Anonymität der Person

    // Getter und Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isAnonym() {
        return anonym;
    }

    public void setAnonym(boolean anonym) {
        this.anonym = anonym;
    }
}
