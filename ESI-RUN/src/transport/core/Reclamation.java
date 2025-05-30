package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Reclamation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private TypeReclamation type;
    private String description;
    private LocalDate dateReclamation;
    private Personne declarant;
    private String stationConcernee; // Renommé de entiteConcernee à stationConcernee

    public Reclamation(TypeReclamation type, String description, Personne declarant, String stationConcernee) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.description = description;
        this.dateReclamation = LocalDate.now();
        this.declarant = declarant;
        this.stationConcernee = stationConcernee;
    }

    public String getId() {
        return id;
    }

    public TypeReclamation getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateReclamation() {
        return dateReclamation;
    }

    public Personne getDeclarant() {
        return declarant;
    }

    public String getStationConcernee() {  // Méthode renommée
        return stationConcernee;
    }

    @Override
    public String toString() {
        return type + " - " + stationConcernee + " (" + dateReclamation + ")";
    }
}