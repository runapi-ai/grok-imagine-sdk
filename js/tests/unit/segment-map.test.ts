import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { SegmentMap } from '../../src/resources/segment-map';

describe('Grok Imagine segment-map', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates from an Image 2.0 text-to-image task', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'segment-map-task' });
    const resource = new SegmentMap(mockHttp);

    await resource.create({
      model: 'grok-imagine-image-2-0',
      source_task_id: 'text-to-image-task',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/segment_map', {
      body: {
        model: 'grok-imagine-image-2-0',
        source_task_id: 'text-to-image-task',
      },
    });
  });

  it('returns typed segment results instead of images', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({
      id: 'segment-map-task',
      status: 'completed',
      segments: [{ url: 'https://file.runapi.ai/segment.png', name: 'subject', index: 1 }],
    });
    const resource = new SegmentMap(mockHttp);

    const response = await resource.get('segment-map-task');

    expect(response.segments).toEqual([{ url: 'https://file.runapi.ai/segment.png', name: 'subject', index: 1 }]);
  });
});
