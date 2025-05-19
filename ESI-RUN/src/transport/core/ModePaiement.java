package transport.core;

public enum ModePaiement {
    ESPECES("Espèces"),
    DAHABIA("Dahabia"),
    BARIDIMOB("BaridiMob");

    private final String libelle;

    ModePaiement(String libelle) {
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