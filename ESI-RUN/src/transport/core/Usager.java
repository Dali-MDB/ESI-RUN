package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Usager extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean mobiliteReduite;
    private TypeUsager typeUsager;

    public Usager(String nom, String prenom, String adresse, LocalDate dateNaissance, boolean mobiliteReduite) {
        super(nom, prenom, adresse, dateNaissance);
        this.mobiliteReduite = mobiliteReduite;
        determinerTypeUsager();
    }

    private void determinerTypeUsager() {
        int age = getAge();
        if (this.mobiliteReduite){
            this.typeUsager = TypeUsager.SOLIDARITE;
        }
        else if (age < 25) {
            this.typeUsager = TypeUsager.JUNIOR;
        } else if (age >= 65) {
            this.typeUsager = TypeUsager.SENIOR;
        } else {
            this.typeUsager = TypeUsager.STANDARD;
        }
    }

    public TypeUsager getTypeUsager() {
        return typeUsager;
    }

    public void setTypeUsager(TypeUsager typeUsager) {
        this.typeUsager = typeUsager;
    }

    public boolean aMobiliteReduite() {
        return mobiliteReduite;
    }

    public void setMobiliteReduite(boolean mobiliteReduite) {
        this.mobiliteReduite = mobiliteReduite;
    }

    // Méthode corrigée pour déterminer le type de carte optimal
    public TypeCarte determinerTypeCarteOptimal() throws ReductionImpossibleException {
        // Si l'usager a une mobilité réduite, priorité à la carte Solidarité
        if (mobiliteReduite) {
            return TypeCarte.SOLIDARITE; // 50% de réduction
        }

        // Si l'usager a un type spécifique qui donne droit à une carte, l'utiliser
        if (typeUsager != null && typeUsager.getTypeCarteCorrespondant() != null) {
            return typeUsager.getTypeCarteCorrespondant();
        }

        // Si l'usager est de type STANDARD et n'a pas de mobilité réduite, pas de réduction
        throw new ReductionImpossibleException(". Vous n'avez droit à aucune réduction.");
    }

    @Override
    public String toString() {
        return super.toString() + " (" + typeUsager + (mobiliteReduite ? ", mobilité réduite" : "") + ")";
    }
}