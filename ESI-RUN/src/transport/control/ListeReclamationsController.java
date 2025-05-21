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
            //check the objects of the FXML
            System.out.println("reclamationsTable: " + (reclamationsTable != null ? "OK" : "NULL"));
            System.out.println("typeColumn: " + (typeColumn != null ? "OK" : "NULL"));
            System.out.println("stationColumn: " + (stationColumn != null ? "OK" : "NULL"));
            System.out.println("statutColumn: " + (statutColumn != null ? "OK" : "NULL"));
            System.out.println("declarantColumn: " + (declarantColumn != null ? "OK" : "NULL"));
            System.out.println("descriptionColumn: " + (descriptionColumn != null ? "OK" : "NULL"));
            System.out.println("dateColumn: " + (dateColumn != null ? "OK" : "NULL"));

            gestionnaire = MainApp.getGestionnaireTransport();

            //configure the columns
            if (typeColumn != null) {
                typeColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    return new javafx.beans.property.SimpleStringProperty(reclamation.getType().toString());
                });
            }
            //set the value of the column
            if (stationColumn != null) {
                stationColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    return new javafx.beans.property.SimpleStringProperty(reclamation.getStationConcernee());
                });
            }
            //set the value of the column
            if (statutColumn != null) {
                statutColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    boolean suspendue = gestionnaire.isStationSuspendue(reclamation.getStationConcernee());
                    return new javafx.beans.property.SimpleStringProperty(suspendue ? "Suspendue" : "Active");
                });
            }
            //set the value of the column
            if (statutColumn != null) {
                statutColumn.setCellFactory(column -> {
                    return new TableCell<Reclamation, String>() {  //set the value of the column
                        @Override
                        protected void updateItem(String statut, boolean empty) {
                            super.updateItem(statut, empty);  //set the value of the column

                            if (empty || statut == null) { //if the column is empty or the value is null, set the value to null
                                setText(null);
                                setTextFill(Color.BLACK);
                            } else {
                                setText(statut);  //set the value of the column
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

            if (declarantColumn != null) {  //set the value of the column
                declarantColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();  
                    if (reclamation.getDeclarant() == null) {  //if the declarant is null, set the value to "Inconnu"
                        return new javafx.beans.property.SimpleStringProperty("Inconnu");
                    }
                    String declarantNom = reclamation.getDeclarant().getNom() + " " + reclamation.getDeclarant().getPrenom();
                    return new javafx.beans.property.SimpleStringProperty(declarantNom);
                });
            }

            if (descriptionColumn != null) {
                descriptionColumn.setCellValueFactory(cellData -> {
                    Reclamation reclamation = cellData.getValue();
                    //limit the description to 50 characters for the display
                    String description = reclamation.getDescription();
                    if (description != null && description.length() > 50) {  //if the description is longer than 50 characters, limit it to 47 characters and add "..."
                        description = description.substring(0, 47) + "...";
                    }
                    return new javafx.beans.property.SimpleStringProperty(description);
                });
            }

            if (dateColumn != null) {  
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReclamation")); //set the date of the reclamation
                dateColumn.setCellFactory(column -> {
                    return new TableCell<Reclamation, LocalDate>() {
                        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        @Override
                        protected void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);  //update the date of the reclamation

                            if (empty || date == null) {  //if the date is empty or the value is null, set the value to null
                                setText(null);
                            } else {
                                setText(formatter.format(date));  //set the date of the reclamation
                            }
                        }
                    };
                });
            }

            //load the data
            actualiserListe();

            //check the stations suspended
            afficherStationsSuspendues();
        } catch (Exception e) {
            System.err.println("Erreur d'initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void actualiserListe() { //refresh the list of reclamations
        try {
            reclamationsTable.setItems(FXCollections.observableArrayList(
                    gestionnaire.getReclamations()
            ));

            //check the stations suspended after the update
            afficherStationsSuspendues();
        } catch (Exception e) {
            System.err.println("Error during the update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherStationsSuspendues() {
        try {
            //get the list of suspended stations
            List<String> stationsSuspendues = gestionnaire.getStationsSuspendues();

            if (!stationsSuspendues.isEmpty()) {
                //build the alert message
                StringBuilder message = new StringBuilder("ALERTE: Les stations suivantes sont suspendues:");
                for (String station : stationsSuspendues) {
                    message.append("\n- ").append(station);
                }

                //display the alert
                alerteLabel.setText(message.toString());   //set the message of the alert
                alerteBox.setVisible(true);
            } else {
                alerteBox.setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("Error during the display of the suspended stations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fermer(ActionEvent event) { //close the window
        Stage stage = (Stage) reclamationsTable.getScene().getWindow();
        stage.close();   //close the window
    }
}