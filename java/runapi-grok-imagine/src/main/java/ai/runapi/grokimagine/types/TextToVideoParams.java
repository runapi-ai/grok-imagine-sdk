package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for text to video operations. */
public final class TextToVideoParams {
  private final String model;
  private final String prompt;
  private final String callbackUrl;
  private final String aspectRatio;
  private final String motionStyle;
  private final Integer durationSeconds;
  private final String outputResolution;
  private final Boolean enableSafetyChecker;

  private TextToVideoParams(Builder builder) {
    this.model = builder.model;
    this.prompt = builder.prompt;
    this.callbackUrl = builder.callbackUrl;
    this.aspectRatio = builder.aspectRatio;
    this.motionStyle = builder.motionStyle;
    this.durationSeconds = builder.durationSeconds;
    this.outputResolution = builder.outputResolution;
    this.enableSafetyChecker = builder.enableSafetyChecker;
  }

  /** Creates a new TextToVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/text-to-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", GrokimagineParamUtils.wireValue(model));
    raw.put("prompt", GrokimagineParamUtils.wireValue(prompt));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    raw.put("aspect_ratio", GrokimagineParamUtils.wireValue(aspectRatio));
    raw.put("motion_style", GrokimagineParamUtils.wireValue(motionStyle));
    raw.put("duration_seconds", GrokimagineParamUtils.wireValue(durationSeconds));
    raw.put("output_resolution", GrokimagineParamUtils.wireValue(outputResolution));
    raw.put("enable_safety_checker", GrokimagineParamUtils.wireValue(enableSafetyChecker));
    return GrokimagineParamUtils.compact(raw);
  }



  /** Builder for {@link TextToVideoParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String callbackUrl;
    private String aspectRatio;
    private String motionStyle;
    private Integer durationSeconds;
    private String outputResolution;
    private Boolean enableSafetyChecker;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(TextToVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = GrokimagineParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = GrokimagineParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = GrokimagineParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Sets the output aspect ratio. */
    public Builder aspectRatio(String value) {
      this.aspectRatio = GrokimagineParamUtils.requireNonBlank(value, "aspectRatio");
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

    /** Sets the content safety checker toggle. */
    public Builder enableSafetyChecker(boolean value) {
      this.enableSafetyChecker = value;
      return this;
    }

    /** Builds immutable text to video parameters. */
    public TextToVideoParams build() {
      return new TextToVideoParams(this);
    }
  }
}
