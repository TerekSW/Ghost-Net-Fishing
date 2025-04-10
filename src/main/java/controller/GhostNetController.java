package main.java.controller;

import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.*;
import main.java.methods.GhostNetMethods;
import main.java.model.*;

@Named // Controller über JSF zugreifbar
@ViewScoped // Bean vorhanden, solange User auf gleichen Seite
public class GhostNetController {

    @Inject // GhostNetMethods automatisch eingebunden
    private GhostNetMethods ghostNetMethods;

    private GhostNet newNet = new GhostNet(); // neues Netz

    private Person newPerson = new Person(); // neue Person (meldend/bergend)

    private GhostNet chosenNet; // ausgewaehltes Netz

    // Getter und Setter
    public GhostNet getNewNet() {
        return newNet;
    }

    public void setNewNet(GhostNet newNet) {
        this.newNet = newNet;
    }

    public Person getNewPerson() {
        return newPerson;
    }

    public void setNewPerson(Person newPerson) {
        this.newPerson = newPerson;
    }

    public GhostNet getChosenNet() {
        return chosenNet;
    }

    public void setChosenNet(GhostNet chosenNet) {
        this.chosenNet = chosenNet;
    }

    // Einbettung Methoden aus GhostNetMethods
    // Zeige alle Geisternetze an
    public List<GhostNet> getAllNets() {
        return ghostNetMethods.getAllNets();
    }

    // Zeige alle Geisternetze mit bestimmten Status an
    public List<GhostNet> getNetzeNachStatus(GhostNetStatus status) {
        return ghostNetMethods.getByStatus(status);
    }

    // Meldung eines neuen Netzes mit Kontaktdaten (optional)
    public void meldeNetz() {
        newNet.setMeldendPerson(newPerson); // Person dem Netz zuweisen
        ghostNetMethods.meldeNetz(newNet); // Aufruf der meldeNetz-Methode aus GhostNetMethods

        // Eingabefeld zurücksetzten
        newNet = new GhostNet();
        newPerson = new Person();
    }

    // Zur Bergung eines Netzes eintragen
    public void bergeNetz() {
        ghostNetMethods.bergeNetz(chosenNet, newPerson);
    }

    // Zeige alle noch zu bergenden Netze an (Status --> GEMELDET)
    public List<GhostNet> getZuBergendeNetze() {
        return ghostNetMethods.getZuBergendeNetze();
    }

    // Netz als geborgen markieren
    public void setGeborgen() {
        ghostNetMethods.setGeborgen(chosenNet);
    }

    // Netz als verschollen markieren
    public void setVerschollen() {
        ghostNetMethods.setVerschollen(chosenNet, newPerson);
    }
}
