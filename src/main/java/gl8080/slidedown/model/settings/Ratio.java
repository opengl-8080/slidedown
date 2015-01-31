package gl8080.slidedown.model.settings;

public enum Ratio {
    STANDARD("4:3"),
    WIDE("16:9"),
    ;

    public final String value;

    private Ratio(String value) {
        this.value = value;
    }
}
