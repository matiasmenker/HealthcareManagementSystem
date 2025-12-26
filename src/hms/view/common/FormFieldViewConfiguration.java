package hms.view.common;

public class FormFieldViewConfiguration {
  private final String key;
  private final String label;
  private final boolean required;
  private final boolean editable;

  public FormFieldViewConfiguration(String key, String label, boolean required, boolean editable) {
    this.key = key;
    this.label = label;
    this.required = required;
    this.editable = editable;
  }

  public String getKey() {
    return key;
  }

  public String getLabel() {
    return label;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean isEditable() {
    return editable;
  }

  public static FormFieldViewConfiguration requiredEditable(String key, String label) {
    return new FormFieldViewConfiguration(key, label, true, true);
  }

  public static FormFieldViewConfiguration requiredReadOnly(String key, String label) {
    return new FormFieldViewConfiguration(key, label, true, false);
  }

  public static FormFieldViewConfiguration optionalEditable(String key, String label) {
    return new FormFieldViewConfiguration(key, label, false, true);
  }

  public static FormFieldViewConfiguration optionalReadOnly(String key, String label) {
    return new FormFieldViewConfiguration(key, label, false, false);
  }
}