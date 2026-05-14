import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { TextToVideos } from './resources/text-to-videos';
import { ImageToVideos } from './resources/image-to-videos';
import { TextToImages } from './resources/text-to-images';
import { ImageToImages } from './resources/image-to-images';
import { Extensions } from './resources/extensions';
import { Upscales } from './resources/upscales';

/**
 * Grok-Imagine multimodal generation API client.
 *
 * @example
 * ```typescript
 * const client = new GrokImagineClient({
 *   apiKey: 'your-api-key',
 *   baseUrl: 'https://runapi.ai',
 * });
 *
 * const result = await client.textToVideos.run({
 *   model: 'grok-imagine-text-to-video',
 *   prompt: 'A drone shot over a neon cityscape at night',
 *   resolution: '720p',
 * });
 * ```
 */
export class GrokImagineClient {
  /** Text-to-video generation. */
  public readonly textToVideos: TextToVideos;
  /** Image-to-video generation (external URLs or prior text-to-image task). */
  public readonly imageToVideos: ImageToVideos;
  /** Text-to-image generation. */
  public readonly textToImages: TextToImages;
  /** Image-to-image re-stylization. */
  public readonly imageToImages: ImageToImages;
  /** Extend a previously generated video. */
  public readonly extensions: Extensions;
  /** Upscale a previously generated video. */
  public readonly upscales: Upscales;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.textToVideos = new TextToVideos(http);
    this.imageToVideos = new ImageToVideos(http);
    this.textToImages = new TextToImages(http);
    this.imageToImages = new ImageToImages(http);
    this.extensions = new Extensions(http);
    this.upscales = new Upscales(http);
  }
}
