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
            afficherErreur("empty field", "please enter the title id to validate.");
            return;
        }

        // Search for the title in the manager
        TitreTransport titre = MainApp.getGestionnaireTransport().rechercherTitreParId(idTitre);

        if (titre == null) {
            afficherErreur("title not found", "no title corresponds to this id.");
            return;
        }

        // Prepare the information on the title
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append(titre.getClass().getSimpleName()).append(" #");

        String idDisplay = titre.getId().length() >= 8 ? titre.getId().substring(0, 8) : titre.getId();
        infoBuilder.append(idDisplay);  //append the id of the title

        infoBuilder.append(" - ");
        if (titre.getUsager() != null) {
            infoBuilder.append(titre.getUsager().getNom()).append(" ").append(titre.getUsager().getPrenom());
        } else {
            infoBuilder.append("Usager non défini");
        }

        if (titre instanceof CarteNavigation) {  //if the title is a navigation card
            CarteNavigation carte = (CarteNavigation) titre;
            infoBuilder.append(" - Type: ").append(carte.getType());
            infoBuilder.append(" - Expiration: ").append(carte.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        titreInfoLabel.setText(infoBuilder.toString());  //set the information on the title

        // Check the validity of the title and try to validate it
        try {
            if (titre.aEteValide()) {
                validationResultatLabel.setText("title already validated");
                validationResultatLabel.setTextFill(Color.BLUE);
            } else {
                // Try to validate the title
                titre.valider();
                validationResultatLabel.setText("title validated successfully");
                validationResultatLabel.setTextFill(Color.GREEN);
            }
        } catch (TitreNonValideException e) {
            validationResultatLabel.setText("impossible to validate: " + e.getMessage());
            validationResultatLabel.setTextFill(Color.RED);
        }

        // Display the result zone
        resultatValidationBox.setVisible(true);
    }

    @FXML
    private void annuler(ActionEvent event) {  //close the window
        fermerFenetre();
    }

    private void fermerFenetre() {  //close the window
        Stage stage = (Stage) idTitreField.getScene().getWindow();
        stage.close();
    }

    private void afficherErreur(String titre, String message) {  //show the error message
        Alert alert = new Alert(Alert.AlertType.ERROR);  //create an error alert
        alert.setTitle(titre);  //set the title of the alert
        alert.setHeaderText(null);  //set the header text of the alert
        alert.setContentText(message);  //set the content text of the alert
        alert.showAndWait();  //show the alert
    }
}