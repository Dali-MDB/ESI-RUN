package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import transport.core.GestionnaireTransport;
import transport.core.Reclamation;
import transport.ui.MainApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeReclamationsController {

    @FXML
    private TableView<Reclamation> reclamationsTable;

    @FXML
    private TableColumn<Reclamation, String> typeColumn;

    @FXML
    private TableColumn<Reclamation, String> stationColumn;

    @FXML
    private TableColumn<Reclamation, String> statutColumn;

    @FXML
    private TableColumn<Reclamation, String> declarantColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;

    @FXML
    private VBox alerteBox;

    @FXML
    private Label alerteLabel;

    private GestionnaireTransport gestionnaire;

    @FXML
    private void initialize() {
        try {
            // Vérification des objets FXML
            System.out.println("reclamationsTable: " + (reclamationsTable != null ? "OK" : "NULL"));
            System.out.println("typeColumn: " + (typeColumn != null ? "OK" : "NULL"));
            System.out.println("stationColumn: " + (stationColumn != null ? "OK" : "NULL"));
            System.out.println("statutColumn: " + (statutColumn != null ? "OK" : "NULL"));
            System.out.println("declarantColumn: " + (declarantColumn != null ? "OK" : "NULL"));
            System.out.println("descriptionColumn: " + (descriptionColumn != null ? "OK" : "NULL"));
            System.out.println("dateColumn: " + (dateColumn != null ? "OK" : "NULL"));

            gestionnaire = MainApp.getGestionnaireTransport();

            // Configurer les colonnes
            if (typeColumn != null) {
                typeColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    return new javafx.beans.property.SimpleStringProperty(reclamation.getType().toString());
                });
            }

            if (stationColumn != null) {
                stationColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    return new javafx.beans.property.SimpleStringProperty(reclamation.getStationConcernee());
                });
            }

            if (statutColumn != null) {
                statutColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    boolean suspendue = gestionnaire.isStationSuspendue(reclamation.getStationConcernee());
                    return new javafx.beans.property.SimpleStringProperty(suspendue ? "Suspendue" : "Active");
                });

                statutColumn.setCellFactory(column -> {
                    return new TableCell<Reclamation, String>() {
                        @Override
                        protected void updateItem(String statut, boolean empty) {
                            super.updateItem(statut, empty);

                            if (empty || statut == null) {
                                setText(null);
                                setTextFill(Color.BLACK);
                            } else {
                                setText(statut);
                                if ("Suspendue".equals(statut)) {
                                    setTextFill(Color.RED);
                                } else {
                                    setTextFill(Color.GREEN);
                                }
                            }
                        }
                    };
                });
            }

            if (declarantColumn != null) {
                declarantColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    if (reclamation.getDeclarant() == null) {
                        return new javafx.beans.property.SimpleStringProperty("Inconnu");
                    }
                    String declarantNom = reclamation.getDeclarant().getNom() + " " + reclamation.getDeclarant().getPrenom();
                    return new javafx.beans.property.SimpleStringProperty(declarantNom);
                });
            }

            if (descriptionColumn != null) {
                descriptionColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    // Limiter la description à 50 caractères pour l'affichage
                    String description = reclamation.getDescription();
                    if (description != null && description.length() > 50) {
                        description = description.substring(0, 47) + "...";
                    }
                    return new javafx.beans.property.SimpleStringProperty(description);
                });
            }

            if (dateColumn != null) {
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReclamation"));
                dateColumn.setCellFactory(column -> {
                    return new TableCell<Reclamation, LocalDate>() {
                        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        @Override
                        protected void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);

                            if (empty || date == null) {
                                setText(null);
                            } else {
                                setText(formatter.format(date));
                            }
                        }
                    };
                });
            }

            // Charger les données
            actualiserListe();

            // Vérifier les stations suspendues
            afficherStationsSuspendues();
        } catch (Exception e) {
            System.err.println("Erreur d'initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void actualiserListe() {
        try {
            reclamationsTable.setItems(FXCollections.observableArrayList(
                    gestionnaire.getReclamations()
            ));

            // Vérifier à nouveau les stations suspendues après actualisation
            afficherStationsSuspendues();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'actualisation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherStationsSuspendues() {
        try {
            // Récupérer la liste des stations suspendues
            List<String> stationsSuspendues = gestionnaire.getStationsSuspendues();

            if (!stationsSuspendues.isEmpty()) {
                // Construire le message d'alerte
                StringBuilder message = new StringBuilder("ALERTE: Les stations suivantes sont suspendues:");
                for (String station : stationsSuspendues) {
                    message.append("\n- ").append(station);
                }

                // Afficher l'alerte
                alerteLabel.setText(message.toString());
                alerteBox.setVisible(true);
            } else {
                alerteBox.setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des stations suspendues: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fermer(ActionEvent event) {
        Stage stage = (Stage) reclamationsTable.getScene().getWindow();
        stage.close();
    }
}