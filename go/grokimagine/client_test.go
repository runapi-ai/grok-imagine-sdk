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
	_, err := client.TextToVideo.Create(context.Background(), TextToVideoParams{
		Model:      ModelTextToVideo,
		Prompt:     "A drone shot over a neon city",
		Resolution: "720p",
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
	if body["resolution"] != "720p" {
		t.Fatalf("unexpected resolution: %v", body["resolution"])
	}
}

func TestImageToVideoWithTaskID(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.ImageToVideo.Create(context.Background(), ImageToVideoParams{
		Model:  ModelImageToVideo,
		TaskID: "prior_t2i_task",
	})
	if err != nil {
		t.Fatal(err)
	}
	body := stub.body.(map[string]any)
	if body["task_id"] != "prior_t2i_task" {
		t.Fatalf("unexpected task_id: %v", body["task_id"])
	}
	if _, ok := body["image_urls"]; ok {
		t.Fatal("expected empty image_urls to be compacted away")
	}
}

func TestExtensionsCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.Extensions.Create(context.Background(), ExtendParams{
		TaskID:      "prior_video_task",
		Prompt:      "Continue the scene",
		ExtendAt:    "0",
		ExtendTimes: "6",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.path != "/api/v1/grok_imagine/extend_video" {
		t.Fatalf("unexpected path: %s", stub.path)
	}
	body := stub.body.(map[string]any)
	if body["extend_times"] != "6" {
		t.Fatalf("unexpected extend_times: %v", body["extend_times"])
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
