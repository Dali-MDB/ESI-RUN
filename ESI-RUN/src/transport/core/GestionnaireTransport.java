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
    private Map<String, Boolean> stationsSuspendues; // Map to follow the state of the stations
    private static final String FICHIER_SAUVEGARDE = "esi_run_data.ser";



    public GestionnaireTransport() {
        this.personnes = new ArrayList<>();
        this.titres = new ArrayList<>();
        this.reclamations = new ArrayList<>();
        this.stationsSuspendues = new HashMap<>();
    }

    // Methods to manage the persons
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

    // Methods to manage the transport titles
    public void ajouterTitre(TitreTransport titre) {
        titres.add(titre);
    }

    // method to get the titles
    public List<TitreTransport> getTitres() {
        return titres.stream()
                .sorted(Comparator.comparing(TitreTransport::getDateAchat).reversed())
                .collect(Collectors.toList());
    }

    // method to get the title by id
    public TitreTransport rechercherTitreParId(String id) {
        return titres.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Methods to manage the reclamations
    public void ajouterReclamation(Reclamation reclamation) {
        reclamations.add(reclamation);

        // Check if the station must be suspended
        verifierSuspensionStation(reclamation.getStationConcernee()); //check if the station must be suspended
    }

    private void verifierSuspensionStation(String nomStation) {
        // Count the number of reclamations for this station
        long nbReclamations = reclamations.stream()
                .filter(r -> r.getStationConcernee().equals(nomStation))
                .count(); //count the number of reclamations for this station

        // If 3 reclamations or more, mark the station as suspended
        if (nbReclamations >= 3) {
            stationsSuspendues.put(nomStation, true);
        }
    }

    public List<Reclamation> getReclamations() {
        return new ArrayList<>(reclamations); //return the list of reclamations
    }

    public boolean isStationSuspendue(String nomStation) {
        return stationsSuspendues.getOrDefault(nomStation, false);
    }

    // method to get the stations with the number of reclamations
    public Map<String, Long> getStationsAvecNombreReclamations() {
        return reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getStationConcernee, Collectors.counting()));
    }

    // method to get the suspended stations
    public List<String> getStationsSuspendues() {
        // Return the list of the names of the suspended stations
        return stationsSuspendues.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Method to lift the suspension of a station
    public void leverSuspensionStation(String nomStation) {
        stationsSuspendues.put(nomStation, false);
    }

    // Methods to save the data
    public void sauvegarderDonnees() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHIER_SAUVEGARDE))) { //save the data in the file
            oos.writeObject(this);
        }
    }

    // method to load the data
    public static GestionnaireTransport chargerDonnees() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHIER_SAUVEGARDE))) {
            return (GestionnaireTransport) ois.readObject();
        } catch (FileNotFoundException e) {
            // If the file does not exist, return a new instance
            return new GestionnaireTransport();
        }
    }



}