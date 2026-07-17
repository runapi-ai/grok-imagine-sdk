package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to video operations. */
public final class TextToVideoModel extends GrokimagineValue {
  /** grok-imagine-text-to-video model slug. */
  public static final TextToVideoModel GROK_IMAGINE_TEXT_TO_VIDEO = new TextToVideoModel("grok-imagine-text-to-video");
  /** grok-imagine-video-1.5-fast model slug. */
  public static final TextToVideoModel GROK_IMAGINE_VIDEO_1_5_FAST = new TextToVideoModel("grok-imagine-video-1.5-fast");
  /** grok-imagine-video-1.5-preview model slug. */
  public static final TextToVideoModel GROK_IMAGINE_VIDEO_1_5_PREVIEW = new TextToVideoModel("grok-imagine-video-1.5-preview");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToVideoModel(String value) {
    super(value);
  }
}
