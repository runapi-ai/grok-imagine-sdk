package ai.runapi.grokimagine.types;

import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.polling.AbstractTaskResponse;
import ai.runapi.core.polling.Poller;
import ai.runapi.core.polling.TaskStatus;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Response for segment-map operations. */
public class SegmentMapResponse extends AbstractTaskResponse implements Poller.CompletedResult {
  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private String status;

  @JsonProperty("error")
  private String error;

  @JsonProperty("segments")
  private List<Segment> segments;

  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** Returns the task ID. */
  public String getId() {
    return id;
  }

  /** Returns the current task status. */
  public TaskStatus getStatus() {
    return new TaskStatus(status == null ? "" : status);
  }

  /** Returns the task error message, if the task failed. */
  public String getError() {
    return error;
  }

  /** Returns the editable segment results, when present. */
  public List<Segment> getSegments() {
    return segments == null ? null : Collections.unmodifiableList(segments);
  }

  /** Returns unrecognized response fields preserved from the API response. */
  @JsonAnyGetter
  public Map<String, JsonNode> extraFields() {
    return Collections.unmodifiableMap(extraFields);
  }

  /** Ensures a completed response contains editable segments. */
  public void ensureResultPresent() {
    if (segments == null) {
      throw new ValidationException("completed task response is missing segments");
    }
  }

  @JsonAnySetter
  void putExtraField(String name, JsonNode value) {
    extraFields.put(name, value);
  }
}
