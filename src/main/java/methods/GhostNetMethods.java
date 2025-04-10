package main.java.methods;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
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

    
}
