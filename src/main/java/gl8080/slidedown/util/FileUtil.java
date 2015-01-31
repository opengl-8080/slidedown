package gl8080.slidedown.util;

import gl8080.slidedown.util.IOConsumer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    
    public static void write(File file, String text) throws IOException {
        FileUtils.write(file, text, Charsets.UTF_8);
    }
    
    public static String read(File file) throws IOException {
        return FileUtils.readFileToString(file, Charsets.UTF_8);
    }
    
    public static void copyResourceToFile(String resource, File file) throws IOException {
        try (InputStream input = FileUtil.class.getResourceAsStream(resource);
             Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            IOUtils.copy(input, writer);
        }
    }
    
    public static void withWriter(File file, IOConsumer<Writer> consumer) throws IOException {
        try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            consumer.accept(writer);
        }
    }
    
    public static void eachLine(File file, IOConsumer<String> consumer) throws IOException {
        Files.lines(file.toPath(), StandardCharsets.UTF_8).forEach(line -> {
            try {
                consumer.accept(line);
            } catch (IOException e) {
                logger.error("error", e);
            } 
        });
    }
    
    private FileUtil() {}
}
