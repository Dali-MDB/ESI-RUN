package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import transport.core.CarteNavigation;
import transport.core.TitreTransport;
import transport.core.TypeCarte;
import transport.ui.MainApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ListeTitresController {

    @FXML
    private TableView<TitreTransport> titresTable;

    @FXML
    private TableColumn<TitreTransport, String> idColumn;

    @FXML
    private TableColumn<TitreTransport, String> typeColumn;

    @FXML
    private TableColumn<TitreTransport, String> usagerColumn;

    @FXML
    private TableColumn<TitreTransport, LocalDate> dateAchatColumn;

    @FXML
    private TableColumn<TitreTransport, TypeCarte> typeCarteColumn;

    @FXML
    private TableColumn<TitreTransport, Double> prixColumn;

    @FXML
    private TableColumn<TitreTransport, Boolean> validiteColumn;

    @FXML
    private TableColumn<TitreTransport, Boolean> validationColumn;

    @FXML
    private void initialize() {
        //configure the columns
        idColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            String id = String.valueOf(titre.getId());
            String idDisplay = safeSubstring(id, 0, 8); //use the safe substring method
            return new javafx.beans.property.SimpleStringProperty(idDisplay);
        });

        typeColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            String type = titre.getClass().getSimpleName(); //get the type of the title
            return new javafx.beans.property.SimpleStringProperty(type);
        });

        usagerColumn.setCellValueFactory(cellData -> {  //get the usager of the title
            TitreTransport titre = cellData.getValue();
            if (titre.getUsager() == null) {  //if the usager is null, set the value to "Non défini"
                return new javafx.beans.property.SimpleStringProperty("Non défini");
            }
            String usagerNom = titre.getUsager().getNom() + " " + titre.getUsager().getPrenom();  //get the name and the first name of the usager
            return new javafx.beans.property.SimpleStringProperty(usagerNom);
        });

        //get the date of the purchase
        dateAchatColumn.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));
        dateAchatColumn.setCellFactory(column -> {  //set the date of the purchase
            return new TableCell<TitreTransport, LocalDate>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);  //update the date of the purchase

                    if (empty || date == null) {  //if the date is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(formatter.format(date));  //set the date of the purchase
                    }
                }
            };
        });

        typeCarteColumn.setCellValueFactory(cellData -> {  //get the type of the title
            TitreTransport titre = cellData.getValue();
            if (titre instanceof CarteNavigation) {
                CarteNavigation carte = (CarteNavigation) titre;
                return new javafx.beans.property.SimpleObjectProperty<>(carte.getType());
            } else {
                return new javafx.beans.property.SimpleObjectProperty<>();
            }
        });

        typeCarteColumn.setCellFactory(column -> {
            return new TableCell<TitreTransport, TypeCarte>() {
                @Override
                protected void updateItem(TypeCarte type, boolean empty) {
                    super.updateItem(type, empty);  //update the type of the title

                    if (empty || type == null) {  //if the type is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(type.toString());  //set the type of the title
                    }
                }
            };
        });

        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prixColumn.setCellFactory(column -> {  //set the price of the title
            return new TableCell<TitreTransport, Double>() {
                @Override
                protected void updateItem(Double prix, boolean empty) {
                    super.updateItem(prix, empty);  //update the price of the title

                    if (empty || prix == null) {  //if the price is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(String.format("%.2f DA", prix));  //set the price of the title
                    }
                }
            };
        });

        // Column indicating if the title is not expired
        validiteColumn.setCellValueFactory(cellData -> {  //get the validity of the title
            TitreTransport titre = cellData.getValue();
            boolean estValide = titre.estValide();
            return new javafx.beans.property.SimpleBooleanProperty(estValide);
        });

        validiteColumn.setCellFactory(column -> {  //set the validity of the title
            return new TableCell<TitreTransport, Boolean>() {
                @Override
                protected void updateItem(Boolean estValide, boolean empty) {
                    super.updateItem(estValide, empty);

                    if (empty || estValide == null) {  //if the validity is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(estValide ? "Valide" : "Expiré");  //set the validity of the title
                        setTextFill(estValide ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
                    }
                }
            };
        });

        // New column to indicate if the title has been validated by a reader
        validationColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            boolean aEteValide = titre.aEteValide();
            return new javafx.beans.property.SimpleBooleanProperty(aEteValide);
        });

        validationColumn.setCellFactory(column -> {
            return new TableCell<TitreTransport, Boolean>() {
                @Override
                protected void updateItem(Boolean aEteValide, boolean empty) {
                    super.updateItem(aEteValide, empty);  //update the validation of the title

                    if (empty || aEteValide == null) {  //if the validation is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(aEteValide ? "Validé" : "Non validé");
                        setTextFill(aEteValide ? javafx.scene.paint.Color.BLUE : javafx.scene.paint.Color.ORANGE);
                    }
                }
            };
        });


        //load the data
        actualiserListe();
    }

    @FXML
    private void actualiserListe() {  //refresh the list of titles
        titresTable.setItems(FXCollections.observableArrayList(
                MainApp.getGestionnaireTransport().getTitres()
        ));
    }

    @FXML
    private void fermer(ActionEvent event) {  //close the window
        Stage stage = (Stage) titresTable.getScene().getWindow();
        stage.close();
    }

    // Utility method to extract a substring safely
    private String safeSubstring(String str, int start, int end) {
        if (str == null) return "";  //if the string is null, return an empty string
        end = Math.min(end, str.length());  //set the end of the substring
        if (start >= end) return "";  //if the start is greater than the end, return an empty string
        return str.substring(start, end);  //return the substring
    }
}