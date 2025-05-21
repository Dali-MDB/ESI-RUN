package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Employe extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    private String matricule;
    private TypeFonction fonction;
    private boolean mobiliteReduite;

    public Employe(String nom, String prenom, String adresse, LocalDate dateNaissance,
                   boolean mobiliteReduite, String matricule, TypeFonction fonction) {
        super(nom, prenom, adresse, dateNaissance);
        this.mobiliteReduite = mobiliteReduite;
        this.matricule = matricule;
        this.fonction = fonction;
    }

    // Getters and setters
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public TypeFonction getFonction() {
        return fonction;
    }

    public void setFonction(TypeFonction fonction) {
        this.fonction = fonction;
    }

    public boolean aMobiliteReduite() {
        return mobiliteReduite;
    }

    public void setMobiliteReduite(boolean mobiliteReduite) {
        this.mobiliteReduite = mobiliteReduite;
    }

    // Détermine si l'employé a droit à une réduction et retourne le type de carte approprié
    public TypeCarte determinerTypeCarteOptimal() {
        if (mobiliteReduite) {
            return TypeCarte.SOLIDARITE; // Priorité à la carte solidarité (50%)
        } else {
            return TypeCarte.PARTENAIRE; // Carte partenaire pour les employés (40%)
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (" + fonction + " à " + (mobiliteReduite ? ", mobilité réduite" : "") + ")";
    }
}