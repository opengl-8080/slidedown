package gl8080.slidedown.controller;

import gl8080.slidedown.model.ExportHtml;
import gl8080.slidedown.view.SlidePreview;
import gl8080.slidedown.model.settings.Settings;
import gl8080.slidedown.view.CssEditor;
import gl8080.slidedown.view.MarkdownEditor;
import gl8080.slidedown.model.CssFile;
import gl8080.slidedown.model.MarkdownFile;
import gl8080.slidedown.model.settings.Ratio;
import gl8080.slidedown.model.settings.SettingsTransaction;
import gl8080.slidedown.view.FileChooserHelper;
import gl8080.slidedown.view.SlideEditor;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    private Stage stage;
    @FXML private WebView preview;
    @FXML private TextArea markdownTextArea;
    @FXML private TextArea cssTextArea;
    @FXML private Tab markdownEditorTab;
    @FXML private Tab cssEditorTab;
    @FXML private MenuItem nextSlideMenu;
    @FXML private MenuItem previousSlideMenu;
    @FXML private MenuItem firstSlideMenu;
    @FXML private MenuItem lastSlideMenu;
    @FXML private MenuItem saveMenu;
    @FXML private MenuItem saveAsMenu;
    @FXML private MenuItem closeMenu;
    @FXML private MenuItem exportHtmlMenu;
    @FXML private MenuItem ratioMenu;
    @FXML private RadioMenuItem standardRatioMenu;
    @FXML private RadioMenuItem wideRatioMenu;
    @FXML private MenuItem fullScreenMenu;
    @FXML private SplitPane mainSplitPane;
    
    private MarkdownFile markdownFile = new MarkdownFile();
    private CssFile cssFile = new CssFile(markdownFile);
    
    private SlideEditor slideEditor;
    private SlidePreview slidePreview;
    private Set<MenuItem> enableMenuWhenOpenFile = new HashSet<>();
    
    private static final String DEFAULT_WINDOW_TITLE = "Slide Down";

    public void setStage(Stage stage) {
        this.stage = stage;
        
        this.stage.setTitle(DEFAULT_WINDOW_TITLE);
        
        this.markdownFile.onOpenFile(() -> {
            this.stage.setTitle(this.markdownFile.getFileName());
        });
        
        this.markdownFile.onCloseFile(() -> {
            this.stage.setTitle(DEFAULT_WINDOW_TITLE);
        });
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        MarkdownEditor markdownEditor = new MarkdownEditor(this.markdownFile, this.markdownTextArea, this.markdownEditorTab);
        CssEditor cssEditor = new CssEditor(this.cssFile, this.cssTextArea, this.cssEditorTab);
        
        this.slideEditor = new SlideEditor(markdownEditor, cssEditor);
        
        try {
            this.slidePreview = new SlidePreview(this.preview, markdownFile, cssFile);
        } catch (IOException e) {
            logger.error("プレビューの生成時にエラーが発生しました。", e);
            DialogController.showErrorDialog(this.stage, "プレビューの生成時にエラーが発生しました。");
        }
        
        this.initRatioMenuSelect();
        
        this.markdownFile
            .onOpenFile(()-> {
                this.enableMenuWhenOpenFile.forEach(menu -> menu.setDisable(false));
            })
            .onCloseFile(()-> {
                this.enableMenuWhenOpenFile.forEach(menu -> menu.setDisable(true));
            });
        
        this.enableMenuWhenOpenFile.addAll(Arrays.asList(
                this.nextSlideMenu,
                this.previousSlideMenu,
                this.firstSlideMenu,
                this.lastSlideMenu,
                this.fullScreenMenu,
                this.saveMenu,
                this.saveAsMenu,
                this.closeMenu,
                this.exportHtmlMenu,
                this.ratioMenu
        ));
    }
    
    private void initRatioMenuSelect() {
        if (Settings.instance.getRatio() == Ratio.STANDARD) {
            this.standardRatioMenu.setSelected(true);
        } else {
            this.wideRatioMenu.setSelected(true);
        }
    }
    
    @FXML
    public void newFile() {
        try {
            this.slideEditor.newFile();
            this.slidePreview.load();
        } catch (IOException e) {
            logger.error("新規ファイルの作成時にエラーが発生しました。", e);
            DialogController.showErrorDialog(this.stage, "新規ファイルの作成時にエラーが発生しました。");
        }
    }
    
    @FXML
    public void openFile() {
        try {
            this.slideEditor.openWithFileChooser();
            this.slidePreview.load();
        } catch(IOException e) {
            logger.error("ファイルのオープン時にエラーが発生しました。", e);
            DialogController.showErrorDialog(this.stage, "ファイルのオープン時にエラーが発生しました。");
        }
    }
    
    @FXML
    public void saveAsFile() {
        try {
            this.slideEditor.saveAs();
            this.slidePreview.reload();
        } catch (IOException e) {
            logger.error("ファイルの保存時にエラーが発生しました。", e);
            DialogController.showErrorDialog(this.stage, "ファイルの保存時にエラーが発生しました。");
        }
    }
    
    @FXML
    public void saveFile() {
        try {
            this.slideEditor.save();
            this.slidePreview.reload();
        } catch (IOException e) {
            logger.error("ファイルの保存時にエラーが発生しました。", e);
            DialogController.showErrorDialog(this.stage, "ファイルの保存時にエラーが発生しました。");
        }
    }
    
    @FXML
    public void nextSlide() {
        this.slidePreview.nextSlide();
    }
    
    @FXML
    public void previousSlide() {
        this.slidePreview.previousSlide();
    }
    
    @FXML
    public void firstSlide() {
        this.slidePreview.firstSlide();
    }
    
    @FXML
    public void lastSlide() {
        this.slidePreview.lastSlide();
    }
    
    @FXML
    public void close() {
        this.slideEditor.close();
        this.slidePreview.close();
    }
    
    @FXML
    public void openEditorSettings() {
        EditorSettingsController.showStage(this.stage);
    }
    
    @FXML
    public void exportHtml() {
        try {
            FileChooserHelper.HTML.showSaveDialog(file -> {
                new ExportHtml(this.markdownFile, this.cssFile).export(file);
            });
        } catch (IOException e) {
            logger.error("HTML エクスポート中にエラーが発生しました。", e);
            DialogController.showErrorDialog(this.stage, "HTML エクスポート中にエラーが発生しました。");
        }
    }
    
    @FXML
    public void switchFullScreen() throws IOException {
        FullScreenController.showStage(this.slidePreview.getWebView(), this.mainSplitPane);
    }
    
    @FXML
    public void selectRatioStandard() {
        SettingsTransaction.with(tx -> tx.setRatio(Ratio.STANDARD));
    }
    
    @FXML
    public void selectRatioWide() {
        SettingsTransaction.with(tx -> tx.setRatio(Ratio.WIDE));
    }
}
