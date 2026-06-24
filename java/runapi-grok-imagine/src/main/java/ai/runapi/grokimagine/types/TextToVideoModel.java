package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to video operations. */
public final class TextToVideoModel extends GrokimagineValue {
  /** grok-imagine-text-to-video model slug. */
  public static final TextToVideoModel GROK_IMAGINE_TEXT_TO_VIDEO = new TextToVideoModel("grok-imagine-text-to-video");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToVideoModel(String value) {
    super(value);
  }
}
