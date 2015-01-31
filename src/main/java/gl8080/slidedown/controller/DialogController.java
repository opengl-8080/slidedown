package gl8080.slidedown.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogController {
    
    private static final Logger logger = LoggerFactory.getLogger(DialogController.class);
    
    public static void showErrorDialog(Stage parent, String message) {
        showDialog(parent, "Error", message);
    }
    
    public static void showDialog(Stage parent, String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogController.class.getResource("/fxml/dialog.fxml"));
            loader.load();
            
            Parent root = loader.getRoot();
            
            Scene scene = new Scene(root);
            Stage dialog = new Stage(StageStyle.UTILITY);
            
            DialogController controller = loader.getController();
            controller.stage = dialog;
            controller.messageLabel.setText(message);
            
            dialog.setTitle(title);
            dialog.setScene(scene);
            dialog.initOwner(parent);
            dialog.setResizable(false);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        } catch (IOException e) {
            logger.error("error", e);
        }
    }
    
    @FXML
    private Label messageLabel;
    private Stage stage;

    @FXML
    public void onOkAction() {
        this.stage.hide();
    }
}
