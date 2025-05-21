package transport.core;

public enum TypeFonction {
    AGENT("Agent de station"),
    CONDUCTEUR("Conducteur");

    private final String libelle;

    TypeFonction(String libelle) {
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