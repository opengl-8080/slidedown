package gl8080.slidedown.util.observer;

import gl8080.slidedown.util.observer.Observer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventAnnouncer {
    private static final Logger logger = LoggerFactory.getLogger(EventAnnouncer.class);
    private final Map<Event, Set<Observer>> observerStore = new HashMap<>();
    
    public void addObserver(Event event, Observer... observers) {
        if (event == null || observers == null) throw new NullPointerException();
        if (observers.length == 0) throw new IllegalArgumentException("observers is empty");
        
        logger.trace("addObserver({}, {})", event, observers);
        
        this.observerStore.putIfAbsent(event, new HashSet<>());
        
        this.observerStore.compute(event, (key, store) -> {
            store.addAll(Arrays.asList(observers));
            return store;
        });
    }
    
    public void fire(Event event) {
        logger.trace("fire({})", event);
        if (this.observerStore.containsKey(event)) {
            logger.trace("containsKey({})", event);
            this.observerStore.get(event).forEach(observer -> observer.update());
        }
    }
}
