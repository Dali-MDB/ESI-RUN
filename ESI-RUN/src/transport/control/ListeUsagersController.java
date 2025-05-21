package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import transport.core.Usager;
import transport.ui.MainApp;

public class ListeUsagersController {

    @FXML
    private TableView<Usager> usagersTable;

    @FXML
    private TableColumn<Usager, String> nomColumn;

    @FXML
    private TableColumn<Usager, String> prenomColumn;

    @FXML
    private TableColumn<Usager, Integer> ageColumn;

    @FXML
    private TableColumn<Usager, String> typeColumn;

    @FXML
    private void initialize() {
        //configure the columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        typeColumn.setCellValueFactory(cellData -> {
            Usager usager = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(usager.getTypeUsager().toString());
        });

        //load the data
        actualiserListe();
    }

    @FXML
    private void actualiserListe() {  //refresh the list of usagers
        usagersTable.setItems(FXCollections.observableArrayList(
                MainApp.getGestionnaireTransport().getUsagers()
        ));
    }

    @FXML
    private void fermer(ActionEvent event) {  //close the window
        Stage stage = (Stage) usagersTable.getScene().getWindow();
        stage.close();
    }
}