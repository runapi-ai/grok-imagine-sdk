package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for image to video operations. */
public final class ImageToVideoParams {
  private final String model;
  private final String callbackUrl;
  private final List<String> sourceImageUrls;
  private final String sourceTaskId;
  private final Integer index;
  private final String prompt;
  private final String motionStyle;
  private final Integer durationSeconds;
  private final String outputResolution;
  private final String aspectRatio;
  private final Boolean enableSafetyChecker;

  private ImageToVideoParams(Builder builder) {
    this.model = builder.model;
    this.callbackUrl = builder.callbackUrl;
    this.sourceImageUrls = GrokimagineParamUtils.strings(builder.sourceImageUrls);
    this.sourceTaskId = builder.sourceTaskId;
    this.index = builder.index;
    this.prompt = builder.prompt;
    this.motionStyle = builder.motionStyle;
    this.durationSeconds = builder.durationSeconds;
    this.outputResolution = builder.outputResolution;
    this.aspectRatio = builder.aspectRatio;
    this.enableSafetyChecker = builder.enableSafetyChecker;
  }

  /** Creates a new ImageToVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/image-to-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", GrokimagineParamUtils.wireValue(model));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    raw.put("source_image_urls", GrokimagineParamUtils.wireValue(sourceImageUrls));
    raw.put("source_task_id", GrokimagineParamUtils.wireValue(sourceTaskId));
    raw.put("index", GrokimagineParamUtils.wireValue(index));
    raw.put("prompt", GrokimagineParamUtils.wireValue(prompt));
    raw.put("motion_style", GrokimagineParamUtils.wireValue(motionStyle));
    raw.put("duration_seconds", GrokimagineParamUtils.wireValue(durationSeconds));
    raw.put("output_resolution", GrokimagineParamUtils.wireValue(outputResolution));
    raw.put("aspect_ratio", GrokimagineParamUtils.wireValue(aspectRatio));
    raw.put("enable_safety_checker", GrokimagineParamUtils.wireValue(enableSafetyChecker));
    return GrokimagineParamUtils.compact(raw);
  }



  /** Builder for {@link ImageToVideoParams}. */
  public static final class Builder {
    private String model;
    private String callbackUrl;
    private List<String> sourceImageUrls;
    private String sourceTaskId;
    private Integer index;
    private String prompt;
    private String motionStyle;
    private Integer durationSeconds;
    private String outputResolution;
    private String aspectRatio;
    private Boolean enableSafetyChecker;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(ImageToVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = GrokimagineParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = GrokimagineParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Sets the source image URLs. */
    public Builder sourceImageUrls(List<String> value) {
      this.sourceImageUrls = value;
      return this;
    }

    /** Sets the source task ID. */
    public Builder sourceTaskId(String value) {
      this.sourceTaskId = GrokimagineParamUtils.requireNonBlank(value, "sourceTaskId");
      return this;
    }

    /** Sets the index. */
    public Builder index(int value) {
      this.index = value;
      return this;
    }

    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = GrokimagineParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the motion style. */
    public Builder motionStyle(String value) {
      this.motionStyle = GrokimagineParamUtils.requireNonBlank(value, "motionStyle");
      return this;
    }

    /** Sets the duration in seconds. */
    public Builder durationSeconds(int value) {
      this.durationSeconds = value;
      return this;
    }

    /** Sets the output resolution. */
    public Builder outputResolution(String value) {
      this.outputResolution = GrokimagineParamUtils.requireNonBlank(value, "outputResolution");
      return this;
    }

    /** Sets the output aspect ratio. */
    public Builder aspectRatio(String value) {
      this.aspectRatio = GrokimagineParamUtils.requireNonBlank(value, "aspectRatio");
      return this;
    }

    /** Sets the content safety checker toggle. */
    public Builder enableSafetyChecker(boolean value) {
      this.enableSafetyChecker = value;
      return this;
    }

    /** Builds immutable image to video parameters. */
    public ImageToVideoParams build() {
      return new ImageToVideoParams(this);
    }
  }
}
