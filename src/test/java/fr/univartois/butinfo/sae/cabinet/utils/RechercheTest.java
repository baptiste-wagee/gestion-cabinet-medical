package fr.univartois.butinfo.sae.cabinet.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Classe de test de la classe Recherche.
 *
 * <p>Elle vérifie la recherche de patients et de médecins selon différents
 * critères (nom, numéro de sécurité sociale, spécialité, numéro d'ordre), la
 * représentation textuelle (toString) ainsi que le comportement en cas
 * d'erreur. Les services GestionPatients et GestionMedecins sont simulés avec
 * des mocks Mockito et injectés dans la classe Recherche.</p>
 *
 * @author Matty Forumestraux
 */
@ExtendWith(MockitoExtension.class)
class RechercheTest {

	/** Mock du service de gestion des patients. */
	@Mock
	private GestionPatients gestionPatients;

	/** Mock du service de gestion des médecins. */
	@Mock
	private GestionMedecins gestionMedecins;

	/** Instance de Recherche testée, dans laquelle les mocks sont injectés. */
	@InjectMocks
	private Recherche recherche;

	/** Mock de patient utilisé dans les recherches. */
	@Mock
	private Patient patient;

	/** Mock de médecin utilisé dans les recherches. */
	@Mock
	private Medecin medecin;

	/**
	 * Vérifie la recherche de patients par nom : le résultat doit contenir le
	 * patient renvoyé par le service.
	 */
	@Test
	void testRechercherPatientParNom() {
		List<Patient> patients = List.of(patient);

		when(gestionPatients.rechercherParNom("Dupont"))
						.thenReturn(patients);

		List<Patient> resultat = recherche.rechercherPatientParNom("Dupont");

		assertThat(resultat).hasSize(1);
		assertThat(resultat).contains(patient);
	}

	/**
	 * Vérifie la recherche d'un patient par numéro de sécurité sociale.
	 */
	@Test
	void testRechercherPatientParSecu() {
		when(gestionPatients.rechercherParNumeroSecu("123456789"))
						.thenReturn(patient);

		Patient resultat = recherche.rechercherPatientParSecu("123456789");

		assertThat(resultat).isEqualTo(patient);
	}

	/**
	 * Vérifie la recherche de médecins par nom.
	 */
	@Test
	void testRechercherMedecinParNom() {
		List<Medecin> medecins = List.of(medecin);

		when(gestionMedecins.rechercherParNom("Martin"))
						.thenReturn(medecins);

		List<Medecin> resultat = recherche.rechercherMedecinParNom("Martin");

		assertThat(resultat).hasSize(1);
		assertThat(resultat).contains(medecin);
	}

	/**
	 * Vérifie la recherche de médecins par spécialité.
	 */
	@Test
	void testRechercherMedecinParSpecialite() {
		List<Medecin> medecins = List.of(medecin);

		when(gestionMedecins.rechercherParSpecialite(Specialite.GENERALISTE))
						.thenReturn(medecins);

		List<Medecin> resultat =
						recherche.rechercherMedecinParSpecialite(Specialite.GENERALISTE);

		assertThat(resultat).hasSize(1);
		assertThat(resultat).contains(medecin);
	}

	/**
	 * Vérifie la recherche d'un médecin par numéro d'ordre.
	 */
	@Test
	void testRechercherMedecinParOrdre() {
		when(gestionMedecins.rechercherParNumeroOrdre("ORD123"))
						.thenReturn(medecin);

		Medecin resultat = recherche.rechercherMedecinParOrdre("ORD123");

		assertThat(resultat).isEqualTo(medecin);
	}

	/**
	 * Vérifie que la représentation textuelle (toString) contient le nombre de
	 * patients et de médecins.
	 */
	@Test
	void testToString() {
		when(gestionPatients.getNombrePatients()).thenReturn(5);
		when(gestionMedecins.getNombreMedecins()).thenReturn(3);

		String resultat = recherche.toString();

		assertThat(resultat)
						.contains("patients=5")
						.contains("médecins=3");
	}

	/**
	 * Vérifie que la recherche de patients par nom renvoie une liste vide
	 * lorsque le service lève une exception.
	 */
	@Test
	void testRechercherPatientParNomErreur() {
		when(gestionPatients.rechercherParNom("Dupont"))
						.thenThrow(new RuntimeException());

		List<Patient> resultat = recherche.rechercherPatientParNom("Dupont");

		assertThat(resultat).isEmpty();
	}

	/**
	 * Vérifie que la recherche d'un patient par numéro de sécurité sociale
	 * renvoie null lorsque le service lève une exception.
	 */
	@Test
	void testRechercherPatientParSecuErreur() {
		when(gestionPatients.rechercherParNumeroSecu("123"))
						.thenThrow(new RuntimeException());

		Patient resultat = recherche.rechercherPatientParSecu("123");

		assertThat(resultat).isNull();
	}
}