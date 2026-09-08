package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service de gestion des patients du cabinet médical.
 * <p>Cette classe centralise les opérations CRUD pour la gestion des patients
 * et leurs informations personnelles.</p>
 */
public class GestionPatients {


    /** Logger de la classe. */
    private static final Logger logger = LoggerFactory.getLogger(GestionPatients.class);

    /** Liste interne des patients enregistrés sous forme d'ObservableList. */
    private final ObservableList<Patient> patients;

    /**
     * Construit un service de gestion des patients avec une liste vide.
     */
    public GestionPatients() {
        // Initialisation avec l'ObservableList de JavaFX
        this.patients = FXCollections.observableArrayList();
    }

    /**
     * Ajoute un nouveau patient au système si son numéro de sécurité sociale est unique.
     *
     * @param patient le patient à ajouter
     */
    public void ajouterPatient(Patient patient) {
        if (rechercherParNumeroSecu(patient.getNumeroSecu()) != null) {
            logger.info("ERREUR : Un patient avec ce numéro de sécu existe déjà.");
            return;
        }
        patients.add(patient);
        logger.info("Patient ajouté : {}", patient.getNomComplet());
    }

    /**
     * Recherche un patient par son identifiant unique.
     *
     * @param id l'identifiant du patient recherché
     * @return le patient correspondant, ou {@code null} s'il est introuvable
     */
    public Patient rechercherParId(int id) {
        return patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Recherche un patient par son numéro de sécurité sociale.
     *
     * @param numeroSecu le numéro de sécurité sociale recherché
     * @return le patient correspondant, ou {@code null} s'il est introuvable
     */
    public Patient rechercherParNumeroSecu(String numeroSecu) {
        return patients.stream()
                .filter(p -> p.getNumeroSecu().equalsIgnoreCase(numeroSecu))
                .findFirst()
                .orElse(null);
    }

    /**
     * Recherche les patients dont le nom ou le prénom contient le terme spécifié.
     * <p>La recherche est insensible à la casse.</p>
     *
     * @param terme le terme de recherche
     * @return une liste observable de patients correspondants
     */
    public ObservableList<Patient> rechercherParNom(String terme) {
        String termeLower = terme.toLowerCase();
        return patients.stream()
                .filter(p -> p.getNom().toLowerCase().contains(termeLower) ||
                        p.getPrenom().toLowerCase().contains(termeLower))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Met à jour les informations d'un patient existant.
     *
     * @param id                       l'identifiant du patient
     * @param nouveauNom               le nouveau nom
     * @param nouveauPrenom            le nouveau prénom
     * @param nouveauTel               le nouveau numéro de téléphone
     * @param nouveauEmail             la nouvelle adresse e-mail
     * @param nouvelleAdresse          la nouvelle adresse postale
     * @param nouveauTauxRemboursement le nouveau taux de remboursement
     */
    public void mettreAJourPatient(int id, String nouveauNom, String nouveauPrenom,
                                   String nouveauTel, String nouveauEmail,
                                   String nouvelleAdresse, double nouveauTauxRemboursement) {
        Patient p = rechercherParId(id);
        if (p == null) {
            logger.info("ERREUR : Patient ID={} introuvable.", id);
            return;
        }
        p.setNom(nouveauNom);
        p.setPrenom(nouveauPrenom);
        p.setTelephone(nouveauTel);
        p.setEmail(nouveauEmail);
        p.setAdresse(nouvelleAdresse);
        p.setTauxRemboursement(nouveauTauxRemboursement);
        logger.info("Patient mis à jour : {}", p.getNomComplet());
    }

    /**
     * Supprime un patient du système par son identifiant.
     *
     * @param id l'identifiant du patient à supprimer
     */
    public void supprimerPatient(int id) {
        Patient p = rechercherParId(id);
        if (p == null) {
            logger.info("ERREUR : Patient ID={} introuvable.", id);
            return;
        }
        patients.remove(p);
        logger.info("Patient supprimé : {}", p.getNomComplet());
    }
    /** Instance unique partagée par toute l'application. */
    private static final GestionPatients INSTANCE = new GestionPatients();

    /** @return l'instance unique de gestion des patients. */
    public static GestionPatients getInstance() {
        return INSTANCE;
    }

    /**
     * Retourne une copie de la liste complète des patients enregistrés.
     *
     * @return la liste observable de tous les patients
     */
    public ObservableList<Patient> listerTousLesPatients() {
        return FXCollections.observableArrayList(patients);
    }

    /**
     * Retourne l'ObservableList interne des patients.
     * <p>À lier directement à une ListView : tout ajout/suppression via le
     * service sera automatiquement répercuté dans la vue.</p>
     *
     * @return la liste observable interne des patients
     */
    public ObservableList<Patient> getPatients() {
        return patients;
    }

    /**
     * Retourne le nombre total de patients enregistrés.
     *
     * @return le nombre de patients
     */
    public int getNombrePatients() {
        return patients.size();
    }
}