package transport.core;

public enum TypeCarte {
    JUNIOR("Junior", 30),
    SENIOR("Senior", 25),
    SOLIDARITE("Solidarité", 50),
    PARTENAIRE("Partenaire", 40);

    private final String libelle;
    private final int tauxReduction; // in percentage

    TypeCarte(String libelle, int tauxReduction) {
        this.libelle = libelle;
        this.tauxReduction = tauxReduction;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getTauxReduction() {
        return tauxReduction;
    }

    @Override
    public String toString() {
        return libelle;
    }
}