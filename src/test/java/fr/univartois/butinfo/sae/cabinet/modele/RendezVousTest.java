package fr.univartois.butinfo.sae.cabinet.modele;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Classe de test de la classe RendezVous.
 *
 * <p>Elle vérifie le calcul du prix d'une consultation (méthode prixConsultation()),
 * aussi bien avec de vrais objets qu'avec des mocks Mockito permettant d'isoler
 * RendezVous de ses dépendances Patient et Medecin.</p>
 *
 * @author Nathan Salomé
 */
@ExtendWith(MockitoExtension.class)
class RendezVousTest {

  /** Mock du médecin, utilisé pour isoler le calcul du prix de la consultation. */
  @Mock
  private Medecin medecinMock;

  /** Mock du patient, utilisé pour isoler le calcul du prix de la consultation. */
  @Mock
  private Patient patientMock;

  /**
   * Vérifie le calcul du prix d'une consultation dans un cas standard.
   *
   * <p>Avec un tarif horaire de 60, une durée de 30 minutes et un taux de
   * remboursement de 70 %, le reste à charge attendu est de 9,0.</p>
   */
  @Test
  void testPrixConsultationCasStandard() {
    Patient patient = new Patient("Dupont", "Jean", "01/01/1998", 0.7);
    Medecin medecin = new Medecin("Martin", "Pierre", "Généraliste", 60.0);
    RendezVous rdv = new RendezVous(patient, medecin, LocalDateTime.now(), 30, "Contrôle");

    double prixCalcule = rdv.prixConsultation();

    assertThat(prixCalcule).isCloseTo(9.0, within(0.001));
  }

  /**
   * Vérifie que le prix d'une consultation est nul lorsque le patient est
   * remboursé à 100 % (taux de remboursement de 1,0).
   */
  @Test
  void testPrixConsultationRemboursementTotal() {
    Patient patient = new Patient("Durand", "Marie", "12/05/1985", 1.0);
    Medecin medecin = new Medecin("Martin", "Pierre", "Généraliste", 60.0);
    RendezVous rdv = new RendezVous(patient, medecin, LocalDateTime.now(), 45, "Consultation");

    double prixCalcule = rdv.prixConsultation();

    assertThat(prixCalcule).isCloseTo(0.0, within(0.001));
  }

  /**
   * Vérifie le calcul du prix d'une consultation en isolant RendezVous de ses
   * dépendances grâce à des mocks Mockito.
   *
   * <p>Le médecin et le patient sont simulés : tarif horaire de 60, durée de
   * 30 minutes et taux de remboursement de 70 %, soit un reste à charge attendu
   * de 9,0.</p>
   */
  @Test
  void testPrixConsultationAvecMocks() {
    when(medecinMock.getTarifHoraire()).thenReturn(60.0);
    when(patientMock.getTauxRemboursement()).thenReturn(0.70);

    LocalDateTime dateRdv = LocalDateTime.of(2025, 6, 10, 14, 0);
    RendezVous rdv = new RendezVous(patientMock, medecinMock, dateRdv, 30, "Consultation test");

    double prix = rdv.prixConsultation();

    assertThat(prix).isCloseTo(9.0, within(0.001));
  }
}