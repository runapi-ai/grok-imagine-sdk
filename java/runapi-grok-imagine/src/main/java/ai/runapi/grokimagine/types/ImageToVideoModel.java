package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for image to video operations. */
public final class ImageToVideoModel extends GrokimagineValue {
  /** grok-imagine-image-to-video model slug. */
  public static final ImageToVideoModel GROK_IMAGINE_IMAGE_TO_VIDEO = new ImageToVideoModel("grok-imagine-image-to-video");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public ImageToVideoModel(String value) {
    super(value);
  }
}
