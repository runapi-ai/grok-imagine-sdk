import type { ActionSchema, HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type {
  CompletedGrokImagineImageResponse,
  GrokImagineEditImageParams,
  GrokImagineImageResponse,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/edit_image';

/** Applies prompt-guided edits to a source image. */
export class EditImage {
  constructor(private readonly http: HttpClient) {}

  /**
   * Edit an image and wait until complete.
   * @param params Image edit parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed edit with images.
   */
  async run(
    params: GrokImagineEditImageParams,
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
   * Create an image edit task; returns immediately with a task id.
   * @param params Image edit parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: GrokImagineEditImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['edit-image'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of an image edit task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current image task status.
   */
  async get(id: string, options?: RequestOptions): Promise<GrokImagineImageResponse> {
    return this.http.request<GrokImagineImageResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
