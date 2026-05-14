// Package grokimagine provides the Grok-Imagine multimodal generation API client.
package grokimagine

// TextToVideoModel identifies the Grok-Imagine text-to-video model.
type TextToVideoModel string

// ImageToVideoModel identifies the Grok-Imagine image-to-video model.
type ImageToVideoModel string

// TextToImageModel identifies the Grok-Imagine text-to-image model.
type TextToImageModel string

// ImageToImageModel identifies the Grok-Imagine image-to-image model.
type ImageToImageModel string

const (
	ModelTextToVideo  TextToVideoModel  = "grok-imagine-text-to-video"
	ModelImageToVideo ImageToVideoModel = "grok-imagine-image-to-video"
	ModelTextToImage  TextToImageModel  = "grok-imagine-text-to-image"
	ModelImageToImage ImageToImageModel = "grok-imagine-image-to-image"
)

// Video contains a generated video URL.
type Video struct {
	URL string `json:"url"`
}

// Image contains a generated image URL.
type Image struct {
	URL string `json:"url"`
}

// AsyncTaskResponse is the base response for async Grok-Imagine tasks.
type AsyncTaskResponse struct {
	ID     string `json:"id"`
	Status string `json:"status"`
	Error  string `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return r.Status }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

// VideoTaskResponse is returned when polling a video generation task.
type VideoTaskResponse struct {
	AsyncTaskResponse
	Videos []Video `json:"videos,omitempty"`
}

// ImageTaskResponse is returned when polling an image generation task.
type ImageTaskResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

// TextToVideoParams contains parameters for text-to-video generation.
type TextToVideoParams struct {
	Model       TextToVideoModel `json:"model" help:"required; must be grok-imagine-text-to-video"`
	Prompt      string           `json:"prompt" help:"required; up to 5000 characters"`
	CallbackURL string           `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	AspectRatio string `json:"aspect_ratio,omitempty" help:"optional; 2:3 (default), 3:2, 1:1, 16:9, 9:16"`
	Mode        string `json:"mode,omitempty" help:"optional; fun, normal (default), or spicy"`
	Duration    *int   `json:"duration,omitempty" help:"optional; video duration in seconds, integer 6-30"`
	Resolution  string `json:"resolution,omitempty" help:"optional; 480p (default) or 720p"`
	NSFWChecker *bool  `json:"nsfw_checker,omitempty" help:"optional; content filtering, default false"`
}

// ImageToVideoParams contains parameters for image-to-video generation.
// Provide either ImageURLs OR TaskID, never both.
type ImageToVideoParams struct {
	Model       ImageToVideoModel `json:"model" help:"required; must be grok-imagine-image-to-video"`
	CallbackURL string            `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	ImageURLs []string `json:"image_urls,omitempty" help:"optional; exactly one reference image URL (mutually exclusive with task_id)"`
	TaskID    string   `json:"task_id,omitempty" help:"optional; prior text-to-image task id (mutually exclusive with image_urls)"`
	Index     *int     `json:"index,omitempty" help:"optional; 0-5, selects image when using task_id"`

	Prompt      string `json:"prompt,omitempty" help:"optional; text description of desired motion"`
	Mode        string `json:"mode,omitempty" help:"optional; fun, normal (default), or spicy (spicy not available with image_urls)"`
	Duration    *int   `json:"duration,omitempty" help:"optional; video duration in seconds, integer 6-30"`
	Resolution  string `json:"resolution,omitempty" help:"optional; 480p (default) or 720p"`
	AspectRatio string `json:"aspect_ratio,omitempty" help:"optional; 2:3, 3:2, 1:1, 16:9 (default), 9:16"`
	NSFWChecker *bool  `json:"nsfw_checker,omitempty" help:"optional; content filtering, default false"`
}

// TextToImageParams contains parameters for text-to-image generation.
type TextToImageParams struct {
	Model       TextToImageModel `json:"model" help:"required; must be grok-imagine-text-to-image"`
	Prompt      string           `json:"prompt" help:"required; up to 5000 characters"`
	CallbackURL string           `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	AspectRatio string `json:"aspect_ratio,omitempty" help:"optional; 2:3, 3:2, 1:1 (default), 16:9, 9:16"`
	NSFWChecker *bool  `json:"nsfw_checker,omitempty" help:"optional; content filtering, default false"`
	EnablePro   *bool  `json:"enable_pro,omitempty" help:"optional; quality mode, slower but higher precision, default false"`
}

// ImageToImageParams contains parameters for image-to-image generation.
type ImageToImageParams struct {
	Model       ImageToImageModel `json:"model" help:"required; must be grok-imagine-image-to-image"`
	ImageURLs   []string          `json:"image_urls" help:"required; exactly one reference image URL"`
	CallbackURL string            `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	Prompt      string `json:"prompt,omitempty" help:"optional; text description of desired output"`
	NSFWChecker *bool  `json:"nsfw_checker,omitempty" help:"optional; content filtering, default false"`
}

// ExtendParams contains parameters for extending a prior grok-imagine video task.
type ExtendParams struct {
	TaskID       string `json:"task_id" help:"required; prior grok-imagine video task id"`
	Prompt       string `json:"prompt" help:"required; describes the motion for the extended segment"`
	ExtendAt     string `json:"extend_at" help:"required; seconds into the original video to begin extension"`
	ExtendTimes  string `json:"extend_times" help:"required; extension duration in seconds, 6 or 10"`
	CallbackURL  string `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`
}

// UpscaleParams contains parameters for upscaling a prior grok-imagine video task.
type UpscaleParams struct {
	TaskID      string `json:"task_id" help:"required; prior grok-imagine video task id"`
	CallbackURL string `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`
}
