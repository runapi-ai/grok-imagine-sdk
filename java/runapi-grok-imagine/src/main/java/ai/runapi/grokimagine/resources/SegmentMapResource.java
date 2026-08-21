package ai.runapi.grokimagine.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.grokimagine.types.CompletedSegmentMapResponse;
import ai.runapi.grokimagine.types.SegmentMapParams;
import ai.runapi.grokimagine.types.SegmentMapResponse;

/** Image 2.0 segment-map operations. */
public final class SegmentMapResource extends GrokimagineResource {
  /** API endpoint path for segment-map operations. */
  public static final String ENDPOINT = "/api/v1/grok_imagine/segment_map";

  /** Creates a resource bound to the supplied transport and client options. */
  public SegmentMapResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a segment-map task. */
  public TaskCreateResponse create(SegmentMapParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a segment-map task with per-request options. */
  public TaskCreateResponse create(SegmentMapParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a segment-map task by ID. */
  public SegmentMapResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a segment-map task by ID with per-request options. */
  public SegmentMapResponse get(String id, RequestOptions options) {
    return getTask(id, options, SegmentMapResponse.class);
  }

  /** Creates a segment-map task and polls until it completes. */
  public CompletedSegmentMapResponse run(SegmentMapParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a segment-map task with per-request options and polls until it completes. */
  public CompletedSegmentMapResponse run(SegmentMapParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, SegmentMapResponse.class, CompletedSegmentMapResponse.class);
  }
}
