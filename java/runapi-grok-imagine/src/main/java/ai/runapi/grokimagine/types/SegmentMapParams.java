package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.Map;

/** Parameters for Image 2.0 segment-map operations. */
public final class SegmentMapParams {
  private final String model;
  private final String imageUrl;
  private final String sourceTaskId;
  private final String callbackUrl;

  private SegmentMapParams(Builder builder) {
    this.model = builder.model;
    this.imageUrl = builder.imageUrl;
    this.sourceTaskId = builder.sourceTaskId;
    this.callbackUrl = builder.callbackUrl;
  }

  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/segment-map";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", GrokimagineParamUtils.wireValue(model));
    raw.put("image_url", GrokimagineParamUtils.wireValue(imageUrl));
    raw.put("source_task_id", GrokimagineParamUtils.wireValue(sourceTaskId));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    return GrokimagineParamUtils.compact(raw);
  }

  /** Builder for {@link SegmentMapParams}. */
  public static final class Builder {
    private String model;
    private String imageUrl;
    private String sourceTaskId;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(SegmentMapModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = GrokimagineParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }

    /** Sets the public image URL that the service should segment. */
    public Builder imageUrl(String value) {
      this.imageUrl = GrokimagineParamUtils.requireNonBlank(value, "imageUrl");
      return this;
    }

    /**
     * Sets the completed Image 2.0 text-to-image task ID.
     * Compatibility input; use {@link #imageUrl(String)} instead when available.
     */
    @Deprecated
    public Builder sourceTaskId(String value) {
      this.sourceTaskId = GrokimagineParamUtils.requireNonBlank(value, "sourceTaskId");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = GrokimagineParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable segment-map parameters. */
    public SegmentMapParams build() {
      return new SegmentMapParams(this);
    }
  }
}
