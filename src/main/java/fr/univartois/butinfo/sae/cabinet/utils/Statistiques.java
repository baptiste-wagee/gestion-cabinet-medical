package fr.univartois.butinfo.sae.cabinet.utils;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import fr.univartois.butinfo.sae.cabinet.service.GestionRendezVous;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module de statistiques du cabinet médical.
 * <p>
 * Cette classe agrège des données issues des différents services du système
 * afin de produire un tableau de bord complet du cabinet médical.
 * Elle permet notamment d'obtenir des indicateurs sur les patients,
 * les médecins, les rendez-vous, les ordonnances et les médicaments.
 */
public class Statistiques {

    /**
     * Logger utilisé pour tracer les éventuelles erreurs ou informations.
     */
    private static final Logger logger = LoggerFactory.getLogger(Statistiques.class);

    /**
     * Service de gestion des patients.
     */
    private final GestionPatients gestionPatients;

    /**
     * Service de gestion des médecins.
     */
    private final GestionMedecins gestionMedecins;

    /**
     * Service de gestion des médicaments.
     */
    private final GestionMedicaments gestionMedicaments;

    /**
     * Service de gestion des rendez-vous.
     */
    private final GestionRendezVous gestionRendezVous;

    /**
     * Service de gestion des ordonnances.
     */
    private final GestionOrdonnances gestionOrdonnances;

    /**
     * Constructeur : injecte l'ensemble des services nécessaires
     * à la génération des statistiques du cabinet médical.
     *
     * @param gestionPatients service de gestion des patients
     * @param gestionMedecins service de gestion des médecins
     * @param gestionMedicaments service de gestion des médicaments
     * @param gestionRendezVous service de gestion des rendez-vous
     * @param gestionOrdonnances service de gestion des ordonnances
     */
    public Statistiques(GestionPatients gestionPatients,
                        GestionMedecins gestionMedecins,
                        GestionMedicaments gestionMedicaments,
                        GestionRendezVous gestionRendezVous,
                        GestionOrdonnances gestionOrdonnances) {
        this.gestionPatients = gestionPatients;
        this.gestionMedecins = gestionMedecins;
        this.gestionMedicaments = gestionMedicaments;
        this.gestionRendezVous = gestionRendezVous;
        this.gestionOrdonnances = gestionOrdonnances;
    }

    /**
     * Génère un tableau de bord global des statistiques du cabinet médical.
     * <p>
     * Ce tableau inclut :
     * <ul>
     *     <li>Le nombre de patients, médecins, médicaments, rendez-vous et ordonnances</li>
     *     <li>La répartition des rendez-vous par statut</li>
     *     <li>La répartition des médecins par spécialité</li>
     *     <li>Le nombre de rendez-vous et d’ordonnances par médecin</li>
     *     <li>Le nombre d’ordonnances valides</li>
     * </ul>
     *
     * @return une chaîne formatée représentant les statistiques globales
     */
    public String getStatistiques() {
        StringBuilder resultat = new StringBuilder();

        resultat.append("\n╔══════════════════════════════════════════╗\n");
        resultat.append("║        TABLEAU DE BORD - CABINET MÉDICAL  ║\n");
        resultat.append("╚══════════════════════════════════════════╝\n");

        resultat.append("\n📋 CHIFFRES GLOBAUX :\n");
        resultat.append("  • Patients enregistrés   : " + gestionPatients.getNombrePatients() + "\n");
        resultat.append("  • Médecins enregistrés   : " + gestionMedecins.getNombreMedecins() + "\n");
        resultat.append("  • Médicaments référencés : " + gestionMedicaments.getNombreMedicaments() + "\n");
        resultat.append("  • Rendez-vous total      : " + gestionRendezVous.getNombreRendezVous() + "\n");
        resultat.append("  • Ordonnances émises     : " + gestionOrdonnances.getNombreOrdonnances() + "\n");

        resultat.append("\n📅 RENDEZ-VOUS PAR STATUT :\n");
        for (StatutRendezVous statut : StatutRendezVous.values()) {
            int nb = gestionRendezVous.getRendezVousParStatut(statut).size();
            resultat.append("  • " + statut.getLibelle() + " : " + nb + "\n");
        }

        resultat.append("\n🩺 MÉDECINS PAR SPÉCIALITÉ :\n");
        for (Specialite sp : Specialite.values()) {
            int nb = gestionMedecins.rechercherParSpecialite(sp).size();
            if (nb > 0) {
                resultat.append("  • " + sp.getLibelle() + " : " + nb + "\n");
            }
        }

        resultat.append("\n🏆 MÉDECINS PAR NOMBRE DE RENDEZ-VOUS :\n");
        for (Medecin m : gestionMedecins.listerTousLesMedecins()) {
            int nbRdv = gestionRendezVous.getRendezVousMedecin(m).size();
            int nbOrdo = gestionOrdonnances.getOrdonnancesMedecin(m).size();
            resultat.append("  • Dr " + m.getNomComplet() +
                    " (" + m.getSpecialite().getLibelle() + ")" +
                    " → " + nbRdv + " RDV, " + nbOrdo + " ordonnances\n");
        }

        resultat.append("\n💊 ORDONNANCES VALIDES (en cours) : " +
                gestionOrdonnances.getOrdonnancesValides().size() + "\n");

        resultat.append("\n══════════════════════════════════════════════\n");

        return resultat.toString();
    }

    /**
     * Retourne le nombre de rendez-vous associés à un statut donné.
     *
     * @param statut statut des rendez-vous
     * @return nombre de rendez-vous correspondant
     */
    public int getNombreRendezVousParStatut(StatutRendezVous statut) {
        return gestionRendezVous.getRendezVousParStatut(statut).size();
    }

    /**
     * Retourne le médecin ayant le plus grand nombre de rendez-vous.
     *
     * @return le médecin le plus actif, ou {@code null} s’il n’existe aucun médecin
     */
    public Medecin getMedecinLePlusActif() {
        Medecin medecinMax = null;
        int max = 0;

        for (Medecin m : gestionMedecins.listerTousLesMedecins()) {
            int nb = gestionRendezVous.getRendezVousMedecin(m).size();
            if (nb > max) {
                max = nb;
                medecinMax = m;
            }
        }
        return medecinMax;
    }

    /**
     * Représentation textuelle des statistiques globales du cabinet.
     *
     * @return résumé des indicateurs principaux
     */
    @Override
    public String toString() {
        return "Statistiques { " +
                "patients=" + gestionPatients.getNombrePatients() +
                ", médecins=" + gestionMedecins.getNombreMedecins() +
                ", médicaments=" + gestionMedicaments.getNombreMedicaments() +
                ", RDV=" + gestionRendezVous.getNombreRendezVous() +
                ", ordonnances=" + gestionOrdonnances.getNombreOrdonnances() + " }";
    }
}