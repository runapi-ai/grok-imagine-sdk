import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineImageResponse,
  GrokImagineImageResponse,
  GrokImagineTextToImageParams,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/text_to_image';

/** Generates images from text prompts. */
export class TextToImage {
  constructor(private readonly http: HttpClient) {}

  /**
   * Generate an image and wait until complete.
   * @param params Text-to-image parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed image with results.
   */
  async run(
    params: GrokImagineTextToImageParams,
    options?: RequestOptions & PollingOptions
  ): Promise<CompletedGrokImagineImageResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<GrokImagineImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedGrokImagineImageResponse;
  }

  /**
   * Create a text-to-image task; returns immediately with a task id.
   * @param params Text-to-image parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: GrokImagineTextToImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  /**
   * Fetch the current status of a text-to-image task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current image task status.
   */
  async get(id: string, options?: RequestOptions): Promise<GrokImagineImageResponse> {
    return this.http.request<GrokImagineImageResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
