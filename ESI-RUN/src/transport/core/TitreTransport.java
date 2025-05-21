package transport.core;

import java.io.Serializable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public abstract class TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String COMPTEUR_FILE = "compteur.txt"; 

    protected String id;  // ID as a String
    protected LocalDate dateAchat;
    protected double prix;
    protected Usager usager;
    protected ModePaiement modePaiement;
    protected boolean valide = false;
    private static int compteur = 0;

    public TitreTransport() {
        //load compteur from file compteur.txt
        //create file if not exists and give compteur value 0
        if (!Files.exists(Paths.get(COMPTEUR_FILE))) {
            try {
                Files.createFile(Paths.get(COMPTEUR_FILE));
                Files.writeString(Paths.get(COMPTEUR_FILE), "0");  //set compteur to 0 as default(first time the program is run)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            compteur = Integer.parseInt(Files.readString(Paths.get(COMPTEUR_FILE)));  //load the compteur from the file
        } catch (IOException e) {
            e.printStackTrace();
        }
        compteur++;
        this.id = String.valueOf(compteur);  // Convert to String
        //save compteur to file compteur.txt
        try {
            Files.writeString(Paths.get(COMPTEUR_FILE), String.valueOf(compteur));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.dateAchat = LocalDate.now();
        this.prix = calculerPrix();
    }

    // Initialize the user and the payment mode
    public void initialiser(Usager usager, ModePaiement modePaiement) {
        this.usager = usager;
        this.modePaiement = modePaiement;
        this.prix = calculerPrix();  // recalculate the price after initialization
    }

    // Abstract method to calculate the price of the title
    protected abstract double calculerPrix();

    // Method to check if the title is not expired (current date)
    public boolean estValide() {
        try {
            return estValide(LocalDate.now());
        } catch (TitreNonValideException e) {
            return false;
        }
    }
    
    // Abstract method to check if the title is not expired at a given date
    public abstract boolean estValide(LocalDate dateUtilisation) throws TitreNonValideException;

    // Method to validate a title (simulation of passing through the magnetic reader)
    public void valider() throws TitreNonValideException {
        if (!estValide()) {
            throw new TitreNonValideException("Title expired, validation impossible.");
        }
        this.valide = true;
    }

    // Indicate if the title has been validated or not
    public boolean aEteValide() {
        return valide;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public double getPrix() {
        return prix;
    }

    public Usager getUsager() {
        return usager;
    }

    public void setUsager(Usager usager) {
        this.usager = usager;
    }
    
    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    

    public TitreTransport(String id) {this.id = id;}
    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    @Override
    public String toString() {
        String idStr = String.valueOf(id);    //get the id as a string
        String idDisplay = idStr.length() >= 8 ? idStr.substring(0, 8) : idStr; //if the id is less than 8 characters, add 0 to the left
        return "Titre #" + idDisplay + " (" + dateAchat + ")";  //return the title with the id, the date of purchase and the payment mode
    }
}