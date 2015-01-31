package gl8080.slidedown.model;

import gl8080.slidedown.model.settings.Settings;
import gl8080.slidedown.util.FileUtil;
import java.io.File;
import java.io.IOException;

public class Preview {
    private MarkdownFile markdownFile;
    private CssFile cssFile;
    private File previewBaseFile;
    private File previewFile;
    private boolean open;

    public Preview(MarkdownFile markdownFile, CssFile cssFile) throws IOException {
        this.markdownFile = markdownFile;
        this.cssFile = cssFile;
        this.createBaseFile();
    }
    
    private void createBaseFile() throws IOException {
        if (this.previewBaseFile == null) {
            this.previewBaseFile = File.createTempFile("preview_base", ".html");
            this.previewBaseFile.deleteOnExit();
            FileUtil.copyResourceToFile("/template/preview_template.html", this.previewBaseFile);
        }
    }
    
    public void load() throws IOException {
        this.touchPreviewFile();
        this.embedMarkdownAndCssUrl();
        this.open = true;
    }
    
    private void touchPreviewFile() throws IOException {
        if (this.previewFile == null) {
            this.previewFile = File.createTempFile("preview", ".html", this.markdownFile.getDirectory());
            this.previewFile.deleteOnExit();
        }
    }
    
    private void embedMarkdownAndCssUrl() throws IOException {
        String markdown = this.markdownFile.getText();
        String cssUrl = this.cssFile.getUrl();
        String ratio = Settings.instance.getRatio().value;
        
        FileUtil.withWriter(this.previewFile, writer -> {
            FileUtil.eachLine(this.previewBaseFile, line -> {
                writer.write(
                    line.replace("#{markdown}", markdown)
                        .replace("#{cssUrl}", cssUrl)
                        .replace("#{ratio}", ratio)
                );
            });
        });
    }

    public String getUrl() {
        return this.previewFile.toURI().toString();
    }
    
    public void close() {
        this.open = false;
    }
    
    public boolean isOpened() {
        return this.open;
    }
}
