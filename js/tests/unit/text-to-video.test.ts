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

});
