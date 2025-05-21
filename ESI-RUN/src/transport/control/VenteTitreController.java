package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import transport.core.*;
import transport.ui.MainApp;

public class VenteTitreController {

    @FXML
    private ComboBox<Usager> usagerCombo;

    @FXML
    private RadioButton ticketRadio;

    @FXML
    private RadioButton carteRadio;

    @FXML
    private ToggleGroup typeTitreGroup;

    @FXML
    private ComboBox<ModePaiement> modePaiementCombo;

    @FXML
    private Label prixLabel;

    @FXML
    private Label typeCarteLabel;

    @FXML
    private Label typeCarteValueLabel;

    @FXML
    private Label messageLabel;

    private GestionnaireTransport gestionnaire;
    private TypeCarte typeCarteDetermine;

    @FXML
    private void initialize() {
        gestionnaire = MainApp.getGestionnaireTransport();

        // Initialize the combobox with the users
        usagerCombo.setItems(FXCollections.observableArrayList(gestionnaire.getUsagers()));

        // Initialize the combobox with the payment modes
        modePaiementCombo.setItems(FXCollections.observableArrayList(ModePaiement.values()));
        modePaiementCombo.getSelectionModel().selectFirst();

        // Add listeners to update the display
        usagerCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateAffichage();
        });

        ticketRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateAffichage();
        });

        carteRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateAffichage();
        });
    }

    private void updateAffichage() {  //update the display
        Usager usager = usagerCombo.getValue();
        if (usager == null) {  //if the user is not selected
            prixLabel.setText("0 DA");
            typeCarteLabel.setVisible(false);
            typeCarteValueLabel.setVisible(false);
            messageLabel.setVisible(false);
            typeCarteDetermine = null;
            return;
        }

        if (ticketRadio.isSelected()) {
            // Ticket mode
            prixLabel.setText("50 DA");
            typeCarteLabel.setVisible(false);
            typeCarteValueLabel.setVisible(false);
            messageLabel.setVisible(false);
            typeCarteDetermine = null;
        } else {
            // Navigation card mode
            try {
                // Determine the type of user and display debug information
                System.out.println("Selected user: " + usager.getNom() + " " + usager.getPrenom());
                System.out.println("Type d'usager: " + usager.getTypeUsager());
                System.out.println("Mobilité réduite: " + usager.aMobiliteReduite());

                // Try to determine the type of card for this user
                typeCarteDetermine = usager.determinerTypeCarteOptimal();

                // Check that the type of card has been correctly determined
                if (typeCarteDetermine != null) {
                    

                    // Calculate the price based on the type of card
                    double prixBase = 5000.0;
                    double reduction = typeCarteDetermine.getTauxReduction() / 100.0;
                    double prixFinal = prixBase * (1 - reduction);

                    // Update the display
                    prixLabel.setText(String.format("%.2f DA", prixFinal));
                    typeCarteLabel.setVisible(true);
                    typeCarteValueLabel.setVisible(true);
                    typeCarteValueLabel.setText(typeCarteDetermine.toString() + " (-" + typeCarteDetermine.getTauxReduction() + "%)");
                    messageLabel.setVisible(false);
                } else {
                    // Null card type
                    prixLabel.setText("Error");
                    typeCarteLabel.setVisible(false);
                    typeCarteValueLabel.setVisible(false);
                    messageLabel.setVisible(true);
                    messageLabel.setText("impossible to determine the type of card for this user.");
                }
            } catch (ReductionImpossibleException e) {
                // The user does not have the right to a reduction
                prixLabel.setText("Not available");
                typeCarteLabel.setVisible(false);
                typeCarteValueLabel.setVisible(false);
                messageLabel.setVisible(true);
                messageLabel.setText("This user does not have the right to a reduction for a navigation card.");
                typeCarteDetermine = null;
            } catch (Exception e) {
                // Other error
                e.printStackTrace(); //debug test !!!!!!!
                prixLabel.setText("Error");
                typeCarteLabel.setVisible(false);
                typeCarteValueLabel.setVisible(false);
                messageLabel.setVisible(true);
                messageLabel.setText("Error: " + e.getMessage());
                typeCarteDetermine = null;
            }
        }
    }

    @FXML
    private void vendreTitre(ActionEvent event) {
        // Check that all necessary fields are filled
        Usager usager = usagerCombo.getValue();  //get the selected user
        if (usager == null || modePaiementCombo.getValue() == null) {
            afficherErreur("Incomplete fields", "Please select a user and a payment mode.");
            return;
        }

        // Check that a card can be created if this is the selected option
        if (carteRadio.isSelected() && typeCarteDetermine == null) {
            afficherErreur("Creation impossible", "This user does not have the right to a personal navigation card.");
            return;
        }

        ModePaiement modePaiement = modePaiementCombo.getValue();

        // Create the appropriate transport title
        try {
            TitreTransport nouveauTitre;

            if (ticketRadio.isSelected()) {  //create a ticket
                // Create a ticket
                Ticket ticket = new Ticket();
                ticket.setUsager(usager);
                ticket.setModePaiement(modePaiement);
                nouveauTitre = ticket;
            } else {
                // Create a navigation card
                nouveauTitre = new CarteNavigation(usager, modePaiement);
            }

            // Add the title to the manager
            gestionnaire.ajouterTitre(nouveauTitre);

            // Display a confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful sale");
            alert.setHeaderText(null);

            if (nouveauTitre instanceof Ticket) {
                alert.setContentText("The ticket has been sold successfully.\n" +
                        "Title ID: " + nouveauTitre.getId() + "\n" +
                        "Price: " + nouveauTitre.getPrix() + " DA");
            } else {
                CarteNavigation carte = (CarteNavigation) nouveauTitre;
                String typeCarte = (carte.getType() != null) ? carte.getType().toString() : "Non défini";
                alert.setContentText("The navigation card has been sold successfully.\n" +
                        "Title ID: " + carte.getId() + "\n" +
                        "Card type: " + typeCarte + "\n" +
                        "Price: " + carte.getPrix() + " DA\n" +
                        "Valid until " + carte.getDateExpiration());
            }

            alert.showAndWait();  //show the alert

            // Close the window
            fermerFenetre();
        } catch (Exception e) {
            afficherErreur("Error during the sale", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {  //close the window
        fermerFenetre();
    }

    private void fermerFenetre() {  //close the window
        Stage stage = (Stage) usagerCombo.getScene().getWindow();
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