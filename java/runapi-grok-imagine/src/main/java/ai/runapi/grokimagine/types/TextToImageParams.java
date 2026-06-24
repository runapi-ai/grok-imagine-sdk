package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for text to image operations. */
public final class TextToImageParams {
  private final String model;
  private final String prompt;
  private final String callbackUrl;
  private final String aspectRatio;
  private final Boolean enableSafetyChecker;
  private final Boolean enablePro;

  private TextToImageParams(Builder builder) {
    this.model = builder.model;
    this.prompt = builder.prompt;
    this.callbackUrl = builder.callbackUrl;
    this.aspectRatio = builder.aspectRatio;
    this.enableSafetyChecker = builder.enableSafetyChecker;
    this.enablePro = builder.enablePro;
  }

  /** Creates a new TextToImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/text-to-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", GrokimagineParamUtils.wireValue(model));
    raw.put("prompt", GrokimagineParamUtils.wireValue(prompt));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    raw.put("aspect_ratio", GrokimagineParamUtils.wireValue(aspectRatio));
    raw.put("enable_safety_checker", GrokimagineParamUtils.wireValue(enableSafetyChecker));
    raw.put("enable_pro", GrokimagineParamUtils.wireValue(enablePro));
    return GrokimagineParamUtils.compact(raw);
  }



  /** Builder for {@link TextToImageParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String callbackUrl;
    private String aspectRatio;
    private Boolean enableSafetyChecker;
    private Boolean enablePro;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(TextToImageModel value) {
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

    /** Sets the content safety checker toggle. */
    public Builder enableSafetyChecker(boolean value) {
      this.enableSafetyChecker = value;
      return this;
    }

    /** Sets the enable pro. */
    public Builder enablePro(boolean value) {
      this.enablePro = value;
      return this;
    }

    /** Builds immutable text to image parameters. */
    public TextToImageParams build() {
      return new TextToImageParams(this);
    }
  }
}
