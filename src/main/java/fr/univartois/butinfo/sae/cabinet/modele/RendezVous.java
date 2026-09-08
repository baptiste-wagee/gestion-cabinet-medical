package fr.univartois.butinfo.sae.cabinet.modele;

import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représente un rendez-vous entre un patient et un médecin.
 * <p>
 * Un rendez-vous est caractérisé par un patient, un médecin,
 * une date et heure, une durée, un motif ainsi qu'un statut
 * permettant de suivre son évolution.
 * </p>
 */
public class RendezVous {

  /**
   * Identifiant unique du rendez-vous.
   */
  private final int id;

  /**
   * Patient concerné par le rendez-vous.
   */
  private final Patient patient;

  /**
   * Médecin réalisant la consultation.
   */
  private final Medecin medecin;

  /**
   * Date et heure du rendez-vous.
   */
  private LocalDateTime dateHeure;

  /**
   * Durée du rendez-vous en minutes.
   */
  private int dureeMinutes;

  /**
   * Statut actuel du rendez-vous.
   */
  private StatutRendezVous statut;

  /**
   * Motif de la consultation.
   */
  private String motif;

  /**
   * Compteur utilisé pour générer automatiquement les identifiants.
   */
  private static int compteurId = 1;

  /**
   * Format d'affichage des dates et heures.
   */
  private static final DateTimeFormatter FORMATTER =
          DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

  /**
   * Construit un rendez-vous avec toutes ses informations.
   *
   * @param patient Le patient concerné.
   * @param medecin Le médecin concerné.
   * @param dateHeure La date et l'heure du rendez-vous.
   * @param dureeMinutes La durée du rendez-vous en minutes.
   * @param motif Le motif de la consultation.
   */
  public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure,
                    int dureeMinutes, String motif) {
    this.id = compteurId++;
    this.patient = patient;
    this.medecin = medecin;
    this.dateHeure = dateHeure;
    this.dureeMinutes = dureeMinutes;
    this.statut = StatutRendezVous.PLANIFIE;
    this.motif = motif;
  }

  /**
   * Construit un rendez-vous avec une durée par défaut de 30 minutes.
   *
   * @param patient Le patient concerné.
   * @param medecin Le médecin concerné.
   * @param dateHeure La date et l'heure du rendez-vous.
   * @param motif Le motif de la consultation.
   */
  public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure,
                    String motif) {
    this(patient, medecin, dateHeure, 30, motif);
  }

  // -------------------------
  // Actions métier
  // -------------------------

  /**
   * Confirme le rendez-vous.
   * <p>
   * Le statut passe de PLANIFIE à CONFIRME.
   * </p>
   */
  public void confirmer() {
    if (this.statut == StatutRendezVous.PLANIFIE) {
      this.statut = StatutRendezVous.CONFIRME;
    }
  }

  /**
   * Annule le rendez-vous.
   * <p>
   * Un rendez-vous terminé ne peut pas être annulé.
   * </p>
   */
  public void annuler() {
    if (this.statut != StatutRendezVous.TERMINE) {
      this.statut = StatutRendezVous.ANNULE;
    }
  }

  /**
   * Marque le rendez-vous comme terminé.
   */
  public void terminer() {
    this.statut = StatutRendezVous.TERMINE;
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne l'identifiant du rendez-vous.
   *
   * @return L'identifiant du rendez-vous.
   */
  public int getId() {
    return id;
  }

  /**
   * Retourne le patient associé au rendez-vous.
   *
   * @return Le patient concerné.
   */
  public Patient getPatient() {
    return patient;
  }

  /**
   * Retourne le médecin associé au rendez-vous.
   *
   * @return Le médecin concerné.
   */
  public Medecin getMedecin() {
    return medecin;
  }

  /**
   * Retourne la date et l'heure du rendez-vous.
   *
   * @return La date et l'heure du rendez-vous.
   */
  public LocalDateTime getDateHeure() {
    return dateHeure;
  }

  /**
   * Modifie la date et l'heure du rendez-vous.
   *
   * @param dateHeure La nouvelle date et heure.
   */
  public void setDateHeure(LocalDateTime dateHeure) {
    this.dateHeure = dateHeure;
  }

  /**
   * Retourne la durée du rendez-vous.
   *
   * @return La durée en minutes.
   */
  public int getDureeMinutes() {
    return dureeMinutes;
  }

  /**
   * Modifie la durée du rendez-vous.
   *
   * @param dureeMinutes La nouvelle durée en minutes.
   */
  public void setDureeMinutes(int dureeMinutes) {
    this.dureeMinutes = dureeMinutes;
  }

  /**
   * Retourne le statut du rendez-vous.
   *
   * @return Le statut actuel.
   */
  public StatutRendezVous getStatut() {
    return statut;
  }

  /**
   * Modifie le statut du rendez-vous.
   *
   * @param statut Le nouveau statut.
   */
  public void setStatut(StatutRendezVous statut) {
    this.statut = statut;
  }

  /**
   * Retourne le motif de la consultation.
   *
   * @return Le motif du rendez-vous.
   */
  public String getMotif() {
    return motif;
  }

  /**
   * Modifie le motif de la consultation.
   *
   * @param motif Le nouveau motif.
   */
  public void setMotif(String motif) {
    this.motif = motif;
  }

  /**
   * Calcule le coût de la consultation restant à la charge du patient.
   * <p>
   * Le calcul tient compte du tarif horaire du médecin,
   * de la durée de la consultation et du taux de remboursement
   * du patient.
   * </p>
   *
   * @return Le coût de la consultation.
   */
  public double prixConsultation() {
    double tarifParMinute = this.medecin.getTarifHoraire() / 60.0;
    double prixBrut = tarifParMinute * this.dureeMinutes;
    double resteACharge = 1.0 - this.patient.getTauxRemboursement();
    return prixBrut * resteACharge;
  }

  /**
   * Calcule le code de hachage du rendez-vous.
   *
   * @return Le code de hachage du rendez-vous.
   */
  @Override
  public int hashCode() {
    return patient.hashCode() * 31 + medecin.hashCode() * 17 + dateHeure.hashCode();
  }

  /**
   * Vérifie si un objet est égal à ce rendez-vous.
   * Deux rendez-vous sont considérés comme égaux s'ils concernent
   * le même patient, le même médecin et la même date/heure.
   *
   * @param obj L'objet à comparer.
   *
   * @return true si les rendez-vous sont égaux, false sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    RendezVous autre = (RendezVous) obj;
    return this.patient.equals(autre.patient) &&
            this.medecin.equals(autre.medecin) &&
            this.dateHeure.equals(autre.dateHeure);
  }

  /**
   * Retourne une représentation textuelle du rendez-vous.
   *
   * @return Une chaîne contenant toutes les informations du rendez-vous.
   */
  @Override
  public String toString() {
    return "RDV      { [ID=" + id + "] " +
            patient.getNomComplet() + " avec Dr " + medecin.getNomComplet() +
            " | " + dateHeure.format(FORMATTER) +
            " (" + dureeMinutes + " min)" +
            " | Motif: " + motif +
            " | Statut: " + statut.getLibelle() +
            " | Coût : " + prixConsultation() + " }";
  }
}