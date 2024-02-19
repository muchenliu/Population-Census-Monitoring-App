package queryTemplates;

public class UpdateTemplate {
    private String attribute;
    private String value;

    public UpdateTemplate(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }
}
