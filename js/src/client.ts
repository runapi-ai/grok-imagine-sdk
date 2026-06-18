import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { TextToVideo } from './resources/text-to-video';
import { ImageToVideo } from './resources/image-to-video';
import { TextToImage } from './resources/text-to-image';
import { EditImage } from './resources/edit-image';
import { Extensions } from './resources/extensions';
import { Upscales } from './resources/upscales';

/**
 * Grok-Imagine multimodal generation API client.
 *
 * Image generation, video generation, image editing, video extension,
 * and video upscaling.
 *
 * @example
 * ```typescript
 * const client = new GrokImagineClient({
 *   apiKey: 'your-api-key',
 *   baseUrl: 'https://runapi.ai',
 * });
 *
 * const result = await client.textToVideo.run({
 *   model: 'grok-imagine-text-to-video',
 *   prompt: 'A drone shot over a neon cityscape at night',
 *   output_resolution: '720p',
 * });
 * ```
 */
export class GrokImagineClient extends BaseClient {
  /** Text-to-video generation. */
  public readonly textToVideo: TextToVideo;
  /** Image-to-video generation (external URLs or prior text-to-image task). */
  public readonly imageToVideo: ImageToVideo;
  /** Text-to-image generation. */
  public readonly textToImage: TextToImage;
  /** Prompt-guided image editing. */
  public readonly editImage: EditImage;
  /** Extend a previously generated video. */
  public readonly extensions: Extensions;
  /** Upscale a previously generated video. */
  public readonly upscales: Upscales;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.textToVideo = new TextToVideo(this.http);
    this.imageToVideo = new ImageToVideo(this.http);
    this.textToImage = new TextToImage(this.http);
    this.editImage = new EditImage(this.http);
    this.extensions = new Extensions(this.http);
    this.upscales = new Upscales(this.http);
  }
}
