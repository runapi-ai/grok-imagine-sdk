package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for extend video operations. */
public final class ExtendParams {
  private final String sourceTaskId;
  private final String prompt;
  private final Integer startSeconds;
  private final Integer extensionDurationSeconds;
  private final String callbackUrl;

  private ExtendParams(Builder builder) {
    this.sourceTaskId = builder.sourceTaskId;
    this.prompt = builder.prompt;
    this.startSeconds = builder.startSeconds;
    this.extensionDurationSeconds = builder.extensionDurationSeconds;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new ExtendParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/extend";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("source_task_id", GrokimagineParamUtils.wireValue(sourceTaskId));
    raw.put("prompt", GrokimagineParamUtils.wireValue(prompt));
    raw.put("start_seconds", GrokimagineParamUtils.wireValue(startSeconds));
    raw.put("extension_duration_seconds", GrokimagineParamUtils.wireValue(extensionDurationSeconds));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    return GrokimagineParamUtils.compact(raw);
  }



  /** Builder for {@link ExtendParams}. */
  public static final class Builder {
    private String sourceTaskId;
    private String prompt;
    private Integer startSeconds;
    private Integer extensionDurationSeconds;
    private String callbackUrl;

    private Builder() {}

    /** Sets the source task ID. */
    public Builder sourceTaskId(String value) {
      this.sourceTaskId = GrokimagineParamUtils.requireNonBlank(value, "sourceTaskId");
      return this;
    }

    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = GrokimagineParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the start seconds. */
    public Builder startSeconds(int value) {
      this.startSeconds = value;
      return this;
    }

    /** Sets the extension duration seconds. */
    public Builder extensionDurationSeconds(int value) {
      this.extensionDurationSeconds = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = GrokimagineParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable extend video parameters. */
    public ExtendParams build() {
      return new ExtendParams(this);
    }
  }
}
