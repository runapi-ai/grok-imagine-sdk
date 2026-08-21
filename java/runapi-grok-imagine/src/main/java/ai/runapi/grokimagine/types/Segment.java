package ai.runapi.grokimagine.types;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Editable segment-map result metadata. */
public final class Segment {
  @JsonProperty("url")
  private String url;

  @JsonProperty("name")
  private String name;

  @JsonProperty("index")
  private Integer index;

  private final Map<String, JsonNode> extraFields = new LinkedHashMap<String, JsonNode>();

  /** Returns the segment URL. */
  public String getUrl() {
    return url;
  }

  /** Returns the segment label, when present. */
  public String getName() {
    return name;
  }

  /** Returns the one-based segment index, when present. */
  public Integer getIndex() {
    return index;
  }

  /** Returns unrecognized response fields preserved from the API response. */
  @JsonAnyGetter
  public Map<String, JsonNode> extraFields() {
    return Collections.unmodifiableMap(extraFields);
  }

  @JsonAnySetter
  void putExtraField(String name, JsonNode value) {
    extraFields.put(name, value);
  }
}
