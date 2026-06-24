package ai.runapi.grokimagine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.grokimagine.types.CompletedTextToImageResponse;
import ai.runapi.grokimagine.types.TextToImageResponse;
import ai.runapi.grokimagine.types.CompletedEditImageResponse;
import ai.runapi.grokimagine.types.CompletedExtendVideoResponse;
import ai.runapi.grokimagine.types.CompletedImageToVideoResponse;
import ai.runapi.grokimagine.types.CompletedTextToImageResponse;
import ai.runapi.grokimagine.types.CompletedTextToVideoResponse;
import ai.runapi.grokimagine.types.CompletedUpscaleImageResponse;
import ai.runapi.grokimagine.types.EditImageModel;
import ai.runapi.grokimagine.types.EditImageParams;
import ai.runapi.grokimagine.types.EditImageResponse;
import ai.runapi.grokimagine.types.ExtendParams;
import ai.runapi.grokimagine.types.ExtendVideoResponse;
import ai.runapi.grokimagine.types.ImageToVideoModel;
import ai.runapi.grokimagine.types.ImageToVideoParams;
import ai.runapi.grokimagine.types.ImageToVideoResponse;
import ai.runapi.grokimagine.types.TextToImageModel;
import ai.runapi.grokimagine.types.TextToImageParams;
import ai.runapi.grokimagine.types.TextToImageResponse;
import ai.runapi.grokimagine.types.TextToVideoModel;
import ai.runapi.grokimagine.types.TextToVideoParams;
import ai.runapi.grokimagine.types.TextToVideoResponse;
import ai.runapi.grokimagine.types.UpscaleImageResponse;
import ai.runapi.grokimagine.types.UpscaleParams;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class GrokImagineClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    GrokImagineClient client = GrokImagineClient.builder().apiKey("sk-test").build();

    assertNotNull(client.textToImage());
    assertNotNull(client.files());
    assertNotNull(client.account());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new TextToImageModel("grok-imagine-text-to-image"));

    assertEquals("\"grok-imagine-text-to-image\"", json);
    assertEquals(new TextToImageModel("grok-imagine-text-to-image"), Json.mapper().readValue(json, TextToImageModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    GrokImagineClient client = GrokImagineClient.builder().apiKey("sk-test").transport(transport).build();

    client.textToImage().create(
        TextToImageParams.builder()
            .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
            .prompt("A small red cube on a plain white table, studio product photo")
            .aspectRatio("2:3")
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/grok_imagine/text_to_image", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);
  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    GrokImagineClient client = GrokImagineClient.builder().apiKey("sk-test").transport(transport).build();

    TextToImageResponse response = client.textToImage().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/grok_imagine/text_to_image/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    GrokImagineClient client = GrokImagineClient.builder().apiKey("sk-test").transport(transport).build();

    CompletedTextToImageResponse response = client.textToImage().run(
        TextToImageParams.builder()
            .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
            .prompt("A small red cube on a plain white table, studio product photo")
            .aspectRatio("2:3")
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    GrokImagineClient client = GrokImagineClient.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.textToImage().run(
                TextToImageParams.builder()
                    .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
                    .prompt("A small red cube on a plain white table, studio product photo")
                    .aspectRatio("2:3")
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversEditimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_edit_image\",\"status\":\"processing\"}");
      GrokImagineClient createClient = GrokImagineClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.editImage().create(
              EditImageParams.builder()
                  .model(EditImageModel.GROK_IMAGINE_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_edit_image_options\",\"status\":\"processing\"}");
      GrokImagineClient createWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.editImage().create(
              EditImageParams.builder()
                  .model(EditImageModel.GROK_IMAGINE_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_edit_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getClient = GrokImagineClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.editImage().get("task_edit_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_edit_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.editImage().get("task_edit_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_edit_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_edit_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runClient = GrokImagineClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedEditImageResponse runResponse = runClient.editImage().run(
              EditImageParams.builder()
                  .model(EditImageModel.GROK_IMAGINE_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_edit_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_edit_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.editImage().run(
              EditImageParams.builder()
                  .model(EditImageModel.GROK_IMAGINE_EDIT_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversExtensionsResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_extend\",\"status\":\"processing\"}");
      GrokImagineClient createClient = GrokImagineClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.extensions().create(
              ExtendParams.builder()
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_extend_options\",\"status\":\"processing\"}");
      GrokImagineClient createWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.extensions().create(
              ExtendParams.builder()
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_extend\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getClient = GrokImagineClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.extensions().get("task_extend"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_extend_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.extensions().get("task_extend_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_extend_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_extend_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runClient = GrokImagineClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedExtendVideoResponse runResponse = runClient.extensions().run(
              ExtendParams.builder()
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_extend_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_extend_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.extensions().run(
              ExtendParams.builder()
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversImagetovideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_image_to_video\",\"status\":\"processing\"}");
      GrokImagineClient createClient = GrokImagineClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.imageToVideo().create(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.GROK_IMAGINE_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_image_to_video_options\",\"status\":\"processing\"}");
      GrokImagineClient createWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.imageToVideo().create(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.GROK_IMAGINE_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_image_to_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getClient = GrokImagineClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.imageToVideo().get("task_image_to_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_image_to_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.imageToVideo().get("task_image_to_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_image_to_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_image_to_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runClient = GrokImagineClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedImageToVideoResponse runResponse = runClient.imageToVideo().run(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.GROK_IMAGINE_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_image_to_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_image_to_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.imageToVideo().run(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.GROK_IMAGINE_IMAGE_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTexttoimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_text_to_image\",\"status\":\"processing\"}");
      GrokImagineClient createClient = GrokImagineClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.textToImage().create(
              TextToImageParams.builder()
                  .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("2:3")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_image_options\",\"status\":\"processing\"}");
      GrokImagineClient createWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.textToImage().create(
              TextToImageParams.builder()
                  .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("2:3")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_text_to_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getClient = GrokImagineClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.textToImage().get("task_text_to_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.textToImage().get("task_text_to_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runClient = GrokImagineClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTextToImageResponse runResponse = runClient.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("2:3")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.textToImage().run(
              TextToImageParams.builder()
                  .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .aspectRatio("2:3")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTexttovideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_text_to_video\",\"status\":\"processing\"}");
      GrokImagineClient createClient = GrokImagineClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.textToVideo().create(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.GROK_IMAGINE_TEXT_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_video_options\",\"status\":\"processing\"}");
      GrokImagineClient createWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.textToVideo().create(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.GROK_IMAGINE_TEXT_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_text_to_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getClient = GrokImagineClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.textToVideo().get("task_text_to_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.textToVideo().get("task_text_to_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runClient = GrokImagineClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTextToVideoResponse runResponse = runClient.textToVideo().run(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.GROK_IMAGINE_TEXT_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.textToVideo().run(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.GROK_IMAGINE_TEXT_TO_VIDEO)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversUpscalesResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_upscale_image\",\"status\":\"processing\"}");
      GrokImagineClient createClient = GrokImagineClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.upscales().create(
              UpscaleParams.builder()
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_image_options\",\"status\":\"processing\"}");
      GrokImagineClient createWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.upscales().create(
              UpscaleParams.builder()
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_upscale_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getClient = GrokImagineClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.upscales().get("task_upscale_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient getWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.upscales().get("task_upscale_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runClient = GrokImagineClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedUpscaleImageResponse runResponse = runClient.upscales().run(
              UpscaleParams.builder()
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      GrokImagineClient runWithOptionsClient = GrokImagineClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.upscales().run(
              UpscaleParams.builder()
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}
