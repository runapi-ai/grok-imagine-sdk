package ai.runapi.grokimagine.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for upscale image operations. */
public final class UpscaleParams {
  private final String sourceTaskId;
  private final String callbackUrl;

  private UpscaleParams(Builder builder) {
    this.sourceTaskId = builder.sourceTaskId;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new UpscaleParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "grok-imagine/upscale-image";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("source_task_id", GrokimagineParamUtils.wireValue(sourceTaskId));
    raw.put("callback_url", GrokimagineParamUtils.wireValue(callbackUrl));
    return GrokimagineParamUtils.compact(raw);
  }



  /** Builder for {@link UpscaleParams}. */
  public static final class Builder {
    private String sourceTaskId;
    private String callbackUrl;

    private Builder() {}

    /** Sets the source task ID. */
    public Builder sourceTaskId(String value) {
      this.sourceTaskId = GrokimagineParamUtils.requireNonBlank(value, "sourceTaskId");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = GrokimagineParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable upscale image parameters. */
    public UpscaleParams build() {
      return new UpscaleParams(this);
    }
  }
}
