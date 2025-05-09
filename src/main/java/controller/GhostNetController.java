package controller;

import java.io.Serializable;
import java.util.List;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.*;
import methods.*;
import model.*;

@Named // Controller über JSF zugreifbar
@ViewScoped // Bean vorhanden, solange User auf gleichen Seite
public class GhostNetController implements Serializable {

    @Inject // GhostNetMethods automatisch eingebunden
    private GhostNetMethods ghostNetMethods;

    private GhostNet newNet = new GhostNet(); // neues Netz

    private Person newPerson = new Person(); // neue Person (meldend/bergend)

    private long chosenNetId;

    private GhostNet chosenNet; // ausgewaehltes Netz

    private boolean zeigeZuBergendeNetze = false;   //Toggle für Ein-Ausklappen der Liste

    private boolean zeigeAlleNetze = false;     //Toggle für Ein-Ausklappen der Liste 

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

    public long getChosenNetId() {
        return chosenNetId;
    }

    public void setChosenNetId(long id) {
        this.chosenNetId = id;
    }

    public boolean isZeigeZuBergendeNetze() {
        return zeigeZuBergendeNetze;
    }

    public void setZeigeZuBergendeNetze(boolean zeigeZuBergendeNetze) {
        this.zeigeZuBergendeNetze = zeigeZuBergendeNetze;
    }

    public boolean isZeigeAlleNetze() {
        return zeigeAlleNetze;
    }

    public void setZeigeAlleNetze(boolean zeigeAlleNetze) {
        this.zeigeAlleNetze = zeigeAlleNetze;
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

    // Meldung eines neuen Netzes mit Kontaktdaten
    public String meldeNetz() {
        newNet.setMeldendePerson(newPerson); // Person dem Netz zuweisen
        ghostNetMethods.meldeNetz(newNet); // Aufruf der meldeNetz-Methode aus GhostNetMethods

        // Eingabefeld zurücksetzten
        newNet = new GhostNet();
        newPerson = new Person();

        return "index.xhtml?faces-redirect=true"; // Redirect zur Startseite
    }

    // Zur Bergung eines Netzes eintragen
    public String bergeNetz() {
        GhostNet selected = ghostNetMethods.getGhostNetById(chosenNetId); // Netz anhand ID abrufen
        ghostNetMethods.bergeNetz(selected, newPerson); // Netz mit Person verknüpfen

        return "index.xhtml?faces-redirect=true"; // Redirect zur Startseite
    }

    // Zeige alle noch zu bergenden Netze an (Status --> GEMELDET)
    public List<GhostNet> getZuBergendeNetze() {
        return ghostNetMethods.getZuBergendeNetze();
    }

    public void zuBergendeNetze() {
        this.zeigeZuBergendeNetze = !this.zeigeZuBergendeNetze; // Toogle Liste
    }

    public void zeigeAlleNetze() {
        this.zeigeAlleNetze = !this.zeigeAlleNetze; // Toogle Liste
    }

    // Netz als geborgen markieren
    public String setGeborgen() {

        GhostNet net = ghostNetMethods.getGhostNetById(chosenNetId); // Netz anhand ID abgerufen

        if (net == null) {
            return "index.xhtml?faces-redirect=true"; // Redirect zur Startseite
        }
        ghostNetMethods.setGeborgen(chosenNetId, newPerson);
        return "index.xhtml?faces-redirect=true"; // Redirect zur Startseite
    }

    // Netz als verschollen markieren
    public String setVerschollen() {

        GhostNet net = ghostNetMethods.getGhostNetById(chosenNetId); // Netz anhand ID abgerufen

        // Überprüfung ob Netz ausgewählt
        if (net == null) {
            return "index.xhtml?faces-redirect=true"; // Redirect zur Startseite
        }

        ghostNetMethods.setVerschollen(chosenNetId, newPerson); // Status auf VERSCHOLLEN setzen
        return "index.xhtml?faces-redirect=true"; // Redirect zur Startseite
    }
}
