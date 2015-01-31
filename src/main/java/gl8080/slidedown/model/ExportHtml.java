package gl8080.slidedown.model;

import gl8080.slidedown.util.FileUtil;
import gl8080.slidedown.model.settings.Settings;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportHtml {
    private static final Logger logger = LoggerFactory.getLogger(ExportHtml.class);
    private MarkdownFile markdownFile;
    private CssFile cssFile;

    public ExportHtml(MarkdownFile markdownFile, CssFile cssFile) {
        this.markdownFile = markdownFile;
        this.cssFile = cssFile;
    }
    
    public void export(File html) throws IOException {
        File tmp = File.createTempFile("export", ".html");
        tmp.deleteOnExit();
        FileUtil.copyResourceToFile("/template/export_template.txt", tmp);

        String css = this.cssFile.getText();
        String markdown = this.markdownFile.getText();
        String ratio = Settings.instance.getRatio().value;

        FileUtil.withWriter(html, writer -> {
            String title = html.getName().replaceAll("\\.html$", "");

            FileUtil.eachLine(tmp, line -> {
                writer.write(
                    line.replace("#{css}", css)
                        .replace("#{markdown}", markdown)
                        .replace("#{title}", title)
                        .replace("#{ratio}", ratio) + "\n"
                );
            });
        });
    }
}
