import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { Upscales } from '../../src/resources/upscales';

describe('Grok Imagine upscales', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates an upscale task with source_task_id', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const upscales = new Upscales(mockHttp);

    await upscales.create({
      source_task_id: 'source-video-task',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/upscale_image', {
      body: {
        source_task_id: 'source-video-task',
      },
    });
  });
});
