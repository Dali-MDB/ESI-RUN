package transport.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transport.core.GestionnaireTransport;

import java.io.IOException;

public class MainApp extends Application {

    private static GestionnaireTransport gestionnaireTransport;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialisation du gestionnaire
            try {
                gestionnaireTransport = GestionnaireTransport.chargerDonnees();
            } catch (Exception e) {
                gestionnaireTransport = new GestionnaireTransport();
            }

            // Chargement de la vue principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();

            // Configuration de la scène principale
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("ESI-RUN - Gestion de Transport");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Sauvegarde des données à la fermeture de l'application
        try {
            gestionnaireTransport.sauvegarderDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GestionnaireTransport getGestionnaireTransport() {
        return gestionnaireTransport;
    }

    public static void setGestionnaireTransport(GestionnaireTransport gestionnaire) {
        gestionnaireTransport = gestionnaire;
    }

    public static void main(String[] args) {
        launch(args);
    }
}