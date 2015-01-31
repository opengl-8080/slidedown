package gl8080.slidedown.model.settings;

import gl8080.slidedown.model.InvalidInputException;
import gl8080.slidedown.Main;
import gl8080.slidedown.util.observer.EventAnnouncer;
import gl8080.slidedown.util.observer.IOObserver;
import gl8080.slidedown.util.observer.Observer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {
    
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);
    
    private static final String DEFAULT_FONT_FAMILY = "Monospaced Regular";
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final Ratio DEFAULT_RATIO = Ratio.STANDARD;
    
    public static final Settings instance = new Settings();
    
    private Properties prop;
    private EventAnnouncer announcer = new EventAnnouncer();
    
    private Settings() {
        try {
            this.initializeConfFile();
            this.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void initializeConfFile() throws IOException {
        File confFile = this.resolveConfFile();
        
        if (!confFile.exists()) {
            this.prop = new Properties();
            confFile.createNewFile();

            this.setFontFamily(DEFAULT_FONT_FAMILY);
            this.setFontSize(DEFAULT_FONT_SIZE);
            this.setRatio(DEFAULT_RATIO);
            this.save();

            logger.debug("initialize config file. : {}", confFile);
        }
    }
    
    private File resolveConfFile() {
        File confDir = this.resolveConfDir();
        return new File(confDir, "selidedown.xml");
    }
    
    private File resolveConfDir() {
        File confDir = new File(Main.resolveAppDir(), "conf");
        if (!confDir.exists()) {
            confDir.mkdirs();
        }
        return confDir;
    }
    
    private void load() throws IOException {
        File confFile = this.resolveConfFile();
        this.prop = new Properties();
        InputStream is = new FileInputStream(confFile);
        this.prop.loadFromXML(is);
    }
    
    void save() {
        File confFile = this.resolveConfFile();
        
        try (OutputStream os = new FileOutputStream(confFile)) {
            this.prop.storeToXML(os, "SlideDown");
        } catch (IOException ex) {
            logger.error("error", ex);
        }
    }

    void setFontFamily(String fontFamily) {
        this.prop.setProperty("font.family", fontFamily);
    }

    public String getFontFamily() {
        return this.prop.getProperty("font.family");
    }

    void setFontSize(int fontSize) throws InvalidInputException {
        if (fontSize < 1) {
            throw new InvalidInputException("フォントサイズは、 1 以上の値を指定してください。");
        }
        
        this.prop.setProperty("font.size", String.valueOf(fontSize));
    }

    public int getFontSize() {
        return Integer.parseInt(prop.getProperty("font.size"));
    }
    
    void setRatio(Ratio ratio) {
        this.prop.setProperty("slide.ratio", ratio.name());
    }

    public Ratio getRatio() {
        return Ratio.valueOf(this.prop.getProperty("slide.ratio"));
    }

    void fire(SettingsEvent event) {
        this.announcer.fire(event);
    }
    
    public void onChangeEditorSettings(Observer observer) {
        this.announcer.addObserver(SettingsEvent.EDITOR, observer);
    }
    
    public void onChangeRatio(IOObserver observer) {
        this.announcer.addObserver(SettingsEvent.RATIO, observer);
    }
}
