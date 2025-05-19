package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id; // ID en tant que String
    protected LocalDate dateAchat;
    protected double prix;
    protected Usager usager;
    protected ModePaiement modePaiement;
    protected boolean valide = false;
    private static int compteur = 0;

    public TitreTransport() {
        this.id = String.valueOf(++compteur); // Conversion en String
        this.dateAchat = LocalDate.now();
        this.prix = calculerPrix();
    }

    // Méthode pour initialiser l'usager et le mode de paiement
    public void initialiser(Usager usager, ModePaiement modePaiement) {
        this.usager = usager;
        this.modePaiement = modePaiement;
        this.prix = calculerPrix(); // Recalcul du prix après l'initialisation
    }

    // Méthode abstraite pour calculer le prix du titre
    protected abstract double calculerPrix();

    // Méthode pour vérifier si le titre n'est pas expiré (à la date actuelle)
    public boolean estValide() {
        try {
            return estValide(LocalDate.now());
        } catch (TitreNonValideException e) {
            return false;
        }
    }

    // Méthode abstraite pour vérifier si le titre n'est pas expiré à une date
    // donnée
    public abstract boolean estValide(LocalDate dateUtilisation) throws TitreNonValideException;

    // Méthode pour valider un titre (simulation de passage au lecteur magnétique)
    public void valider() throws TitreNonValideException {
        if (!estValide()) {
            throw new TitreNonValideException("Titre expiré, validation impossible.");
        }
        this.valide = true;
    }

    // Indique si le titre a été validé par un lecteur
    public boolean aEteValide() {
        return valide;
    }

    // Getters
    public String getId() {
        return id;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public double getPrix() {
        return prix;
    }

    public Usager getUsager() {
        return usager;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    // Setters
    public void setUsager(Usager usager) {
        this.usager = usager;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    @Override
    public String toString() {
        String idStr = String.valueOf(id);
        String idDisplay = idStr.length() >= 8 ? idStr.substring(0, 8) : idStr;
        return "Titre #" + idDisplay + " (" + dateAchat + ")";
    }
}