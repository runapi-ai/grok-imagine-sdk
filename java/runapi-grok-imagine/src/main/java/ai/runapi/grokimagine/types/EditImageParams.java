package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for edit image operations. */
public final class EditImageParams {
  private final String model;
  private final String sourceImageUrl;
  private final String callbackUrl;
  private final String prompt;
  private final Boolean enableSafetyChecker;

  private EditImageParams(Builder builder) {
    this.model = builder.model;
    this.sourceImageUrl = builder.sourceImageUrl;
    this.callbackUrl = builder.callbackUrl;
    this.prompt = builder.prompt;
    this.enableSafetyChecker = builder.enableSafetyChecker;
  }

  /** Creates a new EditImageParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/edit-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", GrokimagineParamUtils.wireValue(model));
    raw.put("source_image_url", GrokimagineParamUtils.wireValue(sourceImageUrl));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    raw.put("prompt", GrokimagineParamUtils.wireValue(prompt));
    raw.put("enable_safety_checker", GrokimagineParamUtils.wireValue(enableSafetyChecker));
    return GrokimagineParamUtils.compact(raw);
  }



  /** Builder for {@link EditImageParams}. */
  public static final class Builder {
    private String model;
    private String sourceImageUrl;
    private String callbackUrl;
    private String prompt;
    private Boolean enableSafetyChecker;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(EditImageModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = GrokimagineParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the source image URL. */
    public Builder sourceImageUrl(String value) {
      this.sourceImageUrl = GrokimagineParamUtils.requireNonBlank(value, "sourceImageUrl");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = GrokimagineParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = GrokimagineParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the content safety checker toggle. */
    public Builder enableSafetyChecker(boolean value) {
      this.enableSafetyChecker = value;
      return this;
    }

    /** Builds immutable edit image parameters. */
    public EditImageParams build() {
      return new EditImageParams(this);
    }
  }
}
