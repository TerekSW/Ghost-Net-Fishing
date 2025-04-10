package main.java.model;

import jakarta.persistence.*;

//Entität für Geisternetz in Datenbank
@Entity
public class GhostNet {

    @Id // Primärschlüssel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // automatische Generierung der ID
    private long id;

    private String coordinates; // Koordinaten des Netzes
    private String size; // Größe des Netzes

    @Enumerated(EnumType.STRING) // speichert Enum als String
    private GhostNetStatus status; // aktueller Status des Netzes

    @ManyToOne // Viele Netze können von einer Person gemeldet werden
    private Person meldendePerson;

    @ManyToOne // Viele Netze können von einer Person geborgen werden
    private Person bergendePerson;

    // Getter und Setter
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public GhostNetStatus getStatus() {
        return status;
    }

    public void setStatus(GhostNetStatus status) {
        this.status = status;
    }

    public Person getMeldendPerson() {
        return meldendePerson;
    }

    public void setMeldendPerson(Person meldendePerson) {
        this.meldendePerson = meldendePerson;
    }

    public Person getBergendePerson() {
        return bergendePerson;
    }

    public void setBergendePerson(Person bergendePerson) {
        this.bergendePerson = bergendePerson;
    }
}
