package transport.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import transport.core.GestionnaireTransport;

import java.io.IOException;

public class MainApp extends Application {

    private static GestionnaireTransport gestionnaireTransport;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialization of the manager
            try {
                gestionnaireTransport = GestionnaireTransport.chargerDonnees();  //load the data from the file
            } catch (Exception e) {
                gestionnaireTransport = new GestionnaireTransport();  //create a new manager if the file does not exist
            }

            // Loading the main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));  //load the main view
            Parent root = loader.load();

            // Configuration of the main scene
            Scene scene = new Scene(root, 800, 600);   //create a new scene with the main view
            primaryStage.setTitle("ESI-RUN - Gestion de Transport");
            
            // Set the application icon
            try {
                Image icon = new Image("file:LOGO.jpg");
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Could not load application icon: " + e.getMessage());
            }
            
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Saving the data at the closing of the application
        try {
            gestionnaireTransport.sauvegarderDonnees(); //save the data at the closing of the application
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GestionnaireTransport getGestionnaireTransport() { //getter for the manager
        return gestionnaireTransport;
    }

    public static void setGestionnaireTransport(GestionnaireTransport gestionnaire) {
        gestionnaireTransport = gestionnaire;
    }

    public static void main(String[] args) {
        launch(args);  //launch the application
    }
}