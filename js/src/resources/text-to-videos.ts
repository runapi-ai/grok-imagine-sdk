import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineVideoResponse,
  GrokImagineTextToVideoParams,
  GrokImagineVideoResponse,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/text_to_videos';

export class TextToVideos {
  constructor(private readonly http: HttpClient) {}

  async run(
    params: GrokImagineTextToVideoParams,
    options?: RequestOptions & PollingOptions
  ): Promise<CompletedGrokImagineVideoResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<GrokImagineVideoResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedGrokImagineVideoResponse;
  }

  async create(params: GrokImagineTextToVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body: compactParams(params),
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<GrokImagineVideoResponse> {
    return this.http.request<GrokImagineVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
