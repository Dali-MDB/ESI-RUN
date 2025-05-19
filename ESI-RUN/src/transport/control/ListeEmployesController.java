package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import transport.core.Employe;
import transport.core.TypeFonction;
import transport.ui.MainApp;

public class ListeEmployesController {

    @FXML
    private TableView<Employe> employesTable;

    @FXML
    private TableColumn<Employe, String> matriculeColumn;

    @FXML
    private TableColumn<Employe, String> nomColumn;

    @FXML
    private TableColumn<Employe, String> prenomColumn;

    @FXML
    private TableColumn<Employe, TypeFonction> fonctionColumn;

    @FXML
    private TableColumn<Employe, String> stationColumn;

    @FXML
    private TableColumn<Employe, Integer> ageColumn;

    @FXML
    private TableColumn<Employe, Boolean> mobiliteReduiteColumn;

    @FXML
    private void initialize() {
        // Configurer les colonnes
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        fonctionColumn.setCellValueFactory(new PropertyValueFactory<>("fonction"));
        fonctionColumn.setCellFactory(column -> {
            return new TableCell<Employe, TypeFonction>() {
                @Override
                protected void updateItem(TypeFonction fonction, boolean empty) {
                    super.updateItem(fonction, empty);

                    if (empty || fonction == null) {
                        setText(null);
                    } else {
                        setText(fonction.toString());
                    }
                }
            };
        });

        stationColumn.setCellValueFactory(new PropertyValueFactory<>("station"));

        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        mobiliteReduiteColumn.setCellValueFactory(cellData -> {
            Employe employe = cellData.getValue();
            return new javafx.beans.property.SimpleBooleanProperty(employe.aMobiliteReduite());
        });

        mobiliteReduiteColumn.setCellFactory(column -> {
            return new TableCell<Employe, Boolean>() {
                @Override
                protected void updateItem(Boolean mobiliteReduite, boolean empty) {
                    super.updateItem(mobiliteReduite, empty);

                    if (empty || mobiliteReduite == null) {
                        setText(null);
                    } else {
                        setText(mobiliteReduite ? "Oui" : "Non");
                    }
                }
            };
        });

        // Charger les données
        actualiserListe();
    }

    @FXML
    private void actualiserListe() {
        employesTable.setItems(FXCollections.observableArrayList(
                MainApp.getGestionnaireTransport().getEmployes()
        ));
    }

    @FXML
    private void fermer(ActionEvent event) {
        Stage stage = (Stage) employesTable.getScene().getWindow();
        stage.close();
    }
}