package gl8080.slidedown.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FullScreenController {

    public static void showStage(WebView webView, SplitPane mainSplitPane) throws IOException {
        int webViewIndex = mainSplitPane.getItems().indexOf(webView);
        
        FXMLLoader loader = new FXMLLoader(FullScreenController.class.getResource("/fxml/fullscreen.fxml"));
        loader.load();
        
        FullScreenController controller = loader.getController();
        controller.borderPane.setCenter(webView);
        
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("F11"));
        stage.setFullScreen(true);
        
        Scene scene = new Scene(loader.getRoot());
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F11) {
                stage.close();
                mainSplitPane.getItems().set(webViewIndex, webView);
            }
        });
        
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private BorderPane borderPane;
}
