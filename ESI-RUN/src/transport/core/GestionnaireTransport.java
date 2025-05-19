package transport.core;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestionnaireTransport implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Personne> personnes;
    private List<TitreTransport> titres;
    private List<Reclamation> reclamations;
    private Map<String, Boolean> stationsSuspendues; // Map pour suivre l'état des stations
    private static final String FICHIER_SAUVEGARDE = "esi_run_data.ser";

    public GestionnaireTransport() {
        this.personnes = new ArrayList<>();
        this.titres = new ArrayList<>();
        this.reclamations = new ArrayList<>();
        this.stationsSuspendues = new HashMap<>();
    }

    // Méthodes de gestion des personnes
    public void ajouterPersonne(Personne personne) {
        personnes.add(personne);
    }

    public List<Personne> getPersonnes() {
        return new ArrayList<>(personnes);
    }

    public List<Usager> getUsagers() {
        return personnes.stream()
                .filter(p -> p instanceof Usager)
                .map(p -> (Usager) p)
                .collect(Collectors.toList());
    }

    public List<Employe> getEmployes() {
        return personnes.stream()
                .filter(p -> p instanceof Employe)
                .map(p -> (Employe) p)
                .collect(Collectors.toList());
    }

    // Méthodes de gestion des titres de transport
    public void ajouterTitre(TitreTransport titre) {
        titres.add(titre);
    }

    public List<TitreTransport> getTitres() {
        return titres.stream()
                .sorted(Comparator.comparing(TitreTransport::getDateAchat).reversed())
                .collect(Collectors.toList());
    }

    public TitreTransport rechercherTitreParId(String id) {
        return titres.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Méthodes de gestion des réclamations
    public void ajouterReclamation(Reclamation reclamation) {
        reclamations.add(reclamation);

        // Vérifier si la station doit être suspendue
        verifierSuspensionStation(reclamation.getStationConcernee());
    }

    private void verifierSuspensionStation(String nomStation) {
        // Compter le nombre de réclamations pour cette station
        long nbReclamations = reclamations.stream()
                .filter(r -> r.getStationConcernee().equals(nomStation))
                .count();

        // Si 3 réclamations ou plus, marquer la station comme suspendue
        if (nbReclamations >= 3) {
            stationsSuspendues.put(nomStation, true);
        }
    }

    public List<Reclamation> getReclamations() {
        return new ArrayList<>(reclamations);
    }

    public boolean isStationSuspendue(String nomStation) {
        return stationsSuspendues.getOrDefault(nomStation, false);
    }

    public Map<String, Long> getStationsAvecNombreReclamations() {
        return reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getStationConcernee, Collectors.counting()));
    }

    public List<String> getStationsSuspendues() {
        // Retourne la liste des noms de stations suspendues
        return stationsSuspendues.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Méthode pour lever la suspension d'une station
    public void leverSuspensionStation(String nomStation) {
        stationsSuspendues.put(nomStation, false);
    }

    // Méthodes de persistance des données
    public void sauvegarderDonnees() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHIER_SAUVEGARDE))) {
            oos.writeObject(this);
        }
    }

    public static GestionnaireTransport chargerDonnees() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHIER_SAUVEGARDE))) {
            return (GestionnaireTransport) ois.readObject();
        } catch (FileNotFoundException e) {
            // Si le fichier n'existe pas, on renvoie une nouvelle instance
            return new GestionnaireTransport();
        }
    }
}