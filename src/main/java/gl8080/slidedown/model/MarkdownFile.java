package gl8080.slidedown.model;

import gl8080.slidedown.util.FileUtil;
import gl8080.slidedown.util.observer.Event;
import gl8080.slidedown.util.observer.EventAnnouncer;
import gl8080.slidedown.util.observer.IOObserver;
import java.io.File;
import java.io.IOException;

public class MarkdownFile {
    
    private File file;
    private final EventAnnouncer announcer = new EventAnnouncer();
    
    public String getText() throws IOException {
        return FileUtil.read(this.file);
    }

    public void setFile(File file) {
        this.file = this.complementExtension(file);
    }
    
    private File complementExtension(File file) {
        if (file == null) {
            return null;
        } else if (!file.getName().endsWith(".md")) {
            return new File(file.getPath() + ".md");
        } else {
            return file;
        }
    }
    
    public void open() {
        this.announcer.fire(MarkdownFileEvent.OPEN);
    }
    
    public void close() {
        this.setFile(null);
        this.announcer.fire(MarkdownFileEvent.CLOSE);
    }

    public void save(String text) throws IOException {
        FileUtil.write(this.file, text);
        this.announcer.fire(MarkdownFileEvent.SAVE);
    }

    public File getParentFile() {
        return this.file.getParentFile();
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public File getDirectory() {
        return this.file.getParentFile();
    }
    
    public MarkdownFile onOpenFile(IOObserver observer) {
        this.announcer.addObserver(MarkdownFileEvent.OPEN, observer);
        return this;
    }
    
    public MarkdownFile onCloseFile(IOObserver observer) {
        this.announcer.addObserver(MarkdownFileEvent.CLOSE, observer);
        return this;
    }
    
    public MarkdownFile onSaved(IOObserver observer) {
        this.announcer.addObserver(MarkdownFileEvent.SAVE, observer);
        return this;
    }
    
    public MarkdownFile onCreateNewFile(IOObserver observer) {
        this.announcer.addObserver(MarkdownFileEvent.NEW, observer);
        return this;
    }

    public void createNewFile() throws IOException {
        if (this.file.exists()) {
            this.file.delete();
        }
        this.file.createNewFile();
        this.announcer.fire(MarkdownFileEvent.NEW);
    }
    
    private static enum MarkdownFileEvent implements Event {
        OPEN,
        CLOSE,
        SAVE,
        NEW,
    }
}
