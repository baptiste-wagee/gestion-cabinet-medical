package fr.univartois.butinfo.sae.cabinet.modele;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Classe de test de la classe Ordonnance.
 *
 * <p>Elle vérifie le calcul du prix total (getPrixTotal), la validité d'une
 * ordonnance (estValide), la gestion des prescriptions (ajout, suppression,
 * copie défensive) et l'égalité entre ordonnances. Les dépendances Medecin,
 * Patient, Medicament et LignePrescription sont simulées avec des mocks Mockito.</p>
 *
 * @author Baptiste Wagée
 */
@ExtendWith(MockitoExtension.class)
class OrdonnanceTest {

  /** Mock du médecin associé à l'ordonnance. */
  @Mock private Medecin medecin;

  /** Mock du patient associé à l'ordonnance. */
  @Mock private Patient patient;

  /** Premier mock de médicament utilisé dans les prescriptions. */
  @Mock private Medicament medicament1;

  /** Second mock de médicament utilisé dans les prescriptions. */
  @Mock private Medicament medicament2;

  /** Première ligne de prescription simulée. */
  @Mock private LignePrescription ligne1;

  /** Seconde ligne de prescription simulée. */
  @Mock private LignePrescription ligne2;

  /** Ordonnance testée, réinitialisée avant chaque test. */
  private Ordonnance ordonnance;

  /**
   * Initialise une ordonnance vierge avant chaque test, à partir des mocks
   * du médecin et du patient.
   */
  @BeforeEach
  void setUp() {
    ordonnance = new Ordonnance(medecin, patient);
  }

  // --- getPrixTotal ---

  /**
   * Vérifie que le prix total est nul lorsque l'ordonnance ne contient aucune
   * prescription.
   */
  @Test
  void getPrixTotal_sansPrescription_retourneZero() {
    when(patient.getTauxRemboursement()).thenReturn(0.75);
    assertThat(ordonnance.getPrixTotal()).isEqualTo(0.0);
  }

  /**
   * Vérifie le calcul du prix total avec une seule prescription.
   *
   * <p>Avec un prix de 10, une quantité de 2 et un taux de remboursement de
   * 75 % : 10 × 2 × (1 − 0,75) = 5,0.</p>
   */
  @Test
  void getPrixTotal_avecUnePrescription() {
    when(ligne1.getMedicament()).thenReturn(medicament1);
    when(ligne1.getQuantite()).thenReturn(2);
    when(medicament1.getPrix()).thenReturn(10.0);
    when(patient.getTauxRemboursement()).thenReturn(0.75);

    ordonnance.ajouterPrescription(ligne1);

    // 10.0 * 2 * (1 - 0.75) = 5.0
    assertThat(ordonnance.getPrixTotal()).isEqualTo(5.0);
  }

  /**
   * Vérifie le calcul du prix total avec plusieurs prescriptions.
   *
   * <p>(10 × 1 + 20 × 2) × (1 − 0,5) = 50 × 0,5 = 25,0.</p>
   */
  @Test
  void getPrixTotal_avecPlusieursPrescriptions() {
    when(ligne1.getMedicament()).thenReturn(medicament1);
    when(ligne1.getQuantite()).thenReturn(1);
    when(medicament1.getPrix()).thenReturn(10.0);

    when(ligne2.getMedicament()).thenReturn(medicament2);
    when(ligne2.getQuantite()).thenReturn(2);
    when(medicament2.getPrix()).thenReturn(20.0);

    when(patient.getTauxRemboursement()).thenReturn(0.5);

    ordonnance.ajouterPrescription(ligne1);
    ordonnance.ajouterPrescription(ligne2);

    // (10*1 + 20*2) * (1 - 0.5) = 50 * 0.5 = 25.0
    assertThat(ordonnance.getPrixTotal()).isEqualTo(25.0);
  }

  // --- estValide ---

  /**
   * Vérifie qu'une ordonnance est valide lorsque sa date de validité est dans
   * le futur.
   */
  @Test
  void estValide_dateValiditeFuture_retourneTrue() {
    ordonnance.setDateValidite(LocalDate.now().plusDays(1));
    assertThat(ordonnance.estValide()).isTrue();
  }

  /**
   * Vérifie qu'une ordonnance est invalide lorsque sa date de validité est
   * dépassée.
   */
  @Test
  void estValide_dateValiditePassee_retourneFalse() {
    ordonnance.setDateValidite(LocalDate.now().minusDays(1));
    assertThat(ordonnance.estValide()).isFalse();
  }

  /**
   * Vérifie qu'une ordonnance dont la date de validité est aujourd'hui est
   * considérée comme valide.
   */
  @Test
  void estValide_dateValiditeAujourdhui_retourneTrue() {
    ordonnance.setDateValidite(LocalDate.now());
    assertThat(ordonnance.estValide()).isTrue();
  }

  // --- ajouterPrescription / getPrescriptions ---

  /**
   * Vérifie que l'ajout d'une prescription augmente bien la liste des
   * prescriptions de l'ordonnance.
   */
  @Test
  void ajouterPrescription_augmenteLaListe() {
    ordonnance.ajouterPrescription(ligne1);
    assertThat(ordonnance.getPrescriptions()).hasSize(1).contains(ligne1);
  }

  /**
   * Vérifie que getPrescriptions renvoie une copie défensive : modifier la
   * liste retournée ne doit pas altérer le contenu réel de l'ordonnance.
   */
  @Test
  void getPrescriptions_retourneCopieDeFensive() {
    ordonnance.ajouterPrescription(ligne1);
    ordonnance.getPrescriptions().clear();
    assertThat(ordonnance.getPrescriptions()).hasSize(1);
  }

  // --- supprimerPrescription ---

  /**
   * Vérifie que la suppression d'une prescription existante renvoie true et
   * vide la liste des prescriptions.
   */
  @Test
  void supprimerPrescription_existante_retourneTrue() {
    when(ligne1.getMedicament()).thenReturn(medicament1);
    ordonnance.ajouterPrescription(ligne1);

    assertThat(ordonnance.supprimerPrescription(medicament1)).isTrue();
    assertThat(ordonnance.getPrescriptions()).isEmpty();
  }

  /**
   * Vérifie que la suppression d'un médicament absent de l'ordonnance renvoie
   * false.
   */
  @Test
  void supprimerPrescription_inexistante_retourneFalse() {
    assertThat(ordonnance.supprimerPrescription(medicament1)).isFalse();
  }

  // --- equals ---

  /**
   * Vérifie qu'une ordonnance est égale à elle-même (réflexivité de equals).
   */
  @Test
  void memeOrdonnance_estEgaleAElleMeme() {
    assertThat(ordonnance).isEqualTo(ordonnance);
  }

  /**
   * Vérifie que deux ordonnances distinctes ne sont pas considérées comme
   * égales.
   */
  @Test
  void deuxOrdonnancesDistinctes_sontInegales() {
    Ordonnance autre = new Ordonnance(medecin, patient);
    assertThat(ordonnance).isNotEqualTo(autre);
  }
}