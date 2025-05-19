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
        // Configurer les colonnes
        idColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            String id = String.valueOf(titre.getId());
            String idDisplay = safeSubstring(id, 0, 8); // Utilisation de la méthode sécurisée
            return new javafx.beans.property.SimpleStringProperty(idDisplay);
        });

        typeColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            String type = titre.getClass().getSimpleName();
            return new javafx.beans.property.SimpleStringProperty(type);
        });

        usagerColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            if (titre.getUsager() == null) {
                return new javafx.beans.property.SimpleStringProperty("Non défini");
            }
            String usagerNom = titre.getUsager().getNom() + " " + titre.getUsager().getPrenom();
            return new javafx.beans.property.SimpleStringProperty(usagerNom);
        });

        dateAchatColumn.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));
        dateAchatColumn.setCellFactory(column -> {
            return new TableCell<TitreTransport, LocalDate>() {
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

        typeCarteColumn.setCellValueFactory(cellData -> {
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
                    super.updateItem(type, empty);

                    if (empty || type == null) {
                        setText(null);
                    } else {
                        setText(type.toString());
                    }
                }
            };
        });

        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        prixColumn.setCellFactory(column -> {
            return new TableCell<TitreTransport, Double>() {
                @Override
                protected void updateItem(Double prix, boolean empty) {
                    super.updateItem(prix, empty);

                    if (empty || prix == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f DA", prix));
                    }
                }
            };
        });

        // Colonne indiquant si le titre n'est pas expiré
        validiteColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            boolean estValide = titre.estValide();
            return new javafx.beans.property.SimpleBooleanProperty(estValide);
        });

        validiteColumn.setCellFactory(column -> {
            return new TableCell<TitreTransport, Boolean>() {
                @Override
                protected void updateItem(Boolean estValide, boolean empty) {
                    super.updateItem(estValide, empty);

                    if (empty || estValide == null) {
                        setText(null);
                    } else {
                        setText(estValide ? "Valide" : "Expiré");
                        setTextFill(estValide ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
                    }
                }
            };
        });

        // Nouvelle colonne pour indiquer si le titre a été validé par un lecteur
        validationColumn.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            boolean aEteValide = titre.aEteValide();
            return new javafx.beans.property.SimpleBooleanProperty(aEteValide);
        });

        validationColumn.setCellFactory(column -> {
            return new TableCell<TitreTransport, Boolean>() {
                @Override
                protected void updateItem(Boolean aEteValide, boolean empty) {
                    super.updateItem(aEteValide, empty);

                    if (empty || aEteValide == null) {
                        setText(null);
                    } else {
                        setText(aEteValide ? "Validé" : "Non validé");
                        setTextFill(aEteValide ? javafx.scene.paint.Color.BLUE : javafx.scene.paint.Color.ORANGE);
                    }
                }
            };
        });


        // Charger les données
        actualiserListe();
    }

    @FXML
    private void actualiserListe() {
        titresTable.setItems(FXCollections.observableArrayList(
                MainApp.getGestionnaireTransport().getTitres()
        ));
    }

    @FXML
    private void fermer(ActionEvent event) {
        Stage stage = (Stage) titresTable.getScene().getWindow();
        stage.close();
    }

    // Méthode utilitaire pour extraire une sous-chaîne en toute sécurité
    private String safeSubstring(String str, int start, int end) {
        if (str == null) return "";
        end = Math.min(end, str.length());
        if (start >= end) return "";
        return str.substring(start, end);
    }
}