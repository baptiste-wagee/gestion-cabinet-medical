package fr.univartois.butinfo.sae.cabinet.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une ordonnance médicale établie par un médecin pour un patient.
 * <p>
 * Une ordonnance contient une liste de prescriptions, une date d'émission,
 * une date de validité ainsi que d'éventuelles remarques complémentaires.
 * </p>
 */
public class Ordonnance {

  /**
   * Identifiant unique de l'ordonnance.
   */
  private final int id;

  /**
   * Médecin ayant émis l'ordonnance.
   */
  private final Medecin medecin;

  /**
   * Patient concerné par l'ordonnance.
   */
  private final Patient patient;

  /**
   * Date d'émission de l'ordonnance.
   */
  private final LocalDate dateEmission;

  /**
   * Date limite de validité de l'ordonnance.
   */
  private LocalDate dateValidite;

  /**
   * Liste des prescriptions contenues dans l'ordonnance.
   */
  private final ObservableList<LignePrescription> prescriptions;

  /**
   * Remarques ou consignes complémentaires associées à l'ordonnance.
   */
  private String remarques;

  /**
   * Compteur utilisé pour générer automatiquement les identifiants.
   */
  private static int compteurId = 1;

  /**
   * Construit une ordonnance avec toutes ses informations.
   *
   * @param medecin Le médecin ayant rédigé l'ordonnance.
   * @param patient Le patient concerné.
   * @param dateValidite La date de validité de l'ordonnance.
   * @param remarques Les remarques éventuelles.
   */
  public Ordonnance(Medecin medecin, Patient patient, LocalDate dateValidite, String remarques) {
    this.id = compteurId++;
    this.medecin = medecin;
    this.patient = patient;
    this.dateEmission = LocalDate.now();
    this.dateValidite = dateValidite;
    this.prescriptions = FXCollections.observableArrayList();
    this.remarques = remarques;
  }

  /**
   * Construit une ordonnance avec une durée de validité de trois mois
   * et sans remarque particulière.
   *
   * @param medecin Le médecin ayant rédigé l'ordonnance.
   * @param patient Le patient concerné.
   */
  public Ordonnance(Medecin medecin, Patient patient) {
    this(medecin, patient, LocalDate.now().plusMonths(3), "");
  }

  // -------------------------
  // Gestion des prescriptions
  // -------------------------

  /**
   * Ajoute une ligne de prescription à l'ordonnance.
   *
   * @param ligne La ligne de prescription à ajouter.
   */
  public void ajouterPrescription(LignePrescription ligne) {
    prescriptions.add(ligne);
  }

  /**
   * Supprime une prescription associée à un médicament donné.
   *
   * @param medicament Le médicament à rechercher.
   *
   * @return true si une prescription a été supprimée, false sinon.
   */
  public boolean supprimerPrescription(Medicament medicament) {
    return prescriptions.removeIf(l -> l.getMedicament().equals(medicament));
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne l'identifiant de l'ordonnance.
   *
   * @return L'identifiant de l'ordonnance.
   */
  public int getId() {
    return id;
  }

  /**
   * Retourne le médecin ayant établi l'ordonnance.
   *
   * @return Le médecin prescripteur.
   */
  public Medecin getMedecin() {
    return medecin;
  }

  /**
   * Retourne le patient concerné.
   *
   * @return Le patient associé à l'ordonnance.
   */
  public Patient getPatient() {
    return patient;
  }

  /**
   * Retourne la date d'émission de l'ordonnance.
   *
   * @return La date d'émission.
   */
  public LocalDate getDateEmission() {
    return dateEmission;
  }

  /**
   * Retourne la date de validité de l'ordonnance.
   *
   * @return La date de validité.
   */
  public LocalDate getDateValidite() {
    return dateValidite;
  }

  /**
   * Modifie la date de validité de l'ordonnance.
   *
   * @param dateValidite La nouvelle date de validité.
   */
  public void setDateValidite(LocalDate dateValidite) {
    this.dateValidite = dateValidite;
  }

  /**
   * Retourne une copie de la liste des prescriptions.
   *
   * @return Une copie défensive des prescriptions.
   */
  public ObservableList<LignePrescription> getPrescriptions() {
    return this.prescriptions;
  }

  /**
   * Retourne les remarques associées à l'ordonnance.
   *
   * @return Les remarques éventuelles.
   */
  public String getRemarques() {
    return remarques;
  }

  /**
   * Modifie les remarques de l'ordonnance.
   *
   * @param remarques Les nouvelles remarques.
   */
  public void setRemarques(String remarques) {
    this.remarques = remarques;
  }

  /**
   * Vérifie si l'ordonnance est encore valide à la date actuelle.
   *
   * @return true si l'ordonnance est valide, false sinon.
   */
  public boolean estValide() {
    return !LocalDate.now().isAfter(dateValidite);
  }

  /**
   * Vérifie si une autre ordonnance est égale à celle-ci.
   * Deux ordonnances sont considérées comme égales lorsqu'elles
   * possèdent le même identifiant.
   *
   * @param obj L'objet à comparer.
   *
   * @return true si les deux ordonnances sont égales, false sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Ordonnance o = (Ordonnance) obj;
    return id == o.id;
  }

  /**
   * Calcule le code de hachage de l'ordonnance.
   *
   * @return Le code de hachage basé sur l'identifiant.
   */
  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }

  /**
   * Calcule le prix total restant à la charge du patient.
   * <p>
   * Le montant est obtenu en additionnant le coût de chaque médicament
   * multiplié par sa quantité, puis en appliquant le taux de remboursement
   * du patient.
   * </p>
   *
   * @return Le prix total de l'ordonnance.
   */
  public double getPrixTotal() {
    double total = 0.0;
    for (LignePrescription lp : prescriptions) {
      total += lp.getMedicament().getPrix() * lp.getQuantite();
    }
    return total * (1 - patient.getTauxRemboursement());
  }

  /**
   * Retourne une représentation textuelle complète de l'ordonnance.
   *
   * @return Une chaîne contenant toutes les informations de l'ordonnance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("=== ORDONNANCE N°").append(id).append(" ===\n");
    sb.append("  Médecin  : Dr ").append(medecin.getNomComplet())
            .append(" (").append(medecin.getSpecialite().getLibelle()).append(")\n");
    sb.append("  Patient  : ").append(patient.getNomComplet()).append("\n");
    sb.append("  Émise le : ").append(dateEmission).append("\n");
    sb.append("  Valide jusqu'au : ").append(dateValidite).append("\n");
    sb.append("  Médicaments prescrits :\n");
    for (LignePrescription lp : prescriptions) {
      sb.append(lp.toString()).append("\n");
    }
    sb.append("  Prix total estimé : ").append(String.format("%.2f €", getPrixTotal())).append("\n");
    if (!remarques.isEmpty()) {
      sb.append("  Remarques : ").append(remarques).append("\n");
    }
    return sb.toString();
  }
}