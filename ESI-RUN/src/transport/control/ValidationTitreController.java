package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import transport.core.CarteNavigation;
import transport.core.TitreNonValideException;
import transport.core.TitreTransport;
import transport.ui.MainApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidationTitreController {

    @FXML
    private TextField idTitreField;

    @FXML
    private VBox resultatValidationBox;

    @FXML
    private Label titreInfoLabel;

    @FXML
    private Label validationResultatLabel;

    @FXML
    private void validerTitre(ActionEvent event) {
        String idTitre = idTitreField.getText().trim();

        if (idTitre.isEmpty()) {
            afficherErreur("Champ vide", "Veuillez saisir l'identifiant du titre à valider.");
            return;
        }

        // Rechercher le titre dans le gestionnaire
        TitreTransport titre = MainApp.getGestionnaireTransport().rechercherTitreParId(idTitre);

        if (titre == null) {
            afficherErreur("Titre introuvable", "Aucun titre ne correspond à cet identifiant.");
            return;
        }

        // Préparer les informations sur le titre
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append(titre.getClass().getSimpleName()).append(" #");

        String idDisplay = titre.getId().length() >= 8 ? titre.getId().substring(0, 8) : titre.getId();
        infoBuilder.append(idDisplay);

        infoBuilder.append(" - ");
        if (titre.getUsager() != null) {
            infoBuilder.append(titre.getUsager().getNom()).append(" ").append(titre.getUsager().getPrenom());
        } else {
            infoBuilder.append("Usager non défini");
        }

        if (titre instanceof CarteNavigation) {
            CarteNavigation carte = (CarteNavigation) titre;
            infoBuilder.append(" - Type: ").append(carte.getType());
            infoBuilder.append(" - Expiration: ").append(carte.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        titreInfoLabel.setText(infoBuilder.toString());

        // Vérifier la validité du titre et tenter de le valider
        try {
            if (titre.aEteValide()) {
                validationResultatLabel.setText("Titre déjà validé");
                validationResultatLabel.setTextFill(Color.BLUE);
            } else {
                // Tenter de valider le titre
                titre.valider();
                validationResultatLabel.setText("Titre validé avec succès");
                validationResultatLabel.setTextFill(Color.GREEN);
            }
        } catch (TitreNonValideException e) {
            validationResultatLabel.setText("Validation impossible: " + e.getMessage());
            validationResultatLabel.setTextFill(Color.RED);
        }

        // Afficher la zone de résultat
        resultatValidationBox.setVisible(true);
    }

    @FXML
    private void annuler(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) idTitreField.getScene().getWindow();
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