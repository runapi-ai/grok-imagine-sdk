package ai.runapi.grokimagine.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.grokimagine.types.CompletedUpscaleImageResponse;
import ai.runapi.grokimagine.types.UpscaleParams;
import ai.runapi.grokimagine.types.UpscaleImageResponse;

/** Upscales operations. */
public final class UpscalesResource extends GrokimagineResource {
  /** API endpoint path for upscale image operations. */
  public static final String ENDPOINT = "/api/v1/grok_imagine/upscale_image";

  /** Creates a resource bound to the supplied transport and client options. */
  public UpscalesResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a upscale image task. */
  public TaskCreateResponse create(UpscaleParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a upscale image task with per-request options. */
  public TaskCreateResponse create(UpscaleParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a upscale image task by ID. */
  public UpscaleImageResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a upscale image task by ID with per-request options. */
  public UpscaleImageResponse get(String id, RequestOptions options) {
    return getTask(id, options, UpscaleImageResponse.class);
  }

  /** Creates a upscale image task and polls until it completes. */
  public CompletedUpscaleImageResponse run(UpscaleParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a upscale image task with per-request options and polls until it completes. */
  public CompletedUpscaleImageResponse run(UpscaleParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, UpscaleImageResponse.class, CompletedUpscaleImageResponse.class);
  }
}
