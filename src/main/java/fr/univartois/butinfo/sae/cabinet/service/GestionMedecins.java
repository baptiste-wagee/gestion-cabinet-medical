package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service de gestion des médecins du cabinet médical.
 *
 * <p>Fournit les opérations CRUD sur les médecins ainsi que des fonctionnalités
 * de recherche par identifiant, numéro d'ordre, nom ou spécialité.</p>
 */
public class GestionMedecins {

    /** Logger de la classe. */
    private static final Logger logger = LoggerFactory.getLogger(GestionMedecins.class);

    /** Instance unique du service (singleton), comme GestionPatients. */
    private static GestionMedecins instance;

    /** Liste interne observable des médecins enregistrés dans le cabinet. */
    private final ObservableList<Medecin> medecins;

    /**
     * Construit un service de gestion des médecins avec une liste vide.
     */
    public GestionMedecins() {
        this.medecins = FXCollections.observableArrayList();
    }

    /**
     * Retourne l'instance unique du service.
     *
     * @return l'instance partagée de GestionMedecins
     */
    public static GestionMedecins getInstance() {
        if (instance == null) {
            instance = new GestionMedecins();
        }
        return instance;
    }

    /**
     * Retourne la liste observable interne (vivante) des médecins.
     *
     * <p>À ne pas confondre avec {@link #listerTousLesMedecins()} qui en
     * renvoie une copie. C'est cette liste-ci qu'il faut lier au ListView
     * pour qu'il se rafraîchisse automatiquement après ajout/suppression.</p>
     *
     * @return la liste observable des médecins
     */
    public ObservableList<Medecin> getMedecins() {
        return medecins;
    }

    /**
     * Enregistre un nouveau médecin dans le cabinet.
     *
     * <p>L'ajout est refusé si un médecin avec le même numéro d'ordre existe déjà.</p>
     *
     * @param medecin le médecin à ajouter
     */
    public void ajouterMedecin(Medecin medecin) {
        if (rechercherParNumeroOrdre(medecin.getNumeroOrdre()) == null) {
            medecins.add(medecin);
            logger.info("Médecin ajouté : {}", medecin.getNom());
        }
    }

    /**
     * Recherche un médecin par son identifiant unique.
     *
     * @param id l'identifiant du médecin
     * @return le médecin correspondant, ou {@code null} s'il n'existe pas
     */
    public Medecin rechercherParId(int id) {
        return medecins.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Recherche un médecin par son numéro d'ordre (insensible à la casse).
     *
     * @param numeroOrdre le numéro d'ordre du médecin
     * @return le médecin correspondant, ou {@code null} s'il n'existe pas
     */
    public Medecin rechercherParNumeroOrdre(String numeroOrdre) {
        return medecins.stream()
                .filter(m -> m.getNumeroOrdre().equalsIgnoreCase(numeroOrdre))
                .findFirst()
                .orElse(null);
    }

    /**
     * Recherche des médecins dont le nom ou le prénom contient le terme donné
     * (insensible à la casse).
     *
     * @param terme le terme de recherche
     * @return la liste des médecins correspondants, vide si aucun résultat
     */
    public List<Medecin> rechercherParNom(String terme) {
        String lowerTerme = terme.toLowerCase();
        return medecins.stream()
                .filter(m -> m.getNom().toLowerCase().contains(lowerTerme)
                        || m.getPrenom().toLowerCase().contains(lowerTerme))
                .collect(Collectors.toList());
    }

    /**
     * Retourne tous les médecins d'une spécialité donnée.
     *
     * @param specialite la spécialité recherchée
     * @return la liste des médecins de cette spécialité, vide si aucun résultat
     */
    public List<Medecin> rechercherParSpecialite(Specialite specialite) {
        return medecins.stream()
                .filter(m -> m.getSpecialite() == specialite)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour les informations d'un médecin existant.
     *
     * <p>Sans effet si aucun médecin ne correspond à l'identifiant fourni.</p>
     *
     * @param id l'identifiant du médecin à mettre à jour
     * @param nouveauNom le nouveau nom
     * @param nouveauPrenom le nouveau prénom
     * @param nouveauTarif le nouveau tarif de consultation
     * @param nouveauTel le nouveau numéro de téléphone
     * @param nouveauEmail la nouvelle adresse e-mail
     * @param nouvelleSpecialite la nouvelle spécialité
     */
    public void mettreAJourMedecin(int id, String nouveauNom, String nouveauPrenom,
                                   double nouveauTarif, String nouveauTel,
                                   String nouveauEmail, Specialite nouvelleSpecialite) {
        Medecin m = rechercherParId(id);
        if (m != null) {
            m.setNom(nouveauNom);
            m.setPrenom(nouveauPrenom);
            m.setTelephone(nouveauTel);
            m.setEmail(nouveauEmail);
            m.setSpecialite(nouvelleSpecialite);
            logger.info("Médecin mis à jour : ID {}", id);
        }
    }

    /**
     * Supprime un médecin par son identifiant.
     *
     * @param id l'identifiant du médecin à supprimer
     * @return {@code true} si le médecin a été supprimé, {@code false} s'il est introuvable
     */
    public boolean supprimerMedecin(int id) {
        Medecin m = rechercherParId(id);
        if (m != null) {
            return medecins.remove(m);
        }
        return false;
    }

    /**
     * Retourne une copie de la liste complète des médecins enregistrés.
     *
     * @return la liste de tous les médecins
     */
    public List<Medecin> listerTousLesMedecins() {
        return new ArrayList<>(medecins);
    }

    /**
     * Retourne le nombre total de médecins enregistrés dans le cabinet.
     *
     * @return le nombre de médecins
     */
    public int getNombreMedecins() {
        return medecins.size();
    }
}