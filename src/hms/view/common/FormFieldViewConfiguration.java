package hms.view.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FormFieldViewConfiguration {

  private final String key;
  private final String label;
  private final boolean required;
  private final boolean editable;
  private final FormFieldInputType inputType;
  private final List<SelectionItem> selectionItems;

  public FormFieldViewConfiguration(String key,
                                    String label,
                                    boolean required,
                                    boolean editable,
                                    FormFieldInputType inputType,
                                    List<SelectionItem> selectionItems) {
    this.key = Objects.requireNonNull(key, "key");
    this.label = Objects.requireNonNull(label, "label");
    this.required = required;
    this.editable = editable;
    this.inputType = Objects.requireNonNull(inputType, "inputType");
    this.selectionItems = selectionItems == null ? new ArrayList<>() : new ArrayList<>(selectionItems);
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

  public FormFieldInputType getInputType() {
    return inputType;
  }

  public List<SelectionItem> getSelectionItems() {
    return new ArrayList<>(selectionItems);
  }

  public static FormFieldViewConfiguration requiredEditable(String key, String label) {
    return new FormFieldViewConfiguration(key, label, true, true, FormFieldInputType.TEXT, List.of());
  }

  public static FormFieldViewConfiguration optionalEditable(String key, String label) {
    return new FormFieldViewConfiguration(key, label, false, true, FormFieldInputType.TEXT, List.of());
  }

  public static FormFieldViewConfiguration requiredReadOnly(String key, String label) {
    return new FormFieldViewConfiguration(key, label, true, false, FormFieldInputType.TEXT, List.of());
  }

  public static FormFieldViewConfiguration requiredSelect(String key, String label, List<SelectionItem> selectionItems) {
    return new FormFieldViewConfiguration(key, label, true, true, FormFieldInputType.SELECT, selectionItems);
  }

  public static FormFieldViewConfiguration optionalSelect(String key, String label, List<SelectionItem> selectionItems) {
    return new FormFieldViewConfiguration(key, label, false, true, FormFieldInputType.SELECT, selectionItems);
  }
}