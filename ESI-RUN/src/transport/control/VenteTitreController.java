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

        // Initialiser la combobox avec les usagers
        usagerCombo.setItems(FXCollections.observableArrayList(gestionnaire.getUsagers()));

        // Initialiser la combobox avec les modes de paiement
        modePaiementCombo.setItems(FXCollections.observableArrayList(ModePaiement.values()));
        modePaiementCombo.getSelectionModel().selectFirst();

        // Ajouter des listeners pour mettre à jour l'affichage
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

    private void updateAffichage() {
        Usager usager = usagerCombo.getValue();
        if (usager == null) {
            prixLabel.setText("0 DA");
            typeCarteLabel.setVisible(false);
            typeCarteValueLabel.setVisible(false);
            messageLabel.setVisible(false);
            typeCarteDetermine = null;
            return;
        }

        if (ticketRadio.isSelected()) {
            // Mode ticket
            prixLabel.setText("50 DA");
            typeCarteLabel.setVisible(false);
            typeCarteValueLabel.setVisible(false);
            messageLabel.setVisible(false);
            typeCarteDetermine = null;
        } else {
            // Mode carte de navigation
            try {
                // Déterminer le type d'usager et afficher des informations de débogage
                System.out.println("Usager sélectionné: " + usager.getNom() + " " + usager.getPrenom());
                System.out.println("Type d'usager: " + usager.getTypeUsager());
                System.out.println("Mobilité réduite: " + usager.aMobiliteReduite());

                // Essayer de déterminer le type de carte pour cet usager
                typeCarteDetermine = usager.determinerTypeCarteOptimal();

                // Vérifier que le type de carte a été correctement déterminé
                if (typeCarteDetermine != null) {
                    System.out.println("Type de carte déterminé: " + typeCarteDetermine);

                    // Calculer le prix en fonction du type de carte
                    double prixBase = 5000.0;
                    double reduction = typeCarteDetermine.getTauxReduction() / 100.0;
                    double prixFinal = prixBase * (1 - reduction);

                    // Mettre à jour l'affichage
                    prixLabel.setText(String.format("%.2f DA", prixFinal));
                    typeCarteLabel.setVisible(true);
                    typeCarteValueLabel.setVisible(true);
                    typeCarteValueLabel.setText(typeCarteDetermine.toString() + " (-" + typeCarteDetermine.getTauxReduction() + "%)");
                    messageLabel.setVisible(false);
                } else {
                    // Type de carte null
                    prixLabel.setText("Erreur");
                    typeCarteLabel.setVisible(false);
                    typeCarteValueLabel.setVisible(false);
                    messageLabel.setVisible(true);
                    messageLabel.setText("Impossible de déterminer le type de carte pour cet usager.");
                }
            } catch (ReductionImpossibleException e) {
                // L'usager n'a pas droit à une réduction
                prixLabel.setText("Non disponible");
                typeCarteLabel.setVisible(false);
                typeCarteValueLabel.setVisible(false);
                messageLabel.setVisible(true);
                messageLabel.setText("Cet usager n'a droit à aucune réduction pour une carte de navigation.");
                typeCarteDetermine = null;
            } catch (Exception e) {
                // Autre erreur
                e.printStackTrace(); // Pour le débogage
                prixLabel.setText("Erreur");
                typeCarteLabel.setVisible(false);
                typeCarteValueLabel.setVisible(false);
                messageLabel.setVisible(true);
                messageLabel.setText("Erreur: " + e.getMessage());
                typeCarteDetermine = null;
            }
        }
    }

    @FXML
    private void vendreTitre(ActionEvent event) {
        // Vérifier que tous les champs nécessaires sont remplis
        Usager usager = usagerCombo.getValue();
        if (usager == null || modePaiementCombo.getValue() == null) {
            afficherErreur("Champs incomplets", "Veuillez sélectionner un usager et un mode de paiement.");
            return;
        }

        // Vérifier qu'une carte peut être créée si c'est l'option sélectionnée
        if (carteRadio.isSelected() && typeCarteDetermine == null) {
            afficherErreur("Création impossible", "Cet usager n'a pas droit à une carte de navigation personnelle.");
            return;
        }

        ModePaiement modePaiement = modePaiementCombo.getValue();

        // Créer le titre de transport approprié
        try {
            TitreTransport nouveauTitre;

            if (ticketRadio.isSelected()) {
                // Créer un ticket
                Ticket ticket = new Ticket();
                ticket.setUsager(usager);
                ticket.setModePaiement(modePaiement);
                nouveauTitre = ticket;
            } else {
                // Créer une carte de navigation
                nouveauTitre = new CarteNavigation(usager, modePaiement);
            }

            // Ajouter le titre au gestionnaire
            gestionnaire.ajouterTitre(nouveauTitre);

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vente réussie");
            alert.setHeaderText(null);

            if (nouveauTitre instanceof Ticket) {
                alert.setContentText("Le ticket a été vendu avec succès.\n" +
                        "ID du titre: " + nouveauTitre.getId() + "\n" +
                        "Prix: " + nouveauTitre.getPrix() + " DA");
            } else {
                CarteNavigation carte = (CarteNavigation) nouveauTitre;
                String typeCarte = (carte.getType() != null) ? carte.getType().toString() : "Non défini";
                alert.setContentText("La carte de navigation a été vendue avec succès.\n" +
                        "ID du titre: " + carte.getId() + "\n" +
                        "Type de carte: " + typeCarte + "\n" +
                        "Prix: " + carte.getPrix() + " DA\n" +
                        "Valable jusqu'au " + carte.getDateExpiration());
            }

            alert.showAndWait();

            // Fermer la fenêtre
            fermerFenetre();
        } catch (Exception e) {
            afficherErreur("Erreur lors de la vente", "Une erreur est survenue: " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) usagerCombo.getScene().getWindow();
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