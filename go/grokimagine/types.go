// Package grokimagine provides the Grok-Imagine multimodal generation API client.
package grokimagine

// TextToVideoModel identifies the Grok-Imagine text-to-video model.
type TextToVideoModel string

// ImageToVideoModel identifies the Grok-Imagine image-to-video model.
type ImageToVideoModel string

// TextToImageModel identifies the Grok-Imagine text-to-image model.
type TextToImageModel string

// EditImageModel identifies the Grok-Imagine image editing model.
type EditImageModel string

const (
	// ModelTextToVideo is the Grok Imagine text-to-video model.
	ModelTextToVideo TextToVideoModel = "grok-imagine-text-to-video"
	// ModelImageToVideo is the Grok Imagine image-to-video model.
	ModelImageToVideo ImageToVideoModel = "grok-imagine-image-to-video"
	// ModelTextToImage is the Grok Imagine text-to-image model, with an optional Pro quality mode.
	ModelTextToImage TextToImageModel = "grok-imagine-text-to-image"
	// ModelEditImage is the Grok Imagine image editing model for prompt-guided modifications to a source image.
	ModelEditImage EditImageModel = "grok-imagine-edit-image"
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
	Model       TextToVideoModel `json:"model" help:"required; model slug"`
	Prompt      string           `json:"prompt" help:"required; up to 5000 characters"`
	CallbackURL string           `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	AspectRatio         string `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	MotionStyle         string `json:"motion_style,omitempty" help:"optional; motion style preset"`
	DurationSeconds     *int   `json:"duration_seconds,omitempty" help:"optional; duration in seconds"`
	OutputResolution    string `json:"output_resolution,omitempty" help:"optional; output resolution"`
	EnableSafetyChecker *bool  `json:"enable_safety_checker,omitempty" help:"optional; content safety check toggle"`
}

// ImageToVideoParams contains parameters for image-to-video generation.
// Provide either SourceImageURLs OR SourceTaskID, never both.
type ImageToVideoParams struct {
	Model       ImageToVideoModel `json:"model" help:"required; model slug"`
	CallbackURL string            `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	SourceImageURLs []string `json:"source_image_urls,omitempty" help:"optional; exactly one source image URL (mutually exclusive with source_task_id)"`
	SourceTaskID    string   `json:"source_task_id,omitempty" help:"optional; prior text-to-image task id (mutually exclusive with source_image_urls)"`
	Index           *int     `json:"index,omitempty" help:"optional; 0-5, selects image when using source_task_id"`

	Prompt              string `json:"prompt,omitempty" help:"optional; text description of desired motion"`
	MotionStyle         string `json:"motion_style,omitempty" help:"optional; motion style preset"`
	DurationSeconds     *int   `json:"duration_seconds,omitempty" help:"optional; duration in seconds"`
	OutputResolution    string `json:"output_resolution,omitempty" help:"optional; output resolution"`
	AspectRatio         string `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	EnableSafetyChecker *bool  `json:"enable_safety_checker,omitempty" help:"optional; content safety check toggle"`
}

// TextToImageParams contains parameters for text-to-image generation.
type TextToImageParams struct {
	Model       TextToImageModel `json:"model" help:"required; model slug"`
	Prompt      string           `json:"prompt" help:"required; up to 5000 characters"`
	CallbackURL string           `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	AspectRatio         string `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	EnableSafetyChecker *bool  `json:"enable_safety_checker,omitempty" help:"optional; content safety check toggle"`
	EnablePro           *bool  `json:"enable_pro,omitempty" help:"optional; quality mode, slower but higher precision, default false"`
}

// EditImageParams contains parameters for prompt-guided image editing.
type EditImageParams struct {
	Model          EditImageModel `json:"model" help:"required; model slug"`
	SourceImageURL string         `json:"source_image_url" help:"required; source image URL"`
	CallbackURL    string         `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`

	Prompt              string `json:"prompt,omitempty" help:"optional; text description of desired output"`
	EnableSafetyChecker *bool  `json:"enable_safety_checker,omitempty" help:"optional; content safety check toggle"`
}

// ExtendParams contains parameters for extending a prior grok-imagine video task.
type ExtendParams struct {
	SourceTaskID             string `json:"source_task_id" help:"required; prior grok-imagine video task id"`
	Prompt                   string `json:"prompt" help:"required; describes the motion for the extended segment"`
	StartSeconds             int    `json:"start_seconds" help:"required; seconds into the source video to begin extension"`
	ExtensionDurationSeconds int    `json:"extension_duration_seconds" help:"required; extension duration in seconds"`
	CallbackURL              string `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`
}

// UpscaleParams contains parameters for upscaling a prior grok-imagine video task.
type UpscaleParams struct {
	SourceTaskID string `json:"source_task_id" help:"required; prior grok-imagine video task id"`
	CallbackURL  string `json:"callback_url,omitempty" help:"optional; URL that receives completion callback"`
}
