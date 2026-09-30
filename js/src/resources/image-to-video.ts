import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineVideoResponse,
  GrokImagineImageToVideoParams,
  GrokImagineVideoResponse,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/image_to_video';

/** Generates videos from reference images or a prior text-to-image task. */
export class ImageToVideo {
  constructor(private readonly http: HttpClient) {}

  /**
   * Generate a video and wait until complete.
   * @param params Image-to-video parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed video with results.
   */
  async run(
    params: GrokImagineImageToVideoParams,
    options?: RequestOptions & PollingOptions
  ): Promise<CompletedGrokImagineVideoResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<GrokImagineVideoResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedGrokImagineVideoResponse;
  }

  /**
   * Create an image-to-video task; returns immediately with a task id.
   * @param params Image-to-video parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: GrokImagineImageToVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of an image-to-video task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current video task status.
   */
  async get(id: string, options?: RequestOptions): Promise<GrokImagineVideoResponse> {
    return this.http.request<GrokImagineVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
