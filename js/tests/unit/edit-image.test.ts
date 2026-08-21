import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { EditImage } from '../../src/resources/edit-image';

describe('Grok Imagine edit-image', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates with source_image_url', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const resource = new EditImage(mockHttp);

    await resource.create({
      model: 'grok-imagine-edit-image',
      source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
      prompt: 'Restyle',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/edit_image', {
      body: {
        model: 'grok-imagine-edit-image',
        source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
        prompt: 'Restyle',
      },
    });
  });

  it('creates Image 2.0 edits from a segment-map task', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-2' });
    const resource = new EditImage(mockHttp);

    await resource.create({
      model: 'grok-imagine-image-2-0',
      source_task_id: 'segment-map-task',
      mask_indices: [1, 3],
      prompt: 'Replace the foreground',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/edit_image', {
      body: {
        model: 'grok-imagine-image-2-0',
        source_task_id: 'segment-map-task',
        mask_indices: [1, 3],
        prompt: 'Replace the foreground',
      },
    });
  });
});
