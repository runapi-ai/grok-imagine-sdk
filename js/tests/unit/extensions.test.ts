import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { Extensions } from '../../src/resources/extensions';

describe('Grok Imagine extensions', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates an extension task with canonical fields', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const extensions = new Extensions(mockHttp);

    await extensions.create({
      source_task_id: 'source-video-task',
      prompt: 'Continue the shot',
      start_seconds: 6,
      extension_duration_seconds: 10,
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/grok_imagine/extend_video', {
      body: {
        source_task_id: 'source-video-task',
        prompt: 'Continue the shot',
        start_seconds: 6,
        extension_duration_seconds: 10,
      },
    });
  });

});
