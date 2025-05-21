package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Usager extends Personne implements Serializable {  //must be serializable to be saved in a file
    private static final long serialVersionUID = 1L;

    private boolean mobiliteReduite;
    private TypeUsager typeUsager;

    public Usager(String nom, String prenom, String adresse, LocalDate dateNaissance, boolean mobiliteReduite) {
        super(nom, prenom, adresse, dateNaissance);
        this.mobiliteReduite = mobiliteReduite;
        determinerTypeUsager();
    }

    // Method to determine the type of user
    private void determinerTypeUsager() {
        int age = getAge();
        if (this.mobiliteReduite){
            this.typeUsager = TypeUsager.SOLIDARITE;
        }
        else if (age < 25) { //if the user is younger than 25, he is a junior
            this.typeUsager = TypeUsager.JUNIOR;
        } else if (age >= 65) { //if the user is older than 65, he is a senior
            this.typeUsager = TypeUsager.SENIOR;
        } else { //if the user is not a junior or a senior, he is a standard
            this.typeUsager = TypeUsager.STANDARD;
        }
    }

    // Getter
    public TypeUsager getTypeUsager() {
        return typeUsager;
    }

    // Setter
    public void setTypeUsager(TypeUsager typeUsager) {
        this.typeUsager = typeUsager;
    }

    public boolean aMobiliteReduite() {
        return mobiliteReduite;
    }

    public void setMobiliteReduite(boolean mobiliteReduite) {
        this.mobiliteReduite = mobiliteReduite;
    }

    // Method to determine the optimal card type
    public TypeCarte determinerTypeCarteOptimal() throws ReductionImpossibleException {
        // If the user has a reduced mobility, priority to the Solidarity card
        if (mobiliteReduite) {
            return TypeCarte.SOLIDARITE; // 50% reduction
        }

        // If the user has a specific type that gives the right to a card, use it
        if (typeUsager != null && typeUsager.getTypeCarteCorrespondant() != null) {
            return typeUsager.getTypeCarteCorrespondant();
        }

        // If the user is of type STANDARD and does not have reduced mobility, no reduction
        throw new ReductionImpossibleException("You are not entitled to any reduction.");
    }

    @Override
    public String toString() {
        return super.toString() + " (" + typeUsager + (mobiliteReduite ? ", reduced mobility" : "") + ")";
    }
}