// Package grokimagine provides the Grok-Imagine multimodal generation API client.
//
//	client, err := grokimagine.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.TextToVideo.Run(ctx, grokimagine.TextToVideoParams{
//	    Model: grokimagine.ModelTextToVideo, Prompt: "A drone shot over a neon city",
//	})
package grokimagine

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/base"
	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	textToVideoPath  = "/api/v1/grok_imagine/text_to_video"
	imageToVideoPath = "/api/v1/grok_imagine/image_to_video"
	textToImagePath  = "/api/v1/grok_imagine/text_to_image"
	editImagePath    = "/api/v1/grok_imagine/edit_image"
	extensionsPath   = "/api/v1/grok_imagine/extend_video"
	upscalesPath     = "/api/v1/grok_imagine/upscale_image"
)

// Client is the Grok-Imagine multimodal generation API client.
type Client struct {
	base.Base
	TextToVideo  *TextToVideo
	ImageToVideo *ImageToVideo
	TextToImage  *TextToImage
	EditImage    *EditImage
	Extensions   *Extensions
	Upscales     *Upscales
}

// NewClient creates a Grok-Imagine client with the given options.
func NewClient(opts ...option.ClientOption) (*Client, error) {
	resolved, err := option.ResolveClientOptions(opts...)
	if err != nil {
		return nil, err
	}
	httpClient, err := core.NewHTTPClient(resolved)
	if err != nil {
		return nil, err
	}
	return NewClientWithHTTP(httpClient), nil
}

// NewClientWithHTTP creates a Grok-Imagine client with a pre-configured HTTP transport.
func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Base:         base.New(httpClient),
		TextToVideo:  &TextToVideo{http: httpClient},
		ImageToVideo: &ImageToVideo{http: httpClient},
		TextToImage:  &TextToImage{http: httpClient},
		EditImage:    &EditImage{http: httpClient},
		Extensions:   &Extensions{http: httpClient},
		Upscales:     &Upscales{http: httpClient},
	}
}

// TextToVideo generates videos from text prompts.
type TextToVideo struct{ http core.HTTPClient }

// Create submits a Grok Imagine text-to-video task and returns immediately with a task id.
func (r *TextToVideo) Create(ctx context.Context, params TextToVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["text-to-video"], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, textToVideoPath, body, requestOptions)
}

// Get fetches the current status of a Grok Imagine text-to-video task by id.
func (r *TextToVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(textToVideoPath, id), requestOptions)
}

// Run submits a Grok Imagine text-to-video task and polls until it completes.
func (r *TextToVideo) Run(ctx context.Context, params TextToVideoParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// ImageToVideo generates videos from reference images or a prior text-to-image task.
type ImageToVideo struct{ http core.HTTPClient }

// Create submits a Grok Imagine image-to-video task and returns immediately with a task id.
func (r *ImageToVideo) Create(ctx context.Context, params ImageToVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["image-to-video"], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, imageToVideoPath, body, requestOptions)
}

// Get fetches the current status of a Grok Imagine image-to-video task by id.
func (r *ImageToVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(imageToVideoPath, id), requestOptions)
}

// Run submits a Grok Imagine image-to-video task and polls until it completes.
func (r *ImageToVideo) Run(ctx context.Context, params ImageToVideoParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// TextToImage generates images from text prompts.
type TextToImage struct{ http core.HTTPClient }

// Create submits a Grok Imagine text-to-image task and returns immediately with a task id.
func (r *TextToImage) Create(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["text-to-image"], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, textToImagePath, body, requestOptions)
}

// Get fetches the current status of a Grok Imagine text-to-image task by id.
func (r *TextToImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(textToImagePath, id), requestOptions)
}

// Run submits a Grok Imagine text-to-image task and polls until it completes.
func (r *TextToImage) Run(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// EditImage applies prompt-guided edits to a source image.
type EditImage struct{ http core.HTTPClient }

// Create submits a Grok Imagine image editing task and returns immediately with a task id.
func (r *EditImage) Create(ctx context.Context, params EditImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["edit-image"], body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, editImagePath, body, requestOptions)
}

// Get fetches the current status of a Grok Imagine image editing task by id.
func (r *EditImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(editImagePath, id), requestOptions)
}

// Run submits a Grok Imagine image editing task and polls until it completes.
func (r *EditImage) Run(ctx context.Context, params EditImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// Extensions appends new footage to a previously generated Grok Imagine video, starting from a chosen timestamp.
type Extensions struct{ http core.HTTPClient }

// Create submits a Grok Imagine video extension task and returns immediately with a task id.
func (r *Extensions) Create(ctx context.Context, params ExtendParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, extensionsPath, core.CompactParams(params), requestOptions)
}

// Get fetches the current status of a Grok Imagine video extension task by id.
func (r *Extensions) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(extensionsPath, id), requestOptions)
}

// Run submits a Grok Imagine video extension task and polls until it completes.
func (r *Extensions) Run(ctx context.Context, params ExtendParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// Upscales increases the resolution of a previously generated Grok Imagine video.
type Upscales struct{ http core.HTTPClient }

// Create submits a Grok Imagine video upscale task and returns immediately with a task id.
func (r *Upscales) Create(ctx context.Context, params UpscaleParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscalesPath, core.CompactParams(params), requestOptions)
}

// Get fetches the current status of a Grok Imagine video upscale task by id.
func (r *Upscales) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(upscalesPath, id), requestOptions)
}

// Run submits a Grok Imagine video upscale task and polls until it completes.
func (r *Upscales) Run(ctx context.Context, params UpscaleParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
