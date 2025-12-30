package hms.view.common;

public class SelectionItem {

  private final String value;
  private final String label;

  public SelectionItem(String value, String label) {
    this.value = value == null ? "" : value.trim();
    this.label = label == null ? "" : label.trim();
  }

  public String getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }
}