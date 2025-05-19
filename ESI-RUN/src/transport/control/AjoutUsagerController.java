package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.TypeCarte;
import transport.core.Usager;
import transport.core.ReductionImpossibleException;
import transport.ui.MainApp;

import java.time.LocalDate;

public class AjoutUsagerController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField adresseField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private CheckBox mobiliteReduiteCheck;

    @FXML
    private Label typeCarteLabel;

    @FXML
    private void initialize() {
        // Définir une valeur par défaut pour la date
        dateNaissancePicker.setValue(LocalDate.now().minusYears(30));

        // Ajouter des listeners pour mettre à jour le type de carte affiché
        dateNaissancePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateTypeCarteLabel();
        });

        mobiliteReduiteCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateTypeCarteLabel();
        });

        // Initialiser l'affichage du type de carte
        updateTypeCarteLabel();
    }

    private void updateTypeCarteLabel() {
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        boolean mobiliteReduite = mobiliteReduiteCheck.isSelected();

        if (dateNaissance != null) {
            // Calculer l'âge
            int age = LocalDate.now().getYear() - dateNaissance.getYear();

            // Déterminer le type de carte
            String typeCarte;
            if (mobiliteReduite) {
                typeCarte = "Solidarité (-50%)";
            } else if (age < 25) {
                typeCarte = "Junior (-30%)";
            } else if (age >= 65) {
                typeCarte = "Senior (-25%)";
            } else {
                typeCarte = "Pas de réduction disponible";
            }

            typeCarteLabel.setText(typeCarte);
        }
    }

    @FXML
    private void ajouterUsager(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                adresseField.getText().isEmpty() || dateNaissancePicker.getValue() == null) {
            afficherErreur("Champs incomplets", "Veuillez remplir tous les champs.");
            return;
        }

        // Créer un nouvel usager
        try {
            Usager nouvelUsager = new Usager(
                    nomField.getText(),
                    prenomField.getText(),
                    adresseField.getText(),
                    dateNaissancePicker.getValue(),
                    mobiliteReduiteCheck.isSelected()
            );

            // Ajouter l'usager au gestionnaire
            MainApp.getGestionnaireTransport().ajouterPersonne(nouvelUsager);

            // Vérifier le type de carte que l'usager peut obtenir (pour l'information)
            String messageInfo;
            try {
                TypeCarte typeCarte = nouvelUsager.determinerTypeCarteOptimal();
                messageInfo = "L'usager a été ajouté avec succès.\nType de carte disponible: " + typeCarte +
                        " (-" + typeCarte.getTauxReduction() + "%)";
            } catch (ReductionImpossibleException e) {
                messageInfo = "L'usager a été ajouté avec succès.\nAucune réduction de carte disponible.";
            }

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText(messageInfo);
            alert.showAndWait();

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
        Stage stage = (Stage) nomField.getScene().getWindow();
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