package gl8080.slidedown.model;

import gl8080.slidedown.util.FileUtil;
import gl8080.slidedown.util.observer.Event;
import gl8080.slidedown.util.observer.EventAnnouncer;
import gl8080.slidedown.util.observer.IOObserver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssFile {
    private static final Logger logger = LoggerFactory.getLogger(CssFile.class);

    private File file;
    private final MarkdownFile markdownFile;
    private EventAnnouncer announcer = new EventAnnouncer();

    public CssFile(MarkdownFile markdownFile) {
        this.markdownFile = markdownFile;
    }
    
    public String getText() throws IOException {
        return FileUtil.read(this.file);
    }
    
    public void save(String text) throws IOException {
        this.resolveFile();
        FileUtil.write(this.file, text);
        this.announcer.fire(CssFileEvent.SAVE);
    }
    
    public String getUrl() {
        logger.trace("file={}", file);
        return this.file.toURI().toString();
    }
    
    public void open() {
        this.announcer.fire(CssFileEvent.OPEN);
    }
    
    public void close() {
        this.file = null;
        this.announcer.fire(CssFileEvent.CLOSE);
    }
    
    public String getFileName() {
        return this.file.getName();
    }
    
    public void initCss() throws IOException {
        Optional<Path> css = Files.list(this.markdownFile.getDirectory().toPath())
                                  .filter(this::isCssFile)
                                  .filter(this::equalsToMarkdownFileNameWithoutExtension)
                                  .findFirst();

        if (css.isPresent()) {
            logger.trace("initCss css isPresent : {}", css.get());
            this.file = css.get().toFile();
        } else {
            this.createCss();
            logger.trace("initCss css is not present : {}", this.file);
        }
    }
    
    private boolean isCssFile(Path path) {
        return path.toFile().getName().endsWith(".css");
    }
    
    private boolean equalsToMarkdownFileNameWithoutExtension(Path path) {
        String baseName = path.toFile().getName().replaceAll("\\.css$", "");
        String markdownBaseName = this.markdownFile.getFileName().replaceAll("\\.md$", "");
        
        return baseName.equals(markdownBaseName);
    }

    private void createCss() throws IOException {
        this.resolveFile();
        FileUtil.copyResourceToFile("/template/style.css", this.file);
    }
    
    private void resolveFile() {
        String markdownFileName = this.markdownFile.getFileName();
        this.file = new File(this.markdownFile.getDirectory(), markdownFileName.replaceAll("\\.md$", ".css"));
    }
    
    public CssFile onOpen(IOObserver observer) {
        this.announcer.addObserver(CssFileEvent.OPEN, observer);
        return this;
    }
    
    public CssFile onClose(IOObserver observer) {
        this.announcer.addObserver(CssFileEvent.CLOSE, observer);
        return this;
    }
    
    public CssFile onSave(IOObserver observer) {
        this.announcer.addObserver(CssFileEvent.SAVE, observer);
        return this;
    }

    public void newFile() throws IOException {
        this.createCss();
    }
    
    private static enum CssFileEvent implements Event {
        OPEN,
        CLOSE,
        SAVE,
    }
}
