package gl8080.slidedown.model.settings;

import gl8080.slidedown.model.InvalidInputException;
import gl8080.slidedown.util.Processor;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SettingsTransaction {
    
    public static void with(Consumer<SettingsTransaction> consumer) throws InvalidInputException {
        SettingsTransaction tx = new SettingsTransaction(Settings.instance);
        consumer.accept(tx);
        tx.commit();
    }
    
    private Settings settings;
    private Set<Processor> computes = new HashSet<>();
    private Set<SettingsEvent> events = new HashSet<>();

    private SettingsTransaction(Settings settings) {
        this.settings = settings;
    }

    public void setFontFamily(String fontFamily) {
        this.computes.add(() -> this.settings.setFontFamily(fontFamily));
        this.events.add(SettingsEvent.EDITOR);
    }

    public void setFontSize(int fontSize) {
        this.computes.add(() -> this.settings.setFontSize(fontSize));
        this.events.add(SettingsEvent.EDITOR);
    }

    public void setRatio(Ratio ratio) {
        this.computes.add(() -> this.settings.setRatio(ratio));
        this.events.add(SettingsEvent.RATIO);
    }

    private void commit() {
        this.computes.forEach(compute -> compute.process());
        this.settings.save();
        this.events.forEach(event -> this.settings.fire(event));
    }
}
