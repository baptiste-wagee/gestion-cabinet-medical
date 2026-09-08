package fr.univartois.butinfo.sae.cabinet.modele;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Représente un patient du cabinet médical.
 * <p>
 * Cette classe hérite de {@link Personne} et contient les informations
 * spécifiques à un patient telles que son adresse, sa date de naissance,
 * son numéro de sécurité sociale et son taux de remboursement.
 * </p>
 */
public class Patient extends Personne {

    /**
     * Adresse postale du patient.
     */
    private String adresse;

    /**
     * Date de naissance du patient.
     */
    private LocalDate dateNaissance;

    /**
     * Numéro de sécurité sociale du patient.
     */
    private final String numeroSecu;

    /**
     * Taux de remboursement du patient par la sécurité sociale.
     */
    private double tauxRemboursement;

    /**
     * Construit un patient avec l'ensemble de ses informations.
     *
     * @param nom Le nom du patient.
     * @param prenom Le prénom du patient.
     * @param telephone Le numéro de téléphone du patient.
     * @param email L'adresse électronique du patient.
     * @param adresse L'adresse du patient.
     * @param dateNaissance La date de naissance du patient.
     * @param numeroSecu Le numéro de sécurité sociale du patient.
     * @param tauxRemboursement Le taux de remboursement du patient.
     */
    public Patient(String nom, String prenom, String telephone, String email,
                   String adresse, LocalDate dateNaissance, String numeroSecu,
                   double tauxRemboursement) {
        super(nom, prenom, telephone, email);
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.numeroSecu = numeroSecu;
        this.tauxRemboursement = tauxRemboursement;
    }

    /**
     * Construit un patient avec un taux de remboursement par défaut.
     *
     * @param nom Le nom du patient.
     * @param prenom Le prénom du patient.
     * @param telephone Le numéro de téléphone du patient.
     * @param email L'adresse électronique du patient.
     * @param adresse L'adresse du patient.
     * @param dateNaissance La date de naissance du patient.
     * @param numeroSecu Le numéro de sécurité sociale du patient.
     */
    public Patient(String nom, String prenom, String telephone, String email,
                   String adresse, LocalDate dateNaissance, String numeroSecu) {
        this(nom, prenom, telephone, email, adresse, dateNaissance, numeroSecu, .75);
    }

    /**
     * Construit un patient à partir des informations minimales.
     *
     * @param nom Le nom du patient.
     * @param prenom Le prénom du patient.
     * @param dateNaissance La date de naissance au format dd/MM/yyyy.
     * @param tauxRemboursement Le taux de remboursement du patient.
     */
    public Patient(String nom, String prenom, String dateNaissance,
                   double tauxRemboursement) {
        this(nom, prenom, null, null, null,
                LocalDate.parse(dateNaissance,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                null, tauxRemboursement);
    }

    /**
     * Retourne l'adresse du patient.
     *
     * @return L'adresse du patient.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Modifie l'adresse du patient.
     *
     * @param adresse La nouvelle adresse.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Retourne la date de naissance du patient.
     *
     * @return La date de naissance.
     */
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Modifie la date de naissance du patient.
     *
     * @param dateNaissance La nouvelle date de naissance.
     */
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Retourne le numéro de sécurité sociale du patient.
     *
     * @return Le numéro de sécurité sociale.
     */
    public String getNumeroSecu() {
        return numeroSecu;
    }

    /**
     * Retourne le taux de remboursement du patient.
     *
     * @return Le taux de remboursement.
     */
    public double getTauxRemboursement() {
        return tauxRemboursement;
    }

    /**
     * Modifie le taux de remboursement du patient.
     *
     * @param tauxRemboursement Le nouveau taux de remboursement.
     */
    public void setTauxRemboursement(double tauxRemboursement) {
        this.tauxRemboursement = tauxRemboursement;
    }

    /**
     * Calcule l'âge actuel du patient.
     *
     * @return L'âge du patient en années.
     */
    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    /**
     * Vérifie si un objet est égal à ce patient.
     * Deux patients sont considérés comme égaux s'ils possèdent
     * le même numéro de sécurité sociale et les mêmes informations
     * héritées de la classe Personne.
     *
     * @param obj L'objet à comparer.
     *
     * @return true si les deux patients sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        Patient autre = (Patient) obj;
        return this.numeroSecu.equals(autre.numeroSecu);
    }

    /**
     * Calcule le code de hachage du patient.
     *
     * @return Le code de hachage basé sur les informations du patient.
     */
    @Override
    public int hashCode() {
        return super.hashCode() * 31 + numeroSecu.hashCode();
    }

    /**
     * Retourne une représentation textuelle du patient.
     *
     * @return Une chaîne contenant les informations du patient.
     */
    @Override
    public String toString() {
        return "Patient  { " + super.toString() +
                " | Naissance: " + dateNaissance +
                " (" + getAge() + " ans)" +
                " | Adresse: " + adresse +
                " | Sécu: " + numeroSecu +
                " | Taux: " + tauxRemboursement + " }";
    }
}