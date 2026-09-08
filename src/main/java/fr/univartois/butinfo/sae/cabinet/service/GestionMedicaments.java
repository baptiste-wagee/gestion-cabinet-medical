package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service de gestion des médicaments disponibles dans le système.
 * * <p>Cette classe fournit les opérations CRUD (Création, Lecture, Mise à jour, Suppression)
 * pour manipuler les médicaments au sein du cabinet médical.</p>
 */
public class GestionMedicaments {

    /** Logger de la classe. */
    private static final Logger logger = LoggerFactory.getLogger(GestionMedicaments.class);

    /** Liste interne des médicaments enregistrés. */
    private final ObservableList<Medicament> medicaments;

    /**
     * Construit un service de gestion des médicaments avec une liste vide.
     */
    public GestionMedicaments() {
        // Remplacer new ArrayList<>() par FXCollections.observableArrayList()
        this.medicaments = FXCollections.observableArrayList();
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * Ajoute un médicament au système s'il n'existe pas déjà.
     * * <p>La vérification d'existence se base sur la méthode {@code equals}
     * du modèle {@link Medicament}.</p>
     *
     * @param medicament le médicament à enregistrer
     */
    public void ajouterMedicament(Medicament medicament) {
        if (medicaments.contains(medicament)) {
            logger.info("ERREUR : Ce médicament existe déjà.");
            return;
        }
        medicaments.add(medicament);
        logger.info("Médicament ajouté : {}", medicament.getNom());
    }

    /**
     * Recherche un médicament par son identifiant unique.
     *
     * @param id l'identifiant du médicament recherché
     * @return le médicament correspondant, ou {@code null} s'il n'existe pas
     */
    public Medicament rechercherParId(int id) {
        for (Medicament m : medicaments) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    /**
     * Recherche les médicaments dont le nom contient le terme spécifié.
     * * <p>La recherche est insensible à la casse.</p>
     *
     * @param terme le terme de recherche
     * @return une liste des médicaments correspondants, vide si aucun résultat n'est trouvé
     */
    public ObservableList<Medicament> rechercherParNom(String terme) {
        // Correction ici : on initialise avec FXCollections
        ObservableList<Medicament> resultats = FXCollections.observableArrayList();
        String termeLower = terme.toLowerCase();

        for (Medicament m : medicaments) {
            if (m.getNom().toLowerCase().contains(termeLower)) {
                resultats.add(m); // .add() fonctionne exactement pareil !
            }
        }
        return resultats;
    }

    /**
     * Supprime un médicament du système par son identifiant.
     *
     * @param id l'identifiant du médicament à supprimer
     * @return {@code true} si le médicament a été supprimé, {@code false} s'il est introuvable
     */
    public boolean supprimerMedicament(int id) {
        Medicament m = rechercherParId(id);
        if (m == null) {
            logger.info("ERREUR : Médicament ID={} introuvable.", id);
            return false;
        }
        medicaments.remove(m);
        logger.info("Médicament supprimé : {}", m.getNom());
        return true;
    }

    /** Instance unique (singleton), comme GestionPatients. */
    private static GestionMedicaments instance;

    /**
     * Retourne l'instance unique du service.
     *
     * @return l'instance partagée de GestionMedicaments
     */
    public static GestionMedicaments getInstance() {
        if (instance == null) {
            instance = new GestionMedicaments();
        }
        return instance;
    }

    /**
     * Retourne la liste observable interne (vivante) des médicaments.
     * <p>À ne pas confondre avec {@link #listerTousLesMedicaments()} qui en
     * renvoie une copie. C'est cette liste-ci qu'il faut lier au ListView
     * pour qu'il se rafraîchisse automatiquement après ajout/suppression.</p>
     *
     * @return la liste observable des médicaments
     */
    public ObservableList<Medicament> getMedicaments() {
        return medicaments;
    }

    /**
     * Retourne une copie de la liste complète des médicaments enregistrés.
     *
     * @return la liste de tous les médicaments
     */
    public ObservableList<Medicament> listerTousLesMedicaments() {
        // On crée et retourne une copie sous forme d'ObservableList
        return FXCollections.observableArrayList(medicaments);
    }

    /**
     * Retourne le nombre total de médicaments enregistrés.
     *
     * @return le nombre de médicaments
     */
    public int getNombreMedicaments() {
        return medicaments.size();
    }
}