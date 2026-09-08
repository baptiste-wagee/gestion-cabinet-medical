package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.modele.RendezVous;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service de gestion des rendez-vous médicaux.
 * <p>Cette classe centralise la planification, la modification de statut et la
 * consultation des rendez-vous au sein du cabinet.</p>
 */
public class GestionRendezVous {

    /** Logger de la classe. */
    private static final Logger logger = LoggerFactory.getLogger(GestionRendezVous.class);

    /** Liste interne des rendez-vous enregistrés sous forme d'ObservableList. */
    private final ObservableList<RendezVous> rendezVous;

    /**
     * Construit un service de gestion des rendez-vous avec une liste vide.
     */
    public GestionRendezVous() {
        // Initialisation avec l'ObservableList de JavaFX
        this.rendezVous = FXCollections.observableArrayList();
    }


    /**
     * Supprime définitivement un rendez-vous de la liste par son identifiant.
     *
     * @param id l'identifiant du rendez-vous à supprimer
     * @return {@code true} si un rendez-vous a été supprimé, {@code false} sinon
     */
    public boolean supprimerRendezVous(int id) {
        return rendezVous.removeIf(r -> r.getId() == id);
    }

    // Champ statique, à placer avec les autres attributs
    private static GestionRendezVous instance;

    // Méthode à ajouter
    public static GestionRendezVous getInstance() {
        if (instance == null) {
            instance = new GestionRendezVous();
        }
        return instance;
    }
    /**
     * Enregistre un nouveau rendez-vous si le créneau du médecin est disponible.
     *
     * @param patient     le patient concerné
     * @param medecin     le médecin consulté
     * @param dateHeure   la date et l'heure du rendez-vous
     * @param dureeMinutes la durée du rendez-vous en minutes
     * @param motif       le motif de la consultation
     * @return le rendez-vous créé, ou {@code null} si le créneau est déjà pris
     */
    public RendezVous prendreRendezVous(Patient patient, Medecin medecin,
                                        LocalDateTime dateHeure, int dureeMinutes, String motif) {
        if (!estDisponible(medecin, dateHeure)) {
            logger.info("ERREUR : Le Dr {} n'est pas disponible à ce créneau.", medecin.getNomComplet());
            return null;
        }
        RendezVous rdv = new RendezVous(patient, medecin, dateHeure, dureeMinutes, motif);
        rendezVous.add(rdv);
        logger.info("Rendez-vous créé : {}", rdv);
        return rdv;
    }

    /**
     * Annule un rendez-vous existant par son identifiant.
     *
     * @param id l'identifiant du rendez-vous à annuler
     */
    public void annulerRendezVous(int id) {
        RendezVous rdv = rechercherParId(id);
        if (rdv == null) {
            logger.info("ERREUR : Rendez-vous ID={} introuvable.", id);
            return;
        }
        rdv.annuler();
        logger.info("Rendez-vous annulé : ID={}", id);
    }

    /**
     * Confirme un rendez-vous par son identifiant.
     *
     * @param id l'identifiant du rendez-vous à confirmer
     */
    public void confirmerRendezVous(int id) {
        RendezVous rdv = rechercherParId(id);
        if (rdv == null) {
            logger.info("ERREUR : Rendez-vous ID={} introuvable.", id);
            return;
        }
        rdv.confirmer();
        logger.info("Rendez-vous confirmed : ID={}", id);
    }

    /**
     * Marque un rendez-vous comme terminé.
     *
     * @param id l'identifiant du rendez-vous
     * @return {@code true} si le statut a été mis à jour, {@code false} sinon
     */
    public boolean terminerRendezVous(int id) {
        RendezVous rdv = rechercherParId(id);
        if (rdv == null) {
            logger.info("ERREUR : Rendez-vous ID={} introuvable.", id);
            return false;
        }
        rdv.terminer();
        logger.info("Rendez-vous terminé : ID={}", id);
        return true;
    }

    /**
     * Recherche un rendez-vous par son identifiant unique.
     *
     * @param id l'identifiant recherché
     * @return le rendez-vous correspondant, ou {@code null} si introuvable
     */
    public RendezVous rechercherParId(int id) {
        return rendezVous.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retourne tous les rendez-vous associés à un patient.
     *
     * @param patient le patient concerné
     * @return la liste observable des rendez-vous du patient
     */
    public ObservableList<RendezVous> getRendezVousPatient(Patient patient) {
        return rendezVous.stream()
                .filter(r -> r.getPatient().equals(patient))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Retourne tous les rendez-vous pris auprès d'un médecin.
     *
     * @param medecin le médecin concerné
     * @return la liste observable des rendez-vous du médecin
     */
    public ObservableList<RendezVous> getRendezVousMedecin(Medecin medecin) {
        return rendezVous.stream()
                .filter(r -> r.getMedecin().equals(medecin))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Retourne les rendez-vous filtrés par statut.
     *
     * @param statut le statut recherché
     * @return la liste observable des rendez-vous correspondants
     */
    public ObservableList<RendezVous> getRendezVousParStatut(StatutRendezVous statut) {
        return rendezVous.stream()
                .filter(r -> r.getStatut() == statut)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Vérifie la disponibilité d'un médecin à une date et heure précises.
     *
     * @param medecin   le médecin à vérifier
     * @param dateHeure la date et l'heure souhaitées
     * @return {@code true} si le médecin est libre, {@code false} sinon
     */
    public boolean estDisponible(Medecin medecin, LocalDateTime dateHeure) {
        return rendezVous.stream()
                .noneMatch(r -> r.getMedecin().equals(medecin)
                        && r.getDateHeure().equals(dateHeure)
                        && r.getStatut() != StatutRendezVous.ANNULE);
    }

    /**
     * Retourne la liste complète des rendez-vous.
     *
     * @return une copie sous forme d'ObservableList des rendez-vous
     */
    public ObservableList<RendezVous> getRendezVous() {
        return rendezVous; // la vraie liste, pas une copie
    }

    /**
     * Retourne le nombre total de rendez-vous enregistrés.
     *
     * @return le nombre de rendez-vous
     */
    public int getNombreRendezVous() {
        return rendezVous.size();
    }
}