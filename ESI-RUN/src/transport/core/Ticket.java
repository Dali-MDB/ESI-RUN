package transport.core;

import java.io.Serializable;
import java.time.LocalDate;

public class Ticket extends TitreTransport implements Serializable {

    private static final double PRIX_BASE = 50.0;

    public Ticket() {
        super();
    }

    // Constructeur complet pour l'application
    public Ticket(Usager usager, ModePaiement modePaiement) {
        super();
        setUsager(usager);
        setModePaiement(modePaiement);
        this.prix = calculerPrix();
    }

    @Override
    protected double calculerPrix() {
        return PRIX_BASE;
    }

    @Override
    public boolean estValide(LocalDate dateUtilisation) throws TitreNonValideException {
        // Un ticket est valide uniquement le jour de son achat
        if (dateAchat.isEqual(dateUtilisation)) {
            return true;
        } else {
            throw new TitreNonValideException("Ticket numéro " + id + " expiré - valable uniquement le : " + dateAchat);
        }
    }
}