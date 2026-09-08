package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAjouterMedecin {

  private static final String FXML_MEDECIN = "/fr/univartois/butinfo/sae/cabinet/vue/Medecin-view.fxml";

  private GestionMedecins gestionMedecins = GestionMedecins.getInstance();

  @FXML private ListView<Medecin> listViewMedecins;
  @FXML private TextField textfieldNom;
  @FXML private TextField textfieldPrenom;
  @FXML private TextField textfieldTelephone;
  @FXML private TextField textfieldEmail;
  @FXML private TextField textfieldNumeroOrdre;
  @FXML private ComboBox<Specialite> comboSpecialite;
  @FXML private TextField textfieldTarif;
  @FXML private Button btnAjouter;
  @FXML private Button btnAccueil; // bouton "Retour"

  @FXML
  public void initialize() {
    btnAccueil.setOnAction(e -> retourListeMedecin());

    // Remplit la liste déroulante avec toutes les valeurs de l'enum,
    // affichées via getLibelle().
    comboSpecialite.setItems(FXCollections.observableArrayList(Specialite.values()));
    comboSpecialite.setCellFactory(lv -> new ListCell<>() {
      @Override
      protected void updateItem(Specialite item, boolean empty) {
        super.updateItem(item, empty);
        setText((empty || item == null) ? null : item.getLibelle());
      }
    });
    comboSpecialite.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(Specialite item, boolean empty) {
        super.updateItem(item, empty);
        setText((empty || item == null) ? null : item.getLibelle());
      }
    });

    listViewMedecins.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> {
              if (nouveau == null) {
                return;
              }
              textfieldNom.setText(nouveau.getNom());
              textfieldPrenom.setText(nouveau.getPrenom());
              textfieldTelephone.setText(nouveau.getTelephone());
              textfieldEmail.setText(nouveau.getEmail());
              textfieldNumeroOrdre.setText(nouveau.getNumeroOrdre());
              comboSpecialite.setValue(nouveau.getSpecialite());
              textfieldTarif.setText(String.valueOf(nouveau.getTarifHoraire()));
            });
  }

  public void setGestionMedecins(GestionMedecins gestionMedecins) {
    this.gestionMedecins = gestionMedecins;
    listViewMedecins.setItems(gestionMedecins.getMedecins());
  }

  @FXML
  void btnAjouter(ActionEvent event) {
    if (gestionMedecins == null) {
      return;
    }
    String nom = textfieldNom.getText();
    if (nom == null || nom.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le nom est obligatoire.").showAndWait();
      return;
    }
    String numeroOrdre = textfieldNumeroOrdre.getText();
    if (numeroOrdre == null || numeroOrdre.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le numéro d'ordre est obligatoire.").showAndWait();
      return;
    }
    Specialite specialite = comboSpecialite.getValue();
    if (specialite == null) {
      new Alert(Alert.AlertType.WARNING, "La spécialité est obligatoire.").showAndWait();
      return;
    }
    String tarifTxt = textfieldTarif.getText();
    if (tarifTxt == null || tarifTxt.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le tarif horaire est obligatoire.").showAndWait();
      return;
    }
    double tarif;
    try {
      tarif = Double.parseDouble(tarifTxt.trim().replace(',', '.'));
    } catch (NumberFormatException e) {
      new Alert(Alert.AlertType.WARNING, "Tarif invalide. Exemple : 30.00").showAndWait();
      return;
    }
    if (tarif < 0) {
      new Alert(Alert.AlertType.WARNING, "Le tarif ne peut pas être négatif.").showAndWait();
      return;
    }
    gestionMedecins.ajouterMedecin(new Medecin(
            nom.trim(), textfieldPrenom.getText(), textfieldTelephone.getText(),
            textfieldEmail.getText(), numeroOrdre.trim(), specialite, tarif));
    retourListeMedecin();
  }

  private void retourListeMedecin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_MEDECIN));
      Parent root = loader.load();
      ControllerMedecin ctrl = loader.getController();
      ctrl.setGestionMedecins(gestionMedecins);
      Stage stage = (Stage) btnAccueil.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}