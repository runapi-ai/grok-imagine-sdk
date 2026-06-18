import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineVideoResponse,
  GrokImagineUpscaleParams,
  GrokImagineVideoResponse,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/upscale_image';

/** Upscales a previously generated image to higher resolution. */
export class Upscales {
  constructor(private readonly http: HttpClient) {}

  /**
   * Upscale an image and wait until complete.
   * @param params Upscale parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed upscale with results.
   */
  async run(
    params: GrokImagineUpscaleParams,
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
   * Create an upscale task; returns immediately with a task id.
   * @param params Upscale parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: GrokImagineUpscaleParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  /**
   * Fetch the current status of an upscale task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current upscale task status.
   */
  async get(id: string, options?: RequestOptions): Promise<GrokImagineVideoResponse> {
    return this.http.request<GrokImagineVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
