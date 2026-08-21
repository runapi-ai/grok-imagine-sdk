package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for edit image operations. */
public final class EditImageModel extends GrokimagineValue {
  /** grok-imagine-edit-image model slug. */
  public static final EditImageModel GROK_IMAGINE_EDIT_IMAGE = new EditImageModel("grok-imagine-edit-image");

  /** Image 2.0 segment-map-guided edit model slug. */
  public static final EditImageModel GROK_IMAGINE_IMAGE_2_0 = new EditImageModel("grok-imagine-image-2-0");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public EditImageModel(String value) {
    super(value);
  }
}
