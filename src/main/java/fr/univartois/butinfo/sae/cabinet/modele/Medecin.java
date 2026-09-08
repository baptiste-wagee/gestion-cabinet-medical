package fr.univartois.butinfo.sae.cabinet.modele;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import java.text.Normalizer;

/**
 * Représente un médecin du cabinet médical.
 * <p>
 * Cette classe hérite de {@link Personne} et ajoute des informations
 * spécifiques aux médecins telles que leur numéro d'ordre, leur spécialité
 * et leur tarif horaire de consultation.
 * </p>
 */
public class Medecin extends Personne {

  /**
   * Numéro d'identification du médecin auprès du Conseil de l'Ordre des Médecins.
   */
  private final String numeroOrdre;

  /**
   * Spécialité médicale exercée par le médecin.
   */
  private Specialite specialite;

  /**
   * Tarif horaire appliqué pour les consultations.
   */
  private double tarifHoraire;

  /**
   * Construit un médecin avec l'ensemble de ses informations.
   *
   * @param nom Le nom du médecin.
   * @param prenom Le prénom du médecin.
   * @param telephone Le numéro de téléphone du médecin.
   * @param email L'adresse électronique du médecin.
   * @param numeroOrdre Le numéro d'inscription à l'Ordre des Médecins.
   * @param specialite La spécialité médicale exercée.
   * @param tarifHoraire Le tarif horaire du médecin.
   */
  public Medecin(String nom, String prenom, String telephone, String email,
                 String numeroOrdre, Specialite specialite, double tarifHoraire) {
    super(nom, prenom, telephone, email);
    this.numeroOrdre = numeroOrdre;
    this.specialite = specialite;
    this.tarifHoraire = tarifHoraire;
  }

  /**
   * Construit un médecin avec un tarif horaire par défaut.
   *
   * @param nom Le nom du médecin.
   * @param prenom Le prénom du médecin.
   * @param telephone Le numéro de téléphone du médecin.
   * @param email L'adresse électronique du médecin.
   * @param numeroOrdre Le numéro d'inscription à l'Ordre des Médecins.
   * @param specialite La spécialité médicale exercée.
   */
  public Medecin(String nom, String prenom, String telephone, String email,
                 String numeroOrdre, Specialite specialite) {
    this(nom, prenom, telephone, email, numeroOrdre, specialite, 30.00);
  }

  /**
   * Construit un médecin à partir des informations minimales.
   *
   * @param nom Le nom du médecin.
   * @param prenom Le prénom du médecin.
   * @param specialite La spécialité du médecin sous forme de texte.
   * @param tarifHoraire Le tarif horaire du médecin.
   */
  public Medecin(String nom, String prenom, String specialite, double tarifHoraire) {
    this(nom, prenom, null, null, null,
            Specialite.valueOf(
                    Normalizer.normalize(specialite, Normalizer.Form.NFD)
                            .replaceAll("\\p{M}", "").toUpperCase()),
            tarifHoraire);
  }

  /**
   * Retourne le numéro d'ordre du médecin.
   *
   * @return Le numéro d'ordre.
   */
  public String getNumeroOrdre() {
    return numeroOrdre;
  }

  /**
   * Retourne la spécialité du médecin.
   *
   * @return La spécialité médicale.
   */
  public Specialite getSpecialite() {
    return specialite;
  }

  /**
   * Modifie la spécialité du médecin.
   *
   * @param specialite La nouvelle spécialité du médecin.
   */
  public void setSpecialite(Specialite specialite) {
    this.specialite = specialite;
  }

  /**
   * Retourne le tarif horaire du médecin.
   *
   * @return Le tarif horaire appliqué.
   */
  public double getTarifHoraire() {
    return tarifHoraire;
  }

  /**
   * Modifie le tarif horaire du médecin.
   *
   * @param tarifHoraire Le nouveau tarif horaire.
   */
  public void setTarifHoraire(double tarifHoraire) {
    this.tarifHoraire = tarifHoraire;
  }

  /**
   * Vérifie si un objet est égal à ce médecin.
   * Deux médecins sont considérés comme égaux s'ils possèdent
   * le même numéro d'ordre et les mêmes informations héritées
   * de la classe Personne.
   *
   * @param obj L'objet à comparer.
   *
   * @return true si les deux objets sont égaux, false sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (!super.equals(obj)) {
      return false;
    }
    Medecin autre = (Medecin) obj;
    return this.numeroOrdre.equals(autre.numeroOrdre);
  }

  /**
   * Calcule le code de hachage du médecin.
   *
   * @return Le code de hachage basé sur le numéro d'ordre.
   */
  @Override
  public int hashCode() {
    return numeroOrdre.hashCode();
  }

  /**
   * Retourne une représentation textuelle du médecin.
   *
   * @return Une chaîne contenant les informations du médecin,
   *         sa spécialité, son numéro d'ordre et son tarif horaire.
   */
  @Override
  public String toString() {
    return "Médecin  { " + super.toString() +
            " | Spécialité: " + specialite.getLibelle() +
            " | N° Ordre: " + numeroOrdre +
            " | tarif: " + tarifHoraire + " }";
  }
}