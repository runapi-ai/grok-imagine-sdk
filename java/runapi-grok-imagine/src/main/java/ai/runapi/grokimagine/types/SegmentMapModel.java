package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for Image 2.0 segment-map operations. */
public final class SegmentMapModel extends GrokimagineValue {
  /** Image 2.0 model slug. */
  public static final SegmentMapModel GROK_IMAGINE_IMAGE_2_0 = new SegmentMapModel("grok-imagine-image-2-0");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public SegmentMapModel(String value) {
    super(value);
  }
}
