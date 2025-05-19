package transport.core;

public enum TypeUsager {
    JUNIOR("Junior", TypeCarte.JUNIOR),
    SENIOR("Senior", TypeCarte.SENIOR),
    SOLIDARITE("Solidarité", TypeCarte.SOLIDARITE),
    PARTENAIRE("Partenaire", TypeCarte.PARTENAIRE),
    STANDARD("Standard", null);

    private final String libelle;
    private final TypeCarte typeCarteCorrespondant;

    TypeUsager(String libelle, TypeCarte typeCarteCorrespondant) {
        this.libelle = libelle;
        this.typeCarteCorrespondant = typeCarteCorrespondant;
    }

    public String getLibelle() {
        return libelle;
    }

    public TypeCarte getTypeCarteCorrespondant() {
        return typeCarteCorrespondant;
    }

    @Override
    public String toString() {
        return libelle;
    }
}