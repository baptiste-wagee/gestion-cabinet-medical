package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerSupprimerPatient {

  private static final String FXML_PATIENT = "/fr/univartois/butinfo/sae/cabinet/vue/Patient-view.fxml";

  private GestionPatients gestionPatients = GestionPatients.getInstance();

  @FXML private TextField textFieldEnlever;
  @FXML private Button retour;
  @FXML private Button valideEnlever;

  @FXML
  private void initialize() {
    valideEnlever.setOnAction(e -> supprimer());
    retour.setOnAction(e -> retourListePatient());
  }

  public void setGestionPatients(GestionPatients gestionPatients) {
    this.gestionPatients = gestionPatients;
  }

  private void supprimer() {
    String saisie = (textFieldEnlever.getText() == null) ? "" : textFieldEnlever.getText().trim();
    if (saisie.isEmpty() || gestionPatients == null) {
      return;
    }
    int id;
    try {
      id = Integer.parseInt(saisie);
    } catch (NumberFormatException e) {
      new Alert(Alert.AlertType.WARNING, "L'ID doit être un nombre entier.").showAndWait();
      return;
    }
    if (gestionPatients.rechercherParId(id) == null) {
      new Alert(Alert.AlertType.WARNING, "Aucun patient avec l'ID " + id).showAndWait();
      return;
    }
    gestionPatients.supprimerPatient(id);
    retourListePatient();
  }

  private void retourListePatient() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATIENT));
      Parent root = loader.load();
      ControllerPatient ctrl = loader.getController();
      ctrl.setGestionPatients(gestionPatients);
      Stage stage = (Stage) retour.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}