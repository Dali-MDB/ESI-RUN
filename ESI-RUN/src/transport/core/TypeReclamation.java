package transport.core;

public enum TypeReclamation {
    TECHNIQUE("Technique"),
    PAIEMENT("Paiement"),
    SERVICE("Service");

    private final String libelle;

    TypeReclamation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}