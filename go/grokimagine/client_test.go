package grokimagine

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method   string
	path     string
	body     any
	response json.RawMessage
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
	}
	if s.response != nil {
		return s.response, nil
	}
	return json.RawMessage(`{"id":"task_123","status":"processing"}`), nil
}

func TestTextToVideoCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	enableSafetyChecker := true
	_, err := client.TextToVideo.Create(context.Background(), TextToVideoParams{
		Model:               ModelTextToVideo,
		Prompt:              "A drone shot over a neon city",
		MotionStyle:         "fun",
		OutputResolution:    "720p",
		EnableSafetyChecker: &enableSafetyChecker,
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/grok_imagine/text_to_video" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "grok-imagine-text-to-video" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if body["output_resolution"] != "720p" {
		t.Fatalf("unexpected output_resolution: %v", body["output_resolution"])
	}
	if body["motion_style"] != "fun" {
		t.Fatalf("unexpected motion_style: %v", body["motion_style"])
	}
	if _, ok := body["mode"]; ok {
		t.Fatal("expected mode to stay off the public request body")
	}
	if body["enable_safety_checker"] != true {
		t.Fatalf("unexpected enable_safety_checker: %v", body["enable_safety_checker"])
	}
}

func TestTextToVideoPreviewCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	duration := 15
	_, err := client.TextToVideo.Create(context.Background(), TextToVideoParams{
		Model:            ModelTextToVideo15Preview,
		Prompt:           "A quiet city rain scene",
		AspectRatio:      "auto",
		DurationSeconds:  &duration,
		OutputResolution: "720p",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "grok-imagine-video-1.5-preview" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if body["aspect_ratio"] != "auto" {
		t.Fatalf("unexpected aspect_ratio: %v", body["aspect_ratio"])
	}
	if _, ok := body["motion_style"]; ok {
		t.Fatal("expected preview request to omit motion_style")
	}
	if _, ok := body["enable_safety_checker"]; ok {
		t.Fatal("expected preview request to omit enable_safety_checker")
	}
}

func TestTextToVideoFastCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	duration := 5
	_, err := client.TextToVideo.Create(context.Background(), TextToVideoParams{
		Model:              ModelTextToVideo15Fast,
		Prompt:             "A paper plane crossing a sunlit room",
		ReferenceImageURLs: []string{"https://cdn.runapi.ai/public/samples/result.png"},
		AspectRatio:        "16:9",
		DurationSeconds:    &duration,
		OutputResolution:   "720p",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "grok-imagine-video-1.5-fast" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if got := body["reference_image_urls"]; got == nil {
		t.Fatalf("expected reference_image_urls in body, got %#v", body)
	}
	if body["output_resolution"] != "720p" {
		t.Fatalf("unexpected output_resolution: %v", body["output_resolution"])
	}
}

func TestImageToVideoWithTaskID(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:        ModelImageToVideo,
		SourceTaskID: "prior_t2i_task",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["source_task_id"] != "prior_t2i_task" {
		t.Fatalf("unexpected source_task_id: %v", body["source_task_id"])
	}
	if _, ok := body["image_urls"]; ok {
		t.Fatal("expected empty image_urls to be compacted away")
	}
	if _, ok := body["task_id"]; ok {
		t.Fatal("expected task_id to stay off the public request body")
	}
}

func TestImageToVideoMotionStyle(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:        ModelImageToVideo,
		SourceTaskID: "prior_t2i_task",
		MotionStyle:  "spicy",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["motion_style"] != "spicy" {
		t.Fatalf("unexpected motion_style: %v", body["motion_style"])
	}
	if _, ok := body["mode"]; ok {
		t.Fatal("expected mode to stay off the public request body")
	}
}

func TestImageToVideoWithSourceImageURL(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:          ModelImageToVideo,
		SourceImageURL: "https://cdn.runapi.ai/public/samples/result.png",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if got := body["source_image_url"]; got != "https://cdn.runapi.ai/public/samples/result.png" {
		t.Fatalf("unexpected source_image_url: %v", got)
	}
	if _, ok := body["image_urls"]; ok {
		t.Fatal("expected image_urls to stay off the public request body")
	}
}

func TestImageToVideoPreviewWithSourceImageURL(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	duration := 8
	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:            ModelImageToVideo15Preview,
		SourceImageURL:   "https://cdn.runapi.ai/public/samples/result.png",
		Prompt:           "Animate the still image",
		AspectRatio:      "auto",
		DurationSeconds:  &duration,
		OutputResolution: "720p",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "grok-imagine-video-1.5-preview" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if got := body["source_image_url"]; got != "https://cdn.runapi.ai/public/samples/result.png" {
		t.Fatalf("unexpected source_image_url: %v", got)
	}
	if _, ok := body["source_task_id"]; ok {
		t.Fatal("expected preview request to omit source_task_id")
	}
	if _, ok := body["motion_style"]; ok {
		t.Fatal("expected preview request to omit motion_style")
	}
}

func TestImageToVideoFastWithSourceAndReferenceImages(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	duration := 21
	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:              ModelImageToVideo15Fast,
		SourceImageURL:     "https://cdn.runapi.ai/public/samples/result.png",
		ReferenceImageURLs: []string{"https://cdn.runapi.ai/public/samples/reference.png"},
		Prompt:             "Animate the still image",
		AspectRatio:        "3:2",
		DurationSeconds:    &duration,
		OutputResolution:   "720p",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "grok-imagine-video-1.5-fast" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if got := body["source_image_url"]; got != "https://cdn.runapi.ai/public/samples/result.png" {
		t.Fatalf("unexpected source_image_url: %v", got)
	}
	if got := body["reference_image_urls"]; got == nil {
		t.Fatalf("expected reference_image_urls in body, got %#v", body)
	}
	if _, ok := body["source_task_id"]; ok {
		t.Fatal("expected fast request to omit source_task_id")
	}
}

func TestEditImageCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.EditImage.Create(context.Background(), EditImageParams{
		Model:          ModelEditImage,
		SourceImageURL: "https://cdn.runapi.ai/public/samples/source.png",
		Prompt:         "Restyle",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/grok_imagine/edit_image" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "grok-imagine-edit-image" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if body["source_image_url"] != "https://cdn.runapi.ai/public/samples/source.png" {
		t.Fatalf("unexpected source_image_url: %v", body["source_image_url"])
	}
	if _, ok := body["image_urls"]; ok {
		t.Fatal("expected image_urls to stay off the public request body")
	}
}

func TestImage2SegmentMapAndEditCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.SegmentMap.Create(context.Background(), SegmentMapParams{
		Model:        ModelImage2SegmentMap,
		SourceTaskID: "image_2_text_task",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/grok_imagine/segment_map" {
		t.Fatalf("unexpected segment-map request: %s %s", stub.method, stub.path)
	}
	segmentBody := stub.body.(map[string]any)
	if segmentBody["model"] != "grok-imagine-image-2-0" || segmentBody["source_task_id"] != "image_2_text_task" {
		t.Fatalf("unexpected segment-map body: %#v", segmentBody)
	}

	_, err = client.SegmentMap.Create(context.Background(), SegmentMapParams{
		Model:    ModelImage2SegmentMap,
		ImageURL: "https://cdn.runapi.ai/public/samples/image.jpg",
	})
	if err != nil {
		t.Fatal(err)
	}
	segmentBody = stub.body.(map[string]any)
	if segmentBody["image_url"] != "https://cdn.runapi.ai/public/samples/image.jpg" {
		t.Fatalf("unexpected image URL segment-map body: %#v", segmentBody)
	}
	if _, ok := segmentBody["source_task_id"]; ok {
		t.Fatalf("expected source_task_id to be omitted for image URL input: %#v", segmentBody)
	}

	_, err = client.EditImage.Create(context.Background(), EditImageParams{
		Model: ModelImage2EditImage,
		SourceImageURLs: []string{
			"https://cdn.runapi.ai/public/samples/source.png",
			"https://cdn.runapi.ai/public/samples/reference.png",
		},
		AspectRatio: "16:9",
		Prompt:      "Replace the foreground",
	})
	if err != nil {
		t.Fatal(err)
	}
	editBody := stub.body.(map[string]any)
	if editBody["prompt"] != "Replace the foreground" || editBody["aspect_ratio"] != "16:9" {
		t.Fatalf("unexpected Image 2.0 edit body: %#v", editBody)
	}
	if got, ok := editBody["source_image_urls"].([]any); !ok || len(got) != 2 {
		t.Fatalf("unexpected Image 2.0 source_image_urls: %#v", editBody["source_image_urls"])
	}
	if _, ok := editBody["source_image_url"]; ok {
		t.Fatal("expected Image 2.0 edit to omit source_image_url")
	}
	if _, ok := editBody["source_task_id"]; ok {
		t.Fatal("expected Image 2.0 edit to omit source_task_id")
	}
	if _, ok := editBody["mask_indices"]; ok {
		t.Fatal("expected Image 2.0 edit to omit mask_indices")
	}
}

func TestImage2EditCreateAllowsPromptToBeOmitted(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.EditImage.Create(context.Background(), EditImageParams{
		Model:           ModelImage2EditImage,
		SourceImageURLs: []string{"https://cdn.runapi.ai/public/samples/source.png"},
		AspectRatio:     "1:1",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if _, ok := body["prompt"]; ok {
		t.Fatalf("expected optional prompt to be omitted: %#v", body)
	}
}

func TestImage2EditCreateValidatesDirectImageContract(t *testing.T) {
	tests := []struct {
		name            string
		sourceImageURLs []string
		aspectRatio     string
	}{
		{name: "requires a source image", aspectRatio: "1:1"},
		{
			name: "accepts at most five source images",
			sourceImageURLs: []string{
				"https://cdn.runapi.ai/public/samples/1.png",
				"https://cdn.runapi.ai/public/samples/2.png",
				"https://cdn.runapi.ai/public/samples/3.png",
				"https://cdn.runapi.ai/public/samples/4.png",
				"https://cdn.runapi.ai/public/samples/5.png",
				"https://cdn.runapi.ai/public/samples/6.png",
			},
			aspectRatio: "1:1",
		},
		{
			name:            "rejects an unsupported aspect ratio",
			sourceImageURLs: []string{"https://cdn.runapi.ai/public/samples/source.png"},
			aspectRatio:     "4:3",
		},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			stub := &stubHTTPClient{}
			client := NewClientWithHTTP(stub)
			_, err := client.EditImage.Create(context.Background(), EditImageParams{
				Model:           ModelImage2EditImage,
				SourceImageURLs: test.sourceImageURLs,
				AspectRatio:     test.aspectRatio,
			})
			if err == nil {
				t.Fatal("expected request validation to fail")
			}
			if stub.method != "" {
				t.Fatalf("expected validation before HTTP request, got method %q", stub.method)
			}
		})
	}
}

func TestSegmentMapGetDecodesSegments(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"segment_map_1","status":"completed","segments":[{"url":"https://file.runapi.ai/segment.png","name":"subject","index":1}]}`)}
	response, err := NewClientWithHTTP(stub).SegmentMap.Get(context.Background(), "segment_map_1")
	if err != nil {
		t.Fatal(err)
	}
	if len(response.Segments) != 1 || response.Segments[0].Name != "subject" || response.Segments[0].Index == nil || *response.Segments[0].Index != 1 {
		t.Fatalf("unexpected segments: %#v", response.Segments)
	}
}

func TestExtensionsCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.Extensions.Create(context.Background(), ExtendParams{
		SourceTaskID:             "prior_video_task",
		Prompt:                   "Continue the scene",
		StartSeconds:             0,
		ExtensionDurationSeconds: 6,
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.path != "/api/v1/grok_imagine/extend_video" {
		t.Fatalf("unexpected path: %s", stub.path)
	}
	body := stub.body.(map[string]any)
	if body["source_task_id"] != "prior_video_task" {
		t.Fatalf("unexpected source_task_id: %v", body["source_task_id"])
	}
	if body["start_seconds"] != float64(0) {
		t.Fatalf("unexpected start_seconds: %v", body["start_seconds"])
	}
	if body["extension_duration_seconds"] != float64(6) {
		t.Fatalf("unexpected extension_duration_seconds: %v", body["extension_duration_seconds"])
	}
}

func TestUpscalesGet(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.Upscales.Get(context.Background(), "task_upsc_123")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != "/api/v1/grok_imagine/upscale_image/task_upsc_123" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
}

func TestUpscalesCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.Upscales.Create(context.Background(), UpscaleParams{
		SourceTaskID: "prior_video_task",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["source_task_id"] != "prior_video_task" {
		t.Fatalf("unexpected source_task_id: %v", body["source_task_id"])
	}
	if _, ok := body["task_id"]; ok {
		t.Fatal("expected task_id to stay off the public request body")
	}
}
