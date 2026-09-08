package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.service.GestionRendezVous;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerRendezVousEnlever {

	@FXML private TextField textFieldEnlever;
	@FXML private Button retour;
	@FXML private Button valideEnlever;

	private GestionRendezVous gestionRendezVous;

	// --- RÉFÉRENCES DE NAVIGATION ET SYNCHRONISATION ---
	private Stage stage;
	private Scene scenePrecedente;
	private Runnable onActionSuccess;

	private static final Logger logger = LoggerFactory.getLogger(ControllerRendezVousEnlever.class);

	public void setGestionRendezVous(GestionRendezVous gestionRendezVous) {
		this.gestionRendezVous = gestionRendezVous;
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
		String saisie = textFieldEnlever.getText() == null ? "" : textFieldEnlever.getText().trim();

		if (saisie.isEmpty()) {
			new Alert(Alert.AlertType.WARNING, "Veuillez saisir un numéro de rendez-vous.").showAndWait();
			return;
		}

		if (gestionRendezVous == null) return;

		try {
			int id = Integer.parseInt(saisie);

			if (gestionRendezVous.rechercherParId(id) == null) {
				new Alert(Alert.AlertType.WARNING, "Aucun rendez-vous trouvé avec l'ID " + id).showAndWait();
				return;
			}

			gestionRendezVous.supprimerRendezVous(id);
			logger.info("RDV ID={} supprimé.", id);

			new Alert(Alert.AlertType.INFORMATION, "Le rendez-vous n°" + id + " a bien été supprimé.").showAndWait();

			// 1. On informe d'abord le contrôleur principal du succès de la suppression
			if (onActionSuccess != null) {
				onActionSuccess.run();
			}

			// 2. On ferme ensuite la fenêtre
			fermer();

		} catch (NumberFormatException e) {
			new Alert(Alert.AlertType.WARNING, "L'ID doit être un nombre entier valide.").showAndWait();
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + e.getMessage()).showAndWait();
			logger.error("Erreur suppression RDV : {}", e.getMessage(), e);
		}
	}

	@FXML
	private void handleRetour() {
		fermer();
	}

	private void fermer() {
		// NETTOYAGE : On a retiré le bloc onActionSuccess d'ici
		Stage stageActuel = (this.stage != null) ? this.stage : (Stage) valideEnlever.getScene().getWindow();

		if (stageActuel != null && scenePrecedente != null) {
			stageActuel.setScene(scenePrecedente);
			stageActuel.setTitle("Gestion des Rendez-vous");
		}
	}
}