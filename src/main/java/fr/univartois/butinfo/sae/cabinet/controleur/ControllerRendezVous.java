package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.RendezVous;
import fr.univartois.butinfo.sae.cabinet.service.GestionRendezVous;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ControllerRendezVous {

	private static final String FXML_AJOUTER = "/fr/univartois/butinfo/sae/cabinet/vue/RendezVous-view_ajouter.fxml";
	private static final String FXML_ENLEVER = "/fr/univartois/butinfo/sae/cabinet/vue/RendezVous-view_enlever.fxml";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATTER_HEURE = DateTimeFormatter.ofPattern("HH:mm");

	@FXML private ListView<RendezVous> listeRDV;
	@FXML private Label numRDV;
	@FXML private Label dateRDV;
	@FXML private Label heureRDV;
	@FXML private Label dureeRDV;
	@FXML private Label statusRDV;
	@FXML private Label motifRDV;
	@FXML private Label medecinRDV;
	@FXML private Label patientRDV;
	@FXML private Label prixRDV;
	
	@FXML private Button AjouterRDV;
	@FXML private Button EnleverRDV;
	@FXML private Button accueilRDV;

	private Stage stage;
	private Scene scenePrecedente;
	private GestionRendezVous gestionRendezVous;
	private static final Logger logger = LoggerFactory.getLogger(ControllerRendezVous.class);

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setScene(Scene scene) {
		this.scenePrecedente = scene;
	}

	public void setGestionRendezVous(GestionRendezVous gestionRendezVous) {
		this.gestionRendezVous = gestionRendezVous;

		// Force l'affichage initial
		rafraichirListe();

		listeRDV.setCellFactory(list -> new ListCell<>() {
			@Override
			protected void updateItem(RendezVous rdv, boolean empty) {
				super.updateItem(rdv, empty);
				if (empty || rdv == null) {
					setText(null);
				} else {
					String patient = rdv.getPatient() != null ? rdv.getPatient().getNomComplet() : "Patient inconnu";
					setText("RDV #" + rdv.getId() + " - " + patient);
				}
			}
		});

		// Afficher les détails quand on clique sur un RDV
		listeRDV.getSelectionModel().selectedItemProperty()
				.addListener((obs, ancien, nouveau) -> afficherDetails(nouveau));
	}

	/**
	 * Rafraîchit la ListView avec les données de la façade et réinitialise la sélection.
	 */

	private void rafraichirListe() {
		if (gestionRendezVous != null && listeRDV != null) {
			// On convertit la liste en ObservableList pour forcer JavaFX à voir les changements
			listeRDV.setItems(FXCollections.observableArrayList(gestionRendezVous.getRendezVous()));

			listeRDV.getSelectionModel().clearSelection();
			afficherDetails(null); // Nettoie les labels de détails à droite
		}
	}

	private void afficherDetails(RendezVous rdv) {
		if (rdv == null) {
			numRDV.setText("");
			dateRDV.setText("");
			heureRDV.setText("");
			dureeRDV.setText("");
			statusRDV.setText("");
			motifRDV.setText("");
			medecinRDV.setText("");
			patientRDV.setText("");
			prixRDV.setText("");
			return;
		}
		numRDV.setText(String.valueOf(rdv.getId()));
		dateRDV.setText(rdv.getDateHeure().format(FORMATTER));
		heureRDV.setText(rdv.getDateHeure().format(FORMATTER_HEURE));
		dureeRDV.setText(rdv.getDureeMinutes() + " min");
		statusRDV.setText(rdv.getStatut().getLibelle());
		motifRDV.setText(rdv.getMotif());
		medecinRDV.setText(rdv.getMedecin() != null ? rdv.getMedecin().getNomComplet() : "Non renseigné");
		patientRDV.setText(rdv.getPatient() != null ? rdv.getPatient().getNomComplet() : "Non renseigné");
		try {
			prixRDV.setText(String.format("%.2f €", rdv.prixConsultation()));
		} catch (Exception e) {
			prixRDV.setText("N/A");
		}
	}

	@FXML
	private void handleAjouter() {
		ouvrirFenetre(FXML_AJOUTER, "Ajouter un rendez-vous");
	}

	@FXML
	private void handleEnlever() {
		ouvrirFenetre(FXML_ENLEVER, "Enlever un rendez-vous");
	}

	private void ouvrirFenetre(String cheminFxml, String titre) {
		if (gestionRendezVous == null) return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFxml));
			Parent root = loader.load();

			Object controleur = loader.getController();

			Stage stagePrincipal = (this.stage != null) ? this.stage : (Stage) listeRDV.getScene().getWindow();
			Runnable rafraichissementCallback = () -> rafraichirListe();

			if (controleur instanceof ControllerRendezVousAjouter c) {
				c.setGestionRendezVous(gestionRendezVous);
				c.setStage(stagePrincipal);
				c.setScene(listeRDV.getScene());
				c.setOnActionSuccess(rafraichissementCallback);
			} else if (controleur instanceof ControllerRendezVousEnlever c) {
				c.setGestionRendezVous(gestionRendezVous);
				c.setStage(stagePrincipal);
				c.setScene(listeRDV.getScene());
				c.setOnActionSuccess(rafraichissementCallback);
			}

			Scene nouvelleScene = new Scene(root);
			stagePrincipal.setTitle(titre);
			stagePrincipal.setScene(nouvelleScene);

		} catch (IOException e) {
			logger.error("Impossible d'ouvrir la vue : {}", e.getMessage(), e);
		}
	}

	@FXML
	private void handleAccueil() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/sae/cabinet/vue/Accueil-view.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			Stage stageActuel = (this.stage != null) ? this.stage : (Stage) listeRDV.getScene().getWindow();

			ControllerAccueil controleur = loader.getController();
			controleur.setStage(stageActuel);
			controleur.setScene(scene);

			stageActuel.setScene(scene);
		} catch (IOException e) {
			logger.error("Erreur lors du chargement de la vue accueil : {}", e.getMessage(), e);
		}
	}
}