package methods;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import model.*;

@ApplicationScoped // Bean gültig, solange Applikation auf Application-Server läuft
public class GhostNetMethods {

    // EntityManager fuer Zugriff auf Datenbank
    @PersistenceContext(unitName = "GhostNetPersistence")
    private EntityManager entityManager;

    // Anzeigen aller Netze
    public List<GhostNet> getAllNets() {
        return entityManager.createQuery("SELECT a FROM GhostNet a", GhostNet.class)
                .getResultList(); // Ergebnis als Liste
    }

    // Anzeigen aller Netze nach Status
    public List<GhostNet> getByStatus(GhostNetStatus status) {
        // Query für Suche nach Status
        return entityManager.createQuery("SELECT a FROM GhostNet a WHERE a.status = :status", GhostNet.class)
                .setParameter("status", status) // Filter nach Status
                .getResultList(); // Ergebnis als Liste
    }

    // Einzelnes Netz anhand ID abrufen
    public GhostNet getGhostNetById(long id) {
        return entityManager.find(GhostNet.class, id);
    }

    // MUST 1 Netz erfassen (auch anonym möglich)
    @Transactional // Führe Datenbank-Transaktion aus
    public void meldeNetz(GhostNet net) {

        // kein Netz eingetragen --> Fehlermeldung
        if (net == null) {
            throw new IllegalArgumentException("Bitte tragen Sie die Informationen des Netzes ein.");
        }

        net.setStatus(GhostNetStatus.GEMELDET); // eingetragenes Netz auf den Status GEMELDET setzen

        Person person = net.getMeldendPerson(); // meldende Person abrufen

        boolean anonym = false;

        // prüfen, ob Person nicht vorhanden --> anonym
        if (person == null) {
            anonym = true;
            // prüfen, ob Name/Telefonnummer leer --> anonym
        } else if ((person.getName() == null || person.getName().isBlank()) &&
                (person.getNumber() == null || person.getNumber().isBlank())) {
            anonym = true;
        }

        // wenn anonym Name/Telefonnummer null
        if (anonym) {
            person.setName(null);
            person.setNumber(null);
            person.setAnonym(true);
        } else {
            person.setAnonym(false);
        }

        entityManager.persist(person); // Person speichern
        net.setMeldendPerson(person); // meldende Person Netz zuweisen
        entityManager.persist(net); // Netz speichern

    }

    // MUST 2 Zur Bergung eintragen
    @Transactional // Führe Datenbank-Transaktion aus
    public void bergeNetz(GhostNet net, Person person) {
        // prüfe ob anonym
        if ((person.getName() == null || person.getName().isBlank()) &&
                (person.getNumber() == null || person.getNumber().isBlank())) {
            person.setAnonym(true);
        } else {
            person.setAnonym(false);
        }

        entityManager.persist(person); // bergende Person speichern
        net.setStatus(GhostNetStatus.BERGUNG_BEVORSTEHEND); // setze Status auf BERGUNG_BEVORSTEHEND
        net.setBergendePerson(person); // Person als bergende Person speichern
        entityManager.merge(net); // vorhandenes Netz aktualisieren
    }

    // MUST 3 alle noch zu bergenden Netze anzeigen
    @Transactional // Führe Datenbank-Transaktion aus
    public List<GhostNet> getZuBergendeNetze() {
        // Query für Suche nach Status
        return entityManager.createQuery("SELECT a FROM GhostNet a WHERE a.status = :status", GhostNet.class)
                .setParameter("status", GhostNetStatus.GEMELDET) // Filter nach Status GEMELDET
                .getResultList(); // Liste mit Netzen --> nur gemeldete Netze
    }

    // MUST 4 Netz als geborgen melden
    @Transactional // Führe Datenbank-Transaktion aus
    public void setGeborgen(long id) {
        GhostNet net = getGhostNetById(id); // Netz anhand ID laden
        net.setStatus(GhostNetStatus.GEBORGEN); // Staus setzen
        entityManager.merge(net);   // Netz speichern
    }

    // COULD 5 Netz als verschollen melden
    @Transactional // Führe Datenbank-Transaktion aus
    public void setVerschollen(long id, Person person) {
        GhostNet net = getGhostNetById(id); // Netz anhand ID laden
        net.setStatus(GhostNetStatus.VERSCHOLLEN);  // Staus setzen
        entityManager.merge(net);   // Netz speichern
    }
}
