package grokimagine

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method string
	path   string
	body   any
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
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
