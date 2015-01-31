package gl8080.slidedown;

import gl8080.slidedown.controller.MainController;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    public static void main(String[] args) {
        File baseDir = Main.resolveAppDir();
        
        System.setProperty("log.dir", new File(baseDir, "log").getPath());
        
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.debug("start");
        
        Application.launch(args);
    }
    
    public static File resolveAppDir() {
        String baseDirPath = System.getenv("APP_HOME");
        return baseDirPath != null ? new File(baseDirPath) : new File(".");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Logger logger = LoggerFactory.getLogger(Main.class);
        
        Thread.currentThread().setUncaughtExceptionHandler((thread, e) -> {
            logger.error("unknown error", e);
        });
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        
        MainController controller = loader.getController();
        controller.setStage(stage);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
    }
}
