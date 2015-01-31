package gl8080.slidedown.util;

import java.io.IOException;

@FunctionalInterface
public interface IOProcessor {
    
    void process() throws IOException;
}
