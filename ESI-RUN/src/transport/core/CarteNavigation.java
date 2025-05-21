package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class CarteNavigation extends TitreTransport implements Serializable {

    private static final double PRIX_BASE = 5000;
    private LocalDate dateExpiration;
    private TypeCarte typeCarte;

    public CarteNavigation(Usager usager, ModePaiement modePaiement) throws ReductionImpossibleException {
        super();
        setUsager(usager);
        setModePaiement(modePaiement);

        // Determine the type of card based on the user
        if (usager != null) {
            this.typeCarte = usager.determinerTypeCarteOptimal();
            // The card is valid for 1 year from the date of purchase
            this.dateExpiration = dateAchat.plusYears(1);
            this.prix = calculerPrix();
        } else {
            throw new IllegalArgumentException("The user cannot be null for a navigation card");
        }
    }

    // Constructor for tests
    public CarteNavigation(Personne proprietaire) throws ReductionImpossibleException {
        super();
        if (proprietaire instanceof Usager) {
            Usager usager = (Usager) proprietaire;
            setUsager(usager);
            this.typeCarte = usager.determinerTypeCarteOptimal();
        } else {
            throw new IllegalArgumentException("The owner must be a user");
        }

        // The card is valid for 1 year from the date of purchase
        this.dateExpiration = dateAchat.plusYears(1);
        this.prix = calculerPrix();
    }

    @Override
    protected double calculerPrix() {
        // Check that the type of card is not null before accessing its methods
        if (typeCarte == null) {
            return PRIX_BASE; // No reduction if the type is not defined
        }

        // Apply the reduction according to the type of card
        double reduction = typeCarte.getTauxReduction() / 100.0;
        return PRIX_BASE * (1 - reduction);
    }

    @Override
    public boolean estValide(LocalDate dateUtilisation) throws TitreNonValideException {
        // Check that the expiration date is defined
        if (dateExpiration == null) {
            throw new TitreNonValideException("Expiration date not defined");
        }

        // The card is valid until its expiration date
        if (!dateUtilisation.isAfter(dateExpiration)) {
            return true;
        } else {
            throw new TitreNonValideException("Navigation card number " + id + " invalid - expired since " + dateExpiration);
        }
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public TypeCarte getType() {
        return typeCarte;
    }

    // Alias for getType() for clarity in the code
    public TypeCarte getTypeCarte() {
        return typeCarte;
    }

    @Override
    public String toString() {
        String idDisplay = id.length() >= 8 ? id.substring(0, 8) : id;
        String userInfo = (usager != null) ? usager.getNom() + " " + usager.getPrenom() : "Usager non défini";
        String typeInfo = (typeCarte != null) ? " [" + typeCarte + "]" : "";

        return "Carte #" + idDisplay + " (" + getDateAchat() +
                (dateExpiration != null ? " à " + getDateExpiration() : "") +
                ") - " + userInfo + typeInfo;
    }
}