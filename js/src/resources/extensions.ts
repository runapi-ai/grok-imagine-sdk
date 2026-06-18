import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineVideoResponse,
  GrokImagineExtendParams,
  GrokImagineVideoResponse,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/extend_video';

/** Appends new footage to a previously generated video from a chosen timestamp. */
export class Extensions {
  constructor(private readonly http: HttpClient) {}

  /**
   * Extend a video and wait until complete.
   * @param params Video extension parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed video with results.
   */
  async run(
    params: GrokImagineExtendParams,
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
   * Create a video extension task; returns immediately with a task id.
   * @param params Video extension parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: GrokImagineExtendParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  /**
   * Fetch the current status of a video extension task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current video task status.
   */
  async get(id: string, options?: RequestOptions): Promise<GrokImagineVideoResponse> {
    return this.http.request<GrokImagineVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
