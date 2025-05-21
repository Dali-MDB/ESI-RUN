package transport.core;

public enum ModePaiement {
    //enum for the payment modes (3 types)
    ESPECES("Espèces"),
    DAHABIA("Dahabia"),
    BARIDIMOB("BaridiMob");

    private final String libelle;

    //constructor for the enum
    ModePaiement(String libelle) {
        this.libelle = libelle;
    }

    //getter for the enum
    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}