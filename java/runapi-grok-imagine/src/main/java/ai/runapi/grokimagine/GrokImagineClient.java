package ai.runapi.grokimagine;

import ai.runapi.core.BaseClient;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import ai.runapi.grokimagine.resources.EditImageResource;
import ai.runapi.grokimagine.resources.ExtensionsResource;
import ai.runapi.grokimagine.resources.ImageToVideoResource;
import ai.runapi.grokimagine.resources.TextToImageResource;
import ai.runapi.grokimagine.resources.TextToVideoResource;
import ai.runapi.grokimagine.resources.UpscalesResource;

/** GrokImagine model-family Java SDK client. */
public final class GrokImagineClient extends BaseClient {
  private final EditImageResource editImage;
  private final ExtensionsResource extensions;
  private final ImageToVideoResource imageToVideo;
  private final TextToImageResource textToImage;
  private final TextToVideoResource textToVideo;
  private final UpscalesResource upscales;

  private GrokImagineClient(ClientOptions options) {
    super(options);
    this.editImage = new EditImageResource(transport(), options());
    this.extensions = new ExtensionsResource(transport(), options());
    this.imageToVideo = new ImageToVideoResource(transport(), options());
    this.textToImage = new TextToImageResource(transport(), options());
    this.textToVideo = new TextToVideoResource(transport(), options());
    this.upscales = new UpscalesResource(transport(), options());
  }

  /** Creates a new GrokImagineClient builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Edit Image operations. */
  public EditImageResource editImage() {
    return editImage;
  }

  /** Extensions operations. */
  public ExtensionsResource extensions() {
    return extensions;
  }

  /** Image To Video operations. */
  public ImageToVideoResource imageToVideo() {
    return imageToVideo;
  }

  /** Text To Image operations. */
  public TextToImageResource textToImage() {
    return textToImage;
  }

  /** Text To Video operations. */
  public TextToVideoResource textToVideo() {
    return textToVideo;
  }

  /** Upscales operations. */
  public UpscalesResource upscales() {
    return upscales;
  }

  /** Builder for {@link GrokImagineClient}. */
  public static final class Builder extends BaseClient.Builder<Builder> {
    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    @Override
    public Builder apiKey(String value) {
      return super.apiKey(value);
    }

    /** Sets the RunAPI base URL. If omitted, the SDK reads {@code RUNAPI_BASE_URL}. */
    @Override
    public Builder baseUrl(String value) {
      return super.baseUrl(value);
    }

    /** Sets the RunAPI base URL from a URI. */
    @Override
    public Builder baseUrl(URI value) {
      return super.baseUrl(value);
    }

    /** Sets a custom HTTP transport. User-provided transports are not closed by SDK clients. */
    @Override
    public Builder transport(HttpTransport value) {
      return super.transport(value);
    }

    /** Builds an immutable GrokImagineClient. */
    @Override
    public GrokImagineClient build() {
      return new GrokImagineClient(options.build());
    }
  }
}
