package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transport.core.GestionnaireTransport;
import transport.ui.MainApp;

import java.io.IOException;

public class MainController {

    @FXML
    private Label statusLabel;

    private GestionnaireTransport gestionnaire;

    @FXML
    private void initialize() {
        gestionnaire = MainApp.getGestionnaireTransport();
        statusLabel.setText("Application prête.");
    }

    @FXML
    private void sauvegarderDonnees(ActionEvent event) {
        try {
            gestionnaire.sauvegarderDonnees();
            statusLabel.setText("Données sauvegardées avec succès.");
        } catch (IOException e) {
            afficherErreur("Erreur de sauvegarde", "Impossible de sauvegarder les données: " + e.getMessage());
        }
    }
    @FXML
    private void afficherListeEmployes(ActionEvent event) {
        ouvrirFenetre("/views/ListeEmployesView.fxml", "Liste des employés");
    }
    @FXML
    private void restaurerDonnees(ActionEvent event) {
        try {
            GestionnaireTransport nouveauGestionnaire = GestionnaireTransport.chargerDonnees();
            // Mettre à jour la référence globale
            MainApp.setGestionnaireTransport(nouveauGestionnaire);
            this.gestionnaire = nouveauGestionnaire;
            statusLabel.setText("Données restaurées avec succès.");
        } catch (Exception e) {
            afficherErreur("Erreur de restauration", "Impossible de restaurer les données: " + e.getMessage());
        }
    }

    @FXML
    private void quitterApplication(ActionEvent event) {
        // Demander confirmation avant de quitter
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quitter l'application");
        alert.setContentText("Voulez-vous sauvegarder les données avant de quitter?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    gestionnaire.sauvegarderDonnees();
                } catch (IOException e) {
                    afficherErreur("Erreur de sauvegarde", "Impossible de sauvegarder les données: " + e.getMessage());
                }
            }
            // Fermer l'application
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.close();
        });
    }

    // Méthodes pour afficher les différentes vues
    @FXML
    private void afficherAjoutUsager(ActionEvent event) {
        ouvrirFenetre("/views/AjoutUsagerView.fxml", "Ajouter un usager");
    }

    @FXML
    private void afficherAjoutEmploye(ActionEvent event) {
        ouvrirFenetre("/views/AjoutEmployeView.fxml", "Ajouter un employé");
    }

    @FXML
    private void afficherVenteTitre(ActionEvent event) {
        ouvrirFenetre("/views/VenteTitreView.fxml", "Vendre un titre de transport");
    }

    @FXML
    private void afficherValidationTitre(ActionEvent event) {
        ouvrirFenetre("/views/ValidationTitreView.fxml", "Valider un titre de transport");
    }

    @FXML
    private void afficherListeUsagers(ActionEvent event) {
        ouvrirFenetre("/views/ListeUsagersView.fxml", "Liste des usagers");
    }

    @FXML
    private void afficherListeTitres(ActionEvent event) {
        ouvrirFenetre("/views/ListeTitresView.fxml", "Liste des titres");
    }

    @FXML
    private void afficherAjoutReclamation(ActionEvent event) {
        ouvrirFenetre("/views/AjoutReclamationView.fxml", "Ajouter une réclamation");
    }

    @FXML
    private void afficherListeReclamations(ActionEvent event) {
        ouvrirFenetre("/views/ListeReclamationsView.fxml", "Liste des réclamations");
    }

    @FXML
    private void afficherAPropos(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("ESI-RUN - Système de gestion de transport");
        alert.setContentText("Version 1.0\nDéveloppé dans le cadre du TP POO 2024/2025\nEcole Supérieure d'Informatique (ESI-Alger)");
        alert.showAndWait();
    }

    // Méthode utilitaire pour ouvrir une nouvelle fenêtre
    private void ouvrirFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titre);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            afficherErreur("Erreur d'interface", "Impossible d'ouvrir la fenêtre: " + e.getMessage());
        }
    }

    // Méthode utilitaire pour afficher une erreur
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}