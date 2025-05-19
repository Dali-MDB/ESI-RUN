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

        // Déterminer le type de carte en fonction de l'usager
        if (usager != null) {
            this.typeCarte = usager.determinerTypeCarteOptimal();
            // La carte est valide pour 1 an à partir de la date d'achat
            this.dateExpiration = dateAchat.plusYears(1);
            this.prix = calculerPrix();
        } else {
            throw new IllegalArgumentException("L'usager ne peut pas être null pour une carte de navigation");
        }
    }

    // Constructeur pour les tests
    public CarteNavigation(Personne proprietaire) throws ReductionImpossibleException {
        super();
        if (proprietaire instanceof Usager) {
            Usager usager = (Usager) proprietaire;
            setUsager(usager);
            this.typeCarte = usager.determinerTypeCarteOptimal();
        } else {
            throw new IllegalArgumentException("Le propriétaire doit être un usager");
        }

        // La carte est valide pour 1 an à partir de la date d'achat
        this.dateExpiration = dateAchat.plusYears(1);
        this.prix = calculerPrix();
    }

    @Override
    protected double calculerPrix() {
        // Vérifier que le type de carte n'est pas null avant d'accéder à ses méthodes
        if (typeCarte == null) {
            return PRIX_BASE; // Pas de réduction si le type n'est pas défini
        }

        // Appliquer la réduction selon le type de carte
        double reduction = typeCarte.getTauxReduction() / 100.0;
        return PRIX_BASE * (1 - reduction);
    }

    @Override
    public boolean estValide(LocalDate dateUtilisation) throws TitreNonValideException {
        // Vérifier que la date d'expiration est définie
        if (dateExpiration == null) {
            throw new TitreNonValideException("Date d'expiration non définie");
        }

        // La carte est valide jusqu'à sa date d'expiration
        if (!dateUtilisation.isAfter(dateExpiration)) {
            return true;
        } else {
            throw new TitreNonValideException("Carte de navigation numéro " + id + " invalide - expirée depuis le " + dateExpiration);
        }
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public TypeCarte getType() {
        return typeCarte;
    }

    // Alias pour getType() pour plus de clarté dans le code
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