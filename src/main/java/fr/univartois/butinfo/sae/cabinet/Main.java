package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.controleur.ControllerAccueil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/sae/cabinet/vue/Accueil-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ControllerAccueil controleur = loader.getController();
        controleur.setStage(stage);
        controleur.setScene(scene);

        stage.setScene(scene);
        stage.setTitle("Cabinet Médical");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}