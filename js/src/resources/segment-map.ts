import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type {
  CompletedGrokImagineSegmentMapResponse,
  GrokImagineSegmentMapResponse,
  GrokImagineSegmentMapParams,
  TaskCreateResponse,
} from '../types';

const ENDPOINT = '/api/v1/grok_imagine/segment_map';

/** Creates an editable segmentation map from a public image URL. */
export class SegmentMap {
  constructor(private readonly http: HttpClient) {}

  async run(
    params: GrokImagineSegmentMapParams,
    options?: RequestOptions & PollingOptions
  ): Promise<CompletedGrokImagineSegmentMapResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<GrokImagineSegmentMapResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedGrokImagineSegmentMapResponse;
  }

  async create(params: GrokImagineSegmentMapParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<GrokImagineSegmentMapResponse> {
    return this.http.request<GrokImagineSegmentMapResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
