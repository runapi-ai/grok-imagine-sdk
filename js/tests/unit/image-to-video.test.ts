import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { ImageToVideo } from '../../src/resources/image-to-video';

describe('Grok Imagine image-to-video', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates with motion_style', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const resource = new ImageToVideo(mockHttp);

    await resource.create({
      model: 'grok-imagine-image-to-video',
      source_task_id: 'source-image-task',
      motion_style: 'spicy',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/image_to_video', {
      body: {
        model: 'grok-imagine-image-to-video',
        source_task_id: 'source-image-task',
        motion_style: 'spicy',
      },
    });
  });

  it('creates with source_image_urls', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const resource = new ImageToVideo(mockHttp);

    await resource.create({
      model: 'grok-imagine-image-to-video',
      source_image_urls: ['https://cdn.runapi.ai/public/samples/result.png'],
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/image_to_video', {
      body: {
        model: 'grok-imagine-image-to-video',
        source_image_urls: ['https://cdn.runapi.ai/public/samples/result.png'],
      },
    });
  });

  it('rejects spicy motion_style with source_image_urls', async () => {
    const resource = new ImageToVideo(mockHttp);

    await expect(resource.create({
      model: 'grok-imagine-image-to-video',
      source_image_urls: ['https://cdn.runapi.ai/public/samples/result.png'],
      motion_style: 'spicy',
    })).rejects.toThrow(/spicy motion_style requires a source_task_id source image/);

    expect(mockHttp.request).not.toHaveBeenCalled();
  });
});
