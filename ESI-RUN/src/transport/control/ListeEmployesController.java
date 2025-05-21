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
        //configure the columns
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));  //set the value of the column

        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));  //set the value of the column

        fonctionColumn.setCellValueFactory(new PropertyValueFactory<>("fonction"));
        fonctionColumn.setCellFactory(column -> {  //set the value of the column
            return new TableCell<Employe, TypeFonction>() {
                @Override
                protected void updateItem(TypeFonction fonction, boolean empty) {  //set the value of the column
                    super.updateItem(fonction, empty);

                    if (empty || fonction == null) {  //if the column is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(fonction.toString());
                    }
                }
            };
        });

        //set the value of the column
        stationColumn.setCellValueFactory(new PropertyValueFactory<>("station"));
        //set the value of the column
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        //set the value of the column
        mobiliteReduiteColumn.setCellValueFactory(cellData -> {
            Employe employe = cellData.getValue();
            return new javafx.beans.property.SimpleBooleanProperty(employe.aMobiliteReduite());
        });
        //set the value of the column
        mobiliteReduiteColumn.setCellFactory(column -> {
            return new TableCell<Employe, Boolean>()   {  //set the value of the column
                @Override
                protected void updateItem(Boolean mobiliteReduite, boolean empty) {
                    super.updateItem(mobiliteReduite, empty);  //set the value of the column

                    if (empty || mobiliteReduite == null) {  //if the column is empty or the value is null, set the value to null
                        setText(null);
                    } else {
                        setText(mobiliteReduite ? "Oui" : "Non");
                    }
                }
            };
        });

        //load the data
        actualiserListe();
    }

    @FXML
    private void actualiserListe() { //refresh the list of employees
        employesTable.setItems(FXCollections.observableArrayList(
                MainApp.getGestionnaireTransport().getEmployes()
        ));
    }

    @FXML
    private void fermer(ActionEvent event) { //close the window
        Stage stage = (Stage) employesTable.getScene().getWindow();
        stage.close();
    }
}