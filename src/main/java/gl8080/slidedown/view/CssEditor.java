package gl8080.slidedown.view;

import gl8080.slidedown.model.settings.Settings;
import gl8080.slidedown.model.CssFile;
import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssEditor {
    private static final Logger logger = LoggerFactory.getLogger(CssEditor.class);
    private TextArea textArea;
    private Tab tab;
    private CssFile cssFile;

    public CssEditor(CssFile cssFile, TextArea textArea, Tab tab) {
        this.textArea = textArea;
        this.tab = tab;
        this.cssFile = cssFile;
        
        this.initEvent();
    }
    
    private void initEvent() {
        this.cssFile
            .onOpen(() -> {
                this.textArea.setText(this.cssFile.getText());
                this.tab.setText(this.cssFile.getFileName());
                this.textArea.setEditable(true);
                this.setFont();
            })
            .onClose(() -> {
                this.textArea.setText(null);
                this.tab.setText("CSS");
                this.textArea.setEditable(false);
            });
        
        Settings.instance.onChangeEditorSettings(() -> {
            this.setFont();
        });
    }
    
    private void setFont() {
        Font font = new Font(Settings.instance.getFontFamily(), Settings.instance.getFontSize());
        this.textArea.setFont(font);
    }

    public void save() throws IOException {
        this.cssFile.save(this.textArea.getText());
    }

    public void open() throws IOException {
        this.cssFile.initCss();
        this.cssFile.open();
    }

    public void close() {
        this.cssFile.close();
    }

    public void newFile() throws IOException {
        this.cssFile.newFile();
        this.cssFile.open();
    }
}
