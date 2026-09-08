package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAjouterMedicament {

  private static final String FXML_MEDICAMENT = "/fr/univartois/butinfo/sae/cabinet/vue/Medicament-view.fxml";

  private GestionMedicaments gestionMedicaments = GestionMedicaments.getInstance();

  @FXML private ListView<Medicament> listViewMedicaments;
  @FXML private TextField textfieldNom;
  @FXML private TextArea textfieldDescription; // TextArea (modif que tu as faite dans le FXML)
  @FXML private TextField textfieldPrix;
  @FXML private Button btnAjouter;
  @FXML private Button btnAccueil; // bouton "Retour"

  @FXML
  public void initialize() {
    btnAccueil.setOnAction(e -> retourListeMedicament());
    listViewMedicaments.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> {
              if (nouveau == null) {
                return;
              }
              textfieldNom.setText(nouveau.getNom());
              textfieldDescription.setText(nouveau.getDescription());
              textfieldPrix.setText(String.valueOf(nouveau.getPrix()));
            });
  }

  public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
    this.gestionMedicaments = gestionMedicaments;
    listViewMedicaments.setItems(gestionMedicaments.getMedicaments());
  }

  @FXML
  void btnAjouter(ActionEvent event) {
    if (gestionMedicaments == null) {
      return;
    }
    String nom = textfieldNom.getText();
    if (nom == null || nom.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le nom est obligatoire.").showAndWait();
      return;
    }
    String prixTxt = textfieldPrix.getText();
    if (prixTxt == null || prixTxt.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le prix est obligatoire.").showAndWait();
      return;
    }
    double prix;
    try {
      // remplacement de la virgule par un point pour accepter "12,50"
      prix = Double.parseDouble(prixTxt.trim().replace(',', '.'));
    } catch (NumberFormatException e) {
      new Alert(Alert.AlertType.WARNING, "Prix invalide. Exemple : 12.50").showAndWait();
      return;
    }
    if (prix < 0) {
      new Alert(Alert.AlertType.WARNING, "Le prix ne peut pas être négatif.").showAndWait();
      return;
    }
    gestionMedicaments.ajouterMedicament(new Medicament(
            nom.trim(), textfieldDescription.getText(), prix));
    retourListeMedicament();
  }

  private void retourListeMedicament() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_MEDICAMENT));
      Parent root = loader.load();
      ControllerMedicament ctrl = loader.getController();
      ctrl.setGestionMedicaments(gestionMedicaments);
      Stage stage = (Stage) btnAccueil.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}