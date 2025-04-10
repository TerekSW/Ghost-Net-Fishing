package main.java.methods;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import main.java.model.*;
import model.GhostNet;
import model.GhostNetStatus;
import model.Person;

@ApplicationScoped //Bean gültig, solange Applikation auf Application-Server läuft
public class GhostNetMethods {

    // EntityManager fuer Zugriff auf Datenbank
    @PersistenceContext
    private EntityManager entityManager;

    // Anzeigen aller Netze
    public List<GhostNet> getAllNets() {    
        return entityManager.createQuery("SELECT a FROM GhostNet a", GhostNet.class)
                .getResultList(); // Ergebnis als Liste
    }

    // Anzeigen aller Netze nach Status
    public List<GhostNet> getByStatus(GhostNetStatus status) {
        return entityManager.createQuery("SELECT a FROM GhostNet a WHERE a.status = :status", GhostNet.class)   //Query für Suche nach Status
                .setParameter("status", status) //Filter nach Status
                .getResultList(); // Ergebnis als Liste
    }

    // MUST 1 Netz erfassen (auch anonym möglich)
    @Transactional  //Führe Datenbank-Transaktion aus
    public void meldeNetz(GhostNet net) {

        //kein Netz eingetragen --> Fehlermeldung
        if (net == null) {
            throw new IllegalArgumentException("Bitte tragen Sie die Informationen des Netzes ein.");
        }

        net.setStatus(GhostNetStatus.GEMELDET); //eingetragenes Netz auf den Status GEMELDET setzen

        Person person = net.getMeldendPerson(); //meldende Person abrufen

        boolean anonym = false; 

        //prüfen, ob Person nicht vorhanden --> anonym
        if (person == null) { 
            anonym = true;
        //prüfen, ob Name/Telefonnummer leer --> anonym
        } else if ((person.getName() == null || person.getName().isBlank()) &&
                (person.getNumber() == null || person.getNumber().isBlank())) {
            anonym = true;  
        }

        //wenn anonym Name/Telefonnummer null
        if (anonym) {
            person.setName(null);
            person.setNumber(null);
        }

        net.setMeldendPerson(person);   //meldende Person Netz zuweisen
        entityManager.persist(net); //Netz speichern  
    }

    // MUST 2 Zur Bergung eintragen
    @Transactional //Führe Datenbank-Transaktion aus
    public void bergeNetz(GhostNet net, Person person) {
        net.setStatus(GhostNetStatus.BERGUNG_BEVORSTEHEND); // setze Status auf BERGUNG_BEVORSTEHEND
        net.setBergendePerson(person); // Person als bergende Person speichern
        entityManager.merge(net); // vorhandenes Netz aktualisieren
    }

    // MUST 3 alle noch zu bergenden Netze anzeigen
    @Transactional  //Führe Datenbank-Transaktion aus
    public List<GhostNet> getZuBergendeNetze() {
        return entityManager.createQuery("SELECT a FROM GhostNet a WHERE a.status = :status", GhostNet.class) //Query für Suche nach Status  
                .setParameter("status", GhostNetStatus.GEMELDET)    //Filter nach Status GEMELDET     
                .getResultList(); // Liste mit Netzen --> nur gemeldete Netze
    }

    // MUST 4 Netz als geborgen melden
    @Transactional  //Führe Datenbank-Transaktion aus
    public void setGeborgen(GhostNet net) {
        net.setStatus(GhostNetStatus.GEBORGEN); // Status ändern
        entityManager.merge(net); // vorhandenes Netz aktualisieren
    }

    // COULD 5 Netz als verschollen melden
    @Transactional  //Führe Datenbank-Transaktion aus
    public void setVerschollen(GhostNet net, Person person) {
        net.setStatus(GhostNetStatus.VERSCHOLLEN); // Status ändern
        entityManager.merge(net); // Netz aktualisieren
    }
}
