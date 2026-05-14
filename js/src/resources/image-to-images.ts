import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineImageResponse,
  GrokImagineImageResponse,
  GrokImagineImageToImageParams,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/image_to_images';

export class ImageToImages {
  constructor(private readonly http: HttpClient) {}

  async run(
    params: GrokImagineImageToImageParams,
    options?: RequestOptions & PollingOptions
  ): Promise<CompletedGrokImagineImageResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<GrokImagineImageResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedGrokImagineImageResponse;
  }

  async create(params: GrokImagineImageToImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<GrokImagineImageResponse> {
    return this.http.request<GrokImagineImageResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
