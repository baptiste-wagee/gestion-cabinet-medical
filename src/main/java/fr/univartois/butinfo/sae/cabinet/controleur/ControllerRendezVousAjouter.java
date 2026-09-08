package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.modele.RendezVous;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import fr.univartois.butinfo.sae.cabinet.service.GestionRendezVous;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ControllerRendezVousAjouter {

	@FXML private ListView<RendezVous> listeRDV;
	@FXML private ComboBox<Medecin> medecinAjouterRDV;
	@FXML private ComboBox<Patient> patientAjouterRDV;
	@FXML private TextField dureeAjouterRDV;
	@FXML private TextArea motifAjouterRDV;
	@FXML private DatePicker dateAjouterRDV;
	@FXML private TextField heureAjouterRDV;
	@FXML private Button validerAjouterRDV;
	@FXML private Button accueilAjouterRDV;

	private GestionRendezVous gestionRendezVous;

	// --- RÉFÉRENCES DE NAVIGATION ET SYNCHRONISATION ---
	private Stage stage;
	private Scene scenePrecedente;
	private Runnable onActionSuccess;

	private static final Logger logger = LoggerFactory.getLogger(ControllerRendezVousAjouter.class);

	@FXML
	private void initialize() {
		// Remplissage des listes déroulantes à partir des services existants
		medecinAjouterRDV.setItems(GestionMedecins.getInstance().getMedecins());
		medecinAjouterRDV.setCellFactory(lv -> creerCelluleMedecin());
		medecinAjouterRDV.setButtonCell(creerCelluleMedecin());

		patientAjouterRDV.setItems(GestionPatients.getInstance().getPatients());
		patientAjouterRDV.setCellFactory(lv -> creerCellulePatient());
		patientAjouterRDV.setButtonCell(creerCellulePatient());
	}

	private ListCell<Medecin> creerCelluleMedecin() {
		return new ListCell<>() {
			@Override
			protected void updateItem(Medecin m, boolean empty) {
				super.updateItem(m, empty);
				setText((empty || m == null) ? null : "Dr " + m.getNomComplet());
			}
		};
	}

	private ListCell<Patient> creerCellulePatient() {
		return new ListCell<>() {
			@Override
			protected void updateItem(Patient p, boolean empty) {
				super.updateItem(p, empty);
				setText((empty || p == null) ? null : p.getNomComplet());
			}
		};
	}

	public void setGestionRendezVous(GestionRendezVous gestionRendezVous) {
		this.gestionRendezVous = gestionRendezVous;

		if (gestionRendezVous != null && listeRDV != null) {
			listeRDV.setItems(gestionRendezVous.getRendezVous());

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
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setScene(Scene scene) {
		this.scenePrecedente = scene;
	}

	public void setOnActionSuccess(Runnable onActionSuccess) {
		this.onActionSuccess = onActionSuccess;
	}

	@FXML
	private void handleValider() {
		try {
			Medecin medecin = medecinAjouterRDV.getValue();
			Patient patient = patientAjouterRDV.getValue();

			if (medecin == null) {
				new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un médecin.").showAndWait();
				return;
			}
			if (patient == null) {
				new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un patient.").showAndWait();
				return;
			}
			if (dateAjouterRDV.getValue() == null) {
				new Alert(Alert.AlertType.WARNING, "Veuillez choisir une date.").showAndWait();
				return;
			}

			String heureTexte = heureAjouterRDV.getText() == null ? "" : heureAjouterRDV.getText().trim();
			int heures;
			int minutes = 0;

			if (heureTexte.contains("h")) {
				String[] heureSplit = heureTexte.split("h");
				heures = Integer.parseInt(heureSplit[0]);
				minutes = heureSplit.length > 1 && !heureSplit[1].isEmpty() ? Integer.parseInt(heureSplit[1]) : 0;
			} else if (heureTexte.contains(":")) {
				String[] heureSplit = heureTexte.split(":");
				heures = Integer.parseInt(heureSplit[0]);
				minutes = Integer.parseInt(heureSplit[1]);
			} else {
				heures = Integer.parseInt(heureTexte);
				minutes = 0;
			}

			LocalDateTime dateHeure = dateAjouterRDV.getValue().atTime(heures, minutes);
			int duree = Integer.parseInt(dureeAjouterRDV.getText().trim());
			String motif = motifAjouterRDV.getText();

			if (gestionRendezVous.prendreRendezVous(patient, medecin, dateHeure, duree, motif) == null) {
				new Alert(Alert.AlertType.WARNING, "Créneau non disponible.").showAndWait();
				return;
			}

			logger.info("RDV ajouté avec succès.");
			fermer();

		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
			logger.error("Erreur lors de l'ajout du RDV : {}", e.getMessage(), e);
		}
	}

	@FXML
	private void handleRetour() {
		fermer();
	}

	private void fermer() {
		if (onActionSuccess != null) {
			onActionSuccess.run();
		}

		Stage stageActuel = (this.stage != null) ? this.stage : (Stage) validerAjouterRDV.getScene().getWindow();

		if (stageActuel != null && scenePrecedente != null) {
			stageActuel.setScene(scenePrecedente);
			stageActuel.setTitle("Gestion des Rendez-vous");
		}
	}
}