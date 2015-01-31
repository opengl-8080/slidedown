package gl8080.slidedown.view;

import gl8080.slidedown.util.IOConsumer;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.stage.FileChooser;

public class FileChooserHelper {
    
    public static final FileChooserHelper MARKDOWN = new FileChooserHelper("markdown file (*.md)", "*.md");
    public static final FileChooserHelper HTML = new FileChooserHelper("html file (*.html)", "*.html");
    
    private final String description;
    private final String[] extentions;
    
    public void showOpenDialog(IOConsumer<File> consumer) throws IOException {
        this.showDialog(consumer, fc -> fc.showOpenDialog(null));
    }
    
    public void showSaveDialog(IOConsumer<File> consumer) throws IOException {
        this.showDialog(consumer, fc -> fc.showSaveDialog(null));
    }
    
    private void showDialog(IOConsumer<File> consumer, Function<FileChooser, File> openDialog) throws IOException {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(this.description, this.extentions);
        fileChooser.getExtensionFilters().add(filter);
        
        File file = openDialog.apply(fileChooser);
        
        if (file != null) {
            consumer.accept(file);
        }
    }
    
    private FileChooserHelper(String description, String... extentions) {
        this.description = description;
        this.extentions = extentions;
    }
}
