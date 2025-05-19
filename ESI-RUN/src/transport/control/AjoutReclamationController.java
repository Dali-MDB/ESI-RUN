package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.*;
import transport.ui.MainApp;

import java.util.Map;

public class AjoutReclamationController {

    @FXML
    private ComboBox<TypeReclamation> typeReclamationCombo;

    @FXML
    private ComboBox<Personne> declarantCombo;

    @FXML
    private TextField stationConcerneeField;  // Renommé

    @FXML
    private TextArea descriptionArea;

    private GestionnaireTransport gestionnaire;

    @FXML
    private void initialize() {
        gestionnaire = MainApp.getGestionnaireTransport();

        // Initialiser la combobox avec les types de réclamation
        typeReclamationCombo.setItems(FXCollections.observableArrayList(TypeReclamation.values()));
        typeReclamationCombo.getSelectionModel().selectFirst();

        // Initialiser la combobox avec les personnes
        declarantCombo.setItems(FXCollections.observableArrayList(gestionnaire.getPersonnes()));
    }

    @FXML
    private void ajouterReclamation(ActionEvent event) {
        // Vérifier que tous les champs nécessaires sont remplis
        if (typeReclamationCombo.getValue() == null || declarantCombo.getValue() == null ||
                stationConcerneeField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
            afficherErreur("Champs incomplets", "Veuillez remplir tous les champs.");
            return;
        }

        String stationConcernee = stationConcerneeField.getText();

        // Vérifier si la station est déjà suspendue
        if (gestionnaire.isStationSuspendue(stationConcernee)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Station suspendue");
            alert.setHeaderText("La station est actuellement suspendue");
            alert.setContentText("La station '" + stationConcernee + "' est déjà suspendue suite à de nombreuses réclamations. La réclamation sera tout de même enregistrée.");
            alert.showAndWait();
        }

        // Créer la réclamation
        try {
            Reclamation nouvelleReclamation = new Reclamation(
                    typeReclamationCombo.getValue(),
                    descriptionArea.getText(),
                    declarantCombo.getValue(),
                    stationConcernee
            );

            // Ajouter la réclamation au gestionnaire
            gestionnaire.ajouterReclamation(nouvelleReclamation);

            // Vérifier si la station doit être suspendue suite à cette nouvelle réclamation
            boolean nouvellementSuspendue = gestionnaire.isStationSuspendue(stationConcernee);

            // Afficher un message de confirmation
            if (nouvellementSuspendue) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alerte suspension");
                alert.setHeaderText("Station suspendue");
                alert.setContentText("La réclamation a été enregistrée avec succès.\n" +
                        "ATTENTION: La station '" + stationConcernee + "' a reçu 3 réclamations ou plus et est maintenant suspendue.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("La réclamation a été enregistrée avec succès.");
                alert.showAndWait();
            }

            // Fermer la fenêtre
            fermerFenetre();
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'ajout", "Une erreur est survenue: " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) typeReclamationCombo.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}