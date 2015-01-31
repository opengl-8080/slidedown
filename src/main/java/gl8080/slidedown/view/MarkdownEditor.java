package gl8080.slidedown.view;

import gl8080.slidedown.model.settings.Settings;
import gl8080.slidedown.model.MarkdownFile;
import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class MarkdownEditor {
    
    private final TextArea textArea;
    private final Tab tab;
    private final MarkdownFile markdownFile;
    
    public MarkdownEditor(MarkdownFile markdownFile, TextArea slideEditor, Tab markdownEditorTab) {
        this.markdownFile = markdownFile;
        this.textArea = slideEditor;
        this.tab = markdownEditorTab;
        
        this.initEvent();
    }
    
    private void initEvent() {
        this.markdownFile
            .onOpenFile(() -> {
                this.tab.setText(this.markdownFile.getFileName());
                this.textArea.setText(this.markdownFile.getText());
                this.textArea.setEditable(true);
                this.setFont();
            })
            .onCloseFile(() -> {
                this.tab.setText("Markdown");
                this.textArea.setText(null);
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
    
    public void open() throws IOException {
        FileChooserHelper.MARKDOWN.showOpenDialog(file -> {
            this.markdownFile.setFile(file);
            this.markdownFile.open();
        });
    }

    public void save() throws IOException {
        this.markdownFile.save(this.textArea.getText());
    }
    
    public void saveAs() throws IOException {
        FileChooserHelper.MARKDOWN.showSaveDialog(file -> {
            this.markdownFile.setFile(file);
            this.save();
            this.markdownFile.open();
        });
    }

    public void close() {
        this.markdownFile.close();
    }

    public void newFile() throws IOException {
        FileChooserHelper.MARKDOWN.showSaveDialog(file -> {
            this.markdownFile.setFile(file);
            this.markdownFile.createNewFile();
            this.markdownFile.open();
        });
    }
}
