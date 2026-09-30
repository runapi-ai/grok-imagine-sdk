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

  it('creates direct Image 2.0 edits from source image URLs', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-2' });
    const resource = new EditImage(mockHttp);

    await resource.create({
      model: 'grok-imagine-image-2-0',
      aspect_ratio: '16:9',
      source_image_urls: [
        'https://cdn.runapi.ai/public/samples/source-1.png',
        'https://cdn.runapi.ai/public/samples/source-2.png',
      ],
      prompt: 'Replace the foreground',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/edit_image', {
      body: {
        model: 'grok-imagine-image-2-0',
        aspect_ratio: '16:9',
        source_image_urls: [
          'https://cdn.runapi.ai/public/samples/source-1.png',
          'https://cdn.runapi.ai/public/samples/source-2.png',
        ],
        prompt: 'Replace the foreground',
      },
    });
  });

  it('allows Image 2.0 edits without a prompt', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-3' });
    const resource = new EditImage(mockHttp);

    await resource.create({
      model: 'grok-imagine-image-2-0',
      aspect_ratio: 'auto',
      source_image_urls: ['https://cdn.runapi.ai/public/samples/source.png'],
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/edit_image', {
      body: {
        model: 'grok-imagine-image-2-0',
        aspect_ratio: 'auto',
        source_image_urls: ['https://cdn.runapi.ai/public/samples/source.png'],
      },
    });
  });
});
