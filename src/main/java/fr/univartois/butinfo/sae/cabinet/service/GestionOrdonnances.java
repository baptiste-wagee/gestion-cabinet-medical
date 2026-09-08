package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.LignePrescription;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service de gestion des ordonnances médicales.
 * <p>Cette classe gère la création, la recherche et le suivi des prescriptions
 * au sein du système du cabinet médical.</p>
 */
public class GestionOrdonnances {

    /** Logger de la classe. */
    private static final Logger logger = LoggerFactory.getLogger(GestionOrdonnances.class);

    /** Instance unique du service (singleton). */
    private static GestionOrdonnances instance;

    /** Liste interne observable des ordonnances enregistrées. */
    private final ObservableList<Ordonnance> ordonnances;

    /**
     * Construit un service de gestion des ordonnances avec une liste vide.
     */
    public GestionOrdonnances() {
        this.ordonnances = FXCollections.observableArrayList();
    }

    /**
     * Retourne l'instance unique du service.
     *
     * @return l'instance partagée de GestionOrdonnances
     */
    public static GestionOrdonnances getInstance() {
        if (instance == null) {
            instance = new GestionOrdonnances();
        }
        return instance;
    }

    /**
     * Retourne la liste observable interne (vivante) des ordonnances.
     *
     * @return la liste observable des ordonnances
     */
    public ObservableList<Ordonnance> getOrdonnances() {
        return ordonnances;
    }

    /**
     * Crée une nouvelle ordonnance et l'enregistre dans le système.
     *
     * @param medecin      le médecin prescripteur
     * @param patient      le patient concerné
     * @param dateValidite la date de fin de validité de l'ordonnance
     * @param remarques    éventuelles remarques ou consignes complémentaires
     * @return l'ordonnance créée
     */
    public Ordonnance creerOrdonnance(Medecin medecin, Patient patient,
                                      LocalDate dateValidite, String remarques) {
        Ordonnance ordo = new Ordonnance(medecin, patient, dateValidite, remarques);
        ordonnances.add(ordo);
        logger.info("Ordonnance créée (ID={}) pour {} par Dr {}",
                ordo.getId(), patient.getNomComplet(), medecin.getNomComplet());
        return ordo;
    }

    /**
     * Ajoute une ligne de prescription à une ordonnance existante.
     *
     * @param ordonnanceId l'identifiant de l'ordonnance
     * @param medicament   le médicament à prescrire
     * @param quantite     la quantité prescrite
     * @param posologie    la posologie (fréquence de prise)
     * @param dureeJours   la durée du traitement en jours
     * @param instructions instructions spécifiques pour le patient
     */
    public void ajouterPrescription(int ordonnanceId, Medicament medicament, int quantite,
                                    String posologie, int dureeJours, String instructions) {
        Ordonnance ordo = rechercherParId(ordonnanceId);
        if (ordo == null) {
            logger.info("ERREUR : Ordonnance ID={} introuvable.", ordonnanceId);
            return;
        }
        LignePrescription ligne = new LignePrescription(medicament, quantite, posologie, dureeJours, instructions);
        ordo.ajouterPrescription(ligne);
        logger.info("Prescription ajoutée : {} → ordonnance ID={}", medicament.getNom(), ordonnanceId);
    }

    /**
     * Recherche une ordonnance par son identifiant unique.
     *
     * @param id l'identifiant recherché
     * @return l'ordonnance correspondante, ou {@code null} si elle n'existe pas
     */
    public Ordonnance rechercherParId(int id) {
        return ordonnances.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Supprime une ordonnance par son identifiant.
     * <p>MÉTHODE AJOUTÉE (absente de la version d'origine) pour permettre
     * l'écran de suppression, sur le modèle de GestionPatients/GestionMedecins.</p>
     *
     * @param id l'identifiant de l'ordonnance à supprimer
     * @return {@code true} si l'ordonnance a été supprimée, {@code false} si introuvable
     */
    public boolean supprimerOrdonnance(int id) {
        Ordonnance o = rechercherParId(id);
        if (o != null) {
            return ordonnances.remove(o);
        }
        return false;
    }

    /**
     * Retourne toutes les ordonnances associées à un patient donné.
     *
     * @param patient le patient recherché
     * @return une liste des ordonnances du patient
     */
    public List<Ordonnance> getOrdonnancesPatient(Patient patient) {
        return ordonnances.stream()
                .filter(o -> o.getPatient().equals(patient))
                .collect(Collectors.toList());
    }

    /**
     * Retourne toutes les ordonnances émises par un médecin spécifique.
     *
     * @param medecin le médecin prescripteur
     * @return une liste des ordonnances émises par ce médecin
     */
    public List<Ordonnance> getOrdonnancesMedecin(Medecin medecin) {
        return ordonnances.stream()
                .filter(o -> o.getMedecin().equals(medecin))
                .collect(Collectors.toList());
    }

    /**
     * Retourne la liste des ordonnances dont la date de validité n'est pas dépassée.
     *
     * @return la liste des ordonnances valides
     */
    public List<Ordonnance> getOrdonnancesValides() {
        return ordonnances.stream()
                .filter(Ordonnance::estValide)
                .collect(Collectors.toList());
    }

    /**
     * Retourne la liste complète de toutes les ordonnances enregistrées.
     *
     * @return une copie de la liste des ordonnances
     */
    public List<Ordonnance> listerToutesLesOrdonnances() {
        return new ArrayList<>(ordonnances);
    }

    /**
     * Retourne le nombre total d'ordonnances enregistrées.
     *
     * @return le nombre d'ordonnances
     */
    public int getNombreOrdonnances() {
        return ordonnances.size();
    }
}