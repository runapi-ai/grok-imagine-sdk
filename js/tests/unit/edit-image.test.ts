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
});
