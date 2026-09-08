package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerSupprimerMedicament {

  private static final String FXML_MEDICAMENT = "/fr/univartois/butinfo/sae/cabinet/vue/Medicament-view.fxml";

  private GestionMedicaments gestionMedicaments = GestionMedicaments.getInstance();

  @FXML private TextField textFieldEnlever;
  @FXML private Button retour;
  @FXML private Button valideEnlever;

  @FXML
  private void initialize() {
    valideEnlever.setOnAction(e -> supprimer());
    retour.setOnAction(e -> retourListeMedicament());
  }

  public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
    this.gestionMedicaments = gestionMedicaments;
  }

  private void supprimer() {
    String saisie = (textFieldEnlever.getText() == null) ? "" : textFieldEnlever.getText().trim();
    if (saisie.isEmpty() || gestionMedicaments == null) {
      return;
    }
    int id;
    try {
      id = Integer.parseInt(saisie);
    } catch (NumberFormatException e) {
      new Alert(Alert.AlertType.WARNING, "L'ID doit être un nombre entier.").showAndWait();
      return;
    }
    if (gestionMedicaments.rechercherParId(id) == null) {
      new Alert(Alert.AlertType.WARNING, "Aucun médicament avec l'ID " + id).showAndWait();
      return;
    }
    gestionMedicaments.supprimerMedicament(id);
    retourListeMedicament();
  }

  private void retourListeMedicament() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_MEDICAMENT));
      Parent root = loader.load();
      ControllerMedicament ctrl = loader.getController();
      ctrl.setGestionMedicaments(gestionMedicaments);
      Stage stage = (Stage) retour.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}