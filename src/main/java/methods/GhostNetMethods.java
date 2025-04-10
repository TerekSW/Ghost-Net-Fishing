package main.java.methods;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import main.java.model.*;

@ApplicationScoped
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
        return entityManager.createQuery("SELECT a FROM GhostNet a WHERE a.status = :status", GhostNet.class)
                .setParameter("status", status)
                .getResultList(); // Ergebnis als Liste
    }

    // MUST 1 Netz erfassen (auch anonym möglich)
    @Transactional
    public void meldeNetz(GhostNet net) {

        if(net == null) {
            throw new IllegalArgumentException("Bitte tragen Sie die Informationen des Netzes ein.");
        }

        net.setStatus(GhostNetStatus.GEMELDET); 

        Person person = net.getMeldendPerson();

        boolean anonym = false;

        if (person == null) { // Wenn keine Person angegeben ist, dann anonym
            anonym = true;
        } else if ((person.getName() == null || person.getName().isBlank()) &&
                (person.getNumber() == null || person.getNumber().isBlank())) {
            anonym = true;
        }

        if(anonym) {
            person.setName(null);
            person.setNumber(null);
        }

        net.setMeldendPerson(person);
        entityManager.persist(net);
    }

    
}
