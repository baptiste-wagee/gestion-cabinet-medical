package fr.univartois.butinfo.sae.cabinet.utils;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import java.util.List;
import java.util.logging.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module de recherche transversale permettant d'effectuer des recherches
 * sur les patients et les médecins du système.
 * <p>
 * Cette classe centralise les fonctionnalités de recherche avancée en
 * s'appuyant sur les services {@link GestionPatients} et {@link GestionMedecins}.
 * Elle gère également les erreurs via un logger SLF4J.
 */
public class Recherche {

    /**
     * Logger utilisé pour tracer les erreurs lors des recherches.
     */
    private static final Logger logger = LoggerFactory.getLogger(Recherche.class);

    /**
     * Service de gestion des patients utilisé pour effectuer les recherches.
     */
    private final GestionPatients gestionPatients;

    /**
     * Service de gestion des médecins utilisé pour effectuer les recherches.
     */
    private final GestionMedecins gestionMedecins;

    /**
     * Constructeur : initialise le module de recherche avec les services nécessaires.
     *
     * @param gestionPatients service de gestion des patients
     * @param gestionMedecins service de gestion des médecins
     */
    public Recherche(GestionPatients gestionPatients, GestionMedecins gestionMedecins) {
        this.gestionPatients = gestionPatients;
        this.gestionMedecins = gestionMedecins;
    }

    // -------------------------
    // Recherche patients
    // -------------------------

    /**
     * Recherche des patients par nom ou prénom correspondant au terme fourni.
     *
     * @param terme chaîne de caractères utilisée pour la recherche (nom ou prénom)
     * @return une liste de patients correspondant au critère de recherche,
     *         ou une liste vide en cas d'erreur
     */
    public List<Patient> rechercherPatientParNom(String terme) {
        try {
            return gestionPatients.rechercherParNom(terme);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de patients par nom \"" + terme + "\" : " + e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Recherche un patient à partir de son numéro de sécurité sociale.
     *
     * @param numeroSecu numéro de sécurité sociale du patient
     * @return le patient correspondant, ou {@code null} en cas d'erreur ou d'absence
     */
    public Patient rechercherPatientParSecu(String numeroSecu) {
        try {
            return gestionPatients.rechercherParNumeroSecu(numeroSecu);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du patient par numéro de sécu \"" + numeroSecu + "\" : " + e.getMessage(), e);
            return null;
        }
    }

    // -------------------------
    // Recherche médecins
    // -------------------------

    /**
     * Recherche des médecins par nom ou prénom correspondant au terme fourni.
     *
     * @param terme chaîne de caractères utilisée pour la recherche
     * @return une liste de médecins correspondant au critère de recherche,
     *         ou une liste vide en cas d'erreur
     */
    public List<Medecin> rechercherMedecinParNom(String terme) {
        try {
            return gestionMedecins.rechercherParNom(terme);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de médecins par nom \"{}\" : {}", terme, e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Recherche des médecins selon leur spécialité.
     *
     * @param specialite spécialité médicale recherchée
     * @return une liste de médecins correspondant à la spécialité,
     *         ou une liste vide en cas d'erreur
     */
    public List<Medecin> rechercherMedecinParSpecialite(Specialite specialite) {
        try {
            return gestionMedecins.rechercherParSpecialite(specialite);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de médecins par spécialité \"" + specialite + "\" : " + e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Recherche un médecin à partir de son numéro d'ordre.
     *
     * @param numeroOrdre numéro d'identification ordinal du médecin
     * @return le médecin correspondant, ou {@code null} en cas d'erreur ou d'absence
     */
    public Medecin rechercherMedecinParOrdre(String numeroOrdre) {
        try {
            return gestionMedecins.rechercherParNumeroOrdre(numeroOrdre);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du médecin par n° d'ordre \"" + numeroOrdre + "\" : " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retourne une représentation textuelle du module de recherche,
     * incluant le nombre de patients et de médecins disponibles.
     *
     * @return chaîne décrivant l'état du module de recherche
     */
    @Override
    public String toString() {
        return "Module Recherche { patients=" + gestionPatients.getNombrePatients() +
                ", médecins=" + gestionMedecins.getNombreMedecins() + " }";
    }
}