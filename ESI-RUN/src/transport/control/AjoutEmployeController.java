package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.Employe;
import transport.core.TypeFonction;
import transport.ui.MainApp;

import java.time.LocalDate;

public class AjoutEmployeController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField adresseField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private TextField matriculeField;

    @FXML
    private ComboBox<TypeFonction> fonctionCombo;

    @FXML
    private CheckBox mobiliteReduiteCheck;

    @FXML
    private void initialize() {
        // Initialiser la combobox avec les types de fonction
        fonctionCombo.setItems(FXCollections.observableArrayList(TypeFonction.values()));
        fonctionCombo.getSelectionModel().selectFirst();

        // Définir une valeur par défaut pour la date
        dateNaissancePicker.setValue(LocalDate.now().minusYears(25));
    }

    @FXML
    private void ajouterEmploye(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                adresseField.getText().isEmpty() || dateNaissancePicker.getValue() == null ||
                matriculeField.getText().isEmpty() || fonctionCombo.getValue() == null) {
            afficherErreur("Champs incomplets", "Veuillez remplir tous les champs.");
            return;
        }

        // Créer un nouvel employé
        try {
            Employe nouvelEmploye = new Employe(
                    nomField.getText(),
                    prenomField.getText(),
                    adresseField.getText(),
                    dateNaissancePicker.getValue(),
                    mobiliteReduiteCheck.isSelected(),
                    matriculeField.getText(),
                    fonctionCombo.getValue()
            );

            // Ajouter l'employé au gestionnaire
            MainApp.getGestionnaireTransport().ajouterPersonne(nouvelEmploye);

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("L'employé a été ajouté avec succès.");
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