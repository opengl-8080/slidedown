package gl8080.slidedown.view;

import gl8080.slidedown.model.settings.Settings;
import gl8080.slidedown.model.CssFile;
import gl8080.slidedown.model.MarkdownFile;
import gl8080.slidedown.model.Preview;
import java.io.IOException;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlidePreview {
    
    private static final Logger logger = LoggerFactory.getLogger(SlidePreview.class);
    private WebView webView;
    private Preview preview;

    public SlidePreview(WebView webView, MarkdownFile markdownFile, CssFile cssFile) throws IOException {
        this.webView = webView;
        this.preview = new Preview(markdownFile, cssFile);
        
        Settings.instance.onChangeRatio(() -> {
            if (this.preview.isOpened()) {
                this.load();
            }
        });
    }

    public void load() throws IOException {
        this.preview.load();
        this.webView.getEngine().load(this.preview.getUrl());
    }
    
    public void reload() throws IOException {
        this.preview.load();
        this.webView.getEngine().reload();
    }

    public void close() {
        this.webView.getEngine().executeScript("document.write('')");
        this.preview.close();
    }
    
    public void nextSlide() {
        this.executeScript("nextSlide();");
    }
    
    public void previousSlide() {
        this.executeScript("previousSlide();");
    }
    
    public void firstSlide() {
        this.executeScript("firstSlide();");
    }
    
    public void lastSlide() {
        this.executeScript("lastSlide();");
    }
    
    private void executeScript(String script) {
        this.webView.getEngine().executeScript(script);
    }

    public String getUrl() {
        return this.preview.getUrl();
    }

    public WebView getWebView() {
        System.out.println("doyaa");
        return this.webView;
    }
}
