// Package grokimagine provides the Grok-Imagine multimodal generation API client.
//
//	client, err := grokimagine.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.TextToVideos.Run(ctx, grokimagine.TextToVideoParams{
//	    Model: grokimagine.ModelTextToVideo, Prompt: "A drone shot over a neon city",
//	})
package grokimagine

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	textToVideosPath  = "/api/v1/grok_imagine/text_to_videos"
	imageToVideosPath = "/api/v1/grok_imagine/image_to_videos"
	textToImagesPath  = "/api/v1/grok_imagine/text_to_images"
	imageToImagesPath = "/api/v1/grok_imagine/image_to_images"
	extensionsPath    = "/api/v1/grok_imagine/extensions"
	upscalesPath      = "/api/v1/grok_imagine/upscales"
)

// Client is the Grok-Imagine multimodal generation API client.
type Client struct {
	TextToVideos  *TextToVideos
	ImageToVideos *ImageToVideos
	TextToImages  *TextToImages
	ImageToImages *ImageToImages
	Extensions    *Extensions
	Upscales      *Upscales
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
		TextToVideos:  &TextToVideos{http: httpClient},
		ImageToVideos: &ImageToVideos{http: httpClient},
		TextToImages:  &TextToImages{http: httpClient},
		ImageToImages: &ImageToImages{http: httpClient},
		Extensions:    &Extensions{http: httpClient},
		Upscales:      &Upscales{http: httpClient},
	}
}

// TextToVideos generates videos from text prompts.
type TextToVideos struct{ http core.HTTPClient }

func (r *TextToVideos) Create(ctx context.Context, params TextToVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, textToVideosPath, core.CompactParams(params), requestOptions)
}
func (r *TextToVideos) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(textToVideosPath, id), requestOptions)
}
func (r *TextToVideos) Run(ctx context.Context, params TextToVideoParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// ImageToVideos generates videos from reference images or a prior text-to-image task.
type ImageToVideos struct{ http core.HTTPClient }

func (r *ImageToVideos) Create(ctx context.Context, params ImageToVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, imageToVideosPath, core.CompactParams(params), requestOptions)
}
func (r *ImageToVideos) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(imageToVideosPath, id), requestOptions)
}
func (r *ImageToVideos) Run(ctx context.Context, params ImageToVideoParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// TextToImages generates images from text prompts.
type TextToImages struct{ http core.HTTPClient }

func (r *TextToImages) Create(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, textToImagesPath, core.CompactParams(params), requestOptions)
}
func (r *TextToImages) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(textToImagesPath, id), requestOptions)
}
func (r *TextToImages) Run(ctx context.Context, params TextToImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// ImageToImages re-styles a reference image.
type ImageToImages struct{ http core.HTTPClient }

func (r *ImageToImages) Create(ctx context.Context, params ImageToImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, imageToImagesPath, core.CompactParams(params), requestOptions)
}
func (r *ImageToImages) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(imageToImagesPath, id), requestOptions)
}
func (r *ImageToImages) Run(ctx context.Context, params ImageToImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// Extensions extend a prior grok-imagine video.
type Extensions struct{ http core.HTTPClient }

func (r *Extensions) Create(ctx context.Context, params ExtendParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, extensionsPath, core.CompactParams(params), requestOptions)
}
func (r *Extensions) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(extensionsPath, id), requestOptions)
}
func (r *Extensions) Run(ctx context.Context, params ExtendParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// Upscales upscale a prior grok-imagine video.
type Upscales struct{ http core.HTTPClient }

func (r *Upscales) Create(ctx context.Context, params UpscaleParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscalesPath, core.CompactParams(params), requestOptions)
}
func (r *Upscales) Get(ctx context.Context, id string, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[VideoTaskResponse](ctx, r.http, core.ResourcePath(upscalesPath, id), requestOptions)
}
func (r *Upscales) Run(ctx context.Context, params UpscaleParams, opts ...option.RequestOption) (*VideoTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*VideoTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
