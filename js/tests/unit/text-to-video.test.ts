import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { TextToVideo } from '../../src/resources/text-to-video';

describe('Grok Imagine text-to-video', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates with motion_style', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const resource = new TextToVideo(mockHttp);

    await resource.create({
      model: 'grok-imagine-text-to-video',
      prompt: 'A neon city flythrough',
      motion_style: 'fun',
      output_resolution: '720p',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/text_to_video', {
      body: {
        model: 'grok-imagine-text-to-video',
        prompt: 'A neon city flythrough',
        motion_style: 'fun',
        output_resolution: '720p',
      },
    });
  });

  it('creates preview requests without legacy fields', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-preview' });
    const resource = new TextToVideo(mockHttp);

    await resource.create({
      model: 'grok-imagine-video-1.5-preview',
      prompt: 'A quiet city rain scene',
      aspect_ratio: 'auto',
      duration_seconds: 15,
      output_resolution: '720p',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/text_to_video', {
      body: {
        model: 'grok-imagine-video-1.5-preview',
        prompt: 'A quiet city rain scene',
        aspect_ratio: 'auto',
        duration_seconds: 15,
        output_resolution: '720p',
      },
    });
  });

  it('creates fast requests with fast-only inputs', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-fast' });
    const resource = new TextToVideo(mockHttp);

    await resource.create({
      model: 'grok-imagine-video-1.5-fast',
      prompt: 'A paper plane crossing a sunlit room',
      reference_image_urls: ['https://cdn.runapi.ai/public/samples/result.png'],
      aspect_ratio: '16:9',
      duration_seconds: 5,
      output_resolution: '720p',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/text_to_video', {
      body: {
        model: 'grok-imagine-video-1.5-fast',
        prompt: 'A paper plane crossing a sunlit room',
        reference_image_urls: ['https://cdn.runapi.ai/public/samples/result.png'],
        aspect_ratio: '16:9',
        duration_seconds: 5,
        output_resolution: '720p',
      },
    });
  });

});
