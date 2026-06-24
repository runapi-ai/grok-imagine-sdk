package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to image operations. */
public final class TextToImageModel extends GrokimagineValue {
  /** grok-imagine-text-to-image model slug. */
  public static final TextToImageModel GROK_IMAGINE_TEXT_TO_IMAGE = new TextToImageModel("grok-imagine-text-to-image");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToImageModel(String value) {
    super(value);
  }
}
