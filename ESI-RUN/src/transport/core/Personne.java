package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String nom;
    protected String prenom;
    protected String adresse;
    protected LocalDate dateNaissance;

    public Personne(String nom, String prenom, String adresse, LocalDate dateNaissance) {
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}