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

  it.each([
    { source_image_urls: [], message: 'source_image_urls' },
    {
      source_image_urls: [
        'https://cdn.runapi.ai/public/samples/1.png',
        'https://cdn.runapi.ai/public/samples/2.png',
        'https://cdn.runapi.ai/public/samples/3.png',
        'https://cdn.runapi.ai/public/samples/4.png',
        'https://cdn.runapi.ai/public/samples/5.png',
        'https://cdn.runapi.ai/public/samples/6.png',
      ],
      message: 'source_image_urls',
    },
    { source_image_urls: 'https://cdn.runapi.ai/public/samples/source.png', message: 'source_image_urls' },
  ])('rejects invalid Image 2.0 source image collections', async ({ source_image_urls, message }) => {
    const resource = new EditImage(mockHttp);

    await expect(resource.create({
      model: 'grok-imagine-image-2-0',
      aspect_ratio: '1:1',
      source_image_urls,
    } as never)).rejects.toThrow(message);
    expect(mockHttp.request).not.toHaveBeenCalled();
  });

  it('rejects an invalid Image 2.0 aspect ratio', async () => {
    const resource = new EditImage(mockHttp);

    await expect(resource.create({
      model: 'grok-imagine-image-2-0',
      aspect_ratio: '4:3',
      source_image_urls: ['https://cdn.runapi.ai/public/samples/source.png'],
    } as never)).rejects.toThrow('aspect_ratio');
    expect(mockHttp.request).not.toHaveBeenCalled();
  });

  it.each(['source_task_id', 'mask_indices'])('rejects legacy Image 2.0 %s input', async (field) => {
    const resource = new EditImage(mockHttp);

    await expect(resource.create({
      model: 'grok-imagine-image-2-0',
      aspect_ratio: '1:1',
      source_image_urls: ['https://cdn.runapi.ai/public/samples/source.png'],
      [field]: field === 'source_task_id' ? 'segment-map-task' : [1, 3],
    } as never)).rejects.toThrow(field);
    expect(mockHttp.request).not.toHaveBeenCalled();
  });

  it.each([
    ['source_image_urls', ['https://cdn.runapi.ai/public/samples/direct.png']],
    ['aspect_ratio', '1:1'],
  ])('rejects Image 2.0-only %s input for the original model', async (field, value) => {
    const resource = new EditImage(mockHttp);

    await expect(resource.create({
      model: 'grok-imagine-edit-image',
      source_image_url: 'https://cdn.runapi.ai/public/samples/source.png',
      [field]: value,
    } as never)).rejects.toThrow(field);
    expect(mockHttp.request).not.toHaveBeenCalled();
  });
});
