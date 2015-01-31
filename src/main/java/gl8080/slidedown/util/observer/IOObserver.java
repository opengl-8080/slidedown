package gl8080.slidedown.util.observer;

import gl8080.slidedown.util.observer.Observer;
import java.io.IOException;
import java.io.UncheckedIOException;

@FunctionalInterface
public interface IOObserver extends Observer {
    
    @Override
    default void update() {
        try {
            this.ioUpdate();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    void ioUpdate() throws IOException;
}
