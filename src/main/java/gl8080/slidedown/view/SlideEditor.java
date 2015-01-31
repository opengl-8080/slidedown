package gl8080.slidedown.view;

import java.io.IOException;

public class SlideEditor {
    
    private MarkdownEditor markdownEditor;
    private CssEditor cssEditor;

    public SlideEditor(MarkdownEditor markdownEditor, CssEditor cssEditor) {
        this.markdownEditor = markdownEditor;
        this.cssEditor = cssEditor;
    }

    public void saveAs() throws IOException {
        this.markdownEditor.saveAs();
        this.cssEditor.save();
    }

    public void openWithFileChooser() throws IOException {
        this.markdownEditor.open();
        this.cssEditor.open();
    }

    public void save() throws IOException {
        this.markdownEditor.save();
        this.cssEditor.save();
    }

    public void close() {
        this.markdownEditor.close();
        this.cssEditor.close();
    }

    public void newFile() throws IOException {
        this.markdownEditor.newFile();
        this.cssEditor.newFile();
    }
}
