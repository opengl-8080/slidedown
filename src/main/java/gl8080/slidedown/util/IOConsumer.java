package gl8080.slidedown.util;

import java.io.IOException;

@FunctionalInterface
public interface IOConsumer<T> {
    
    void accept(T param) throws IOException;
}
