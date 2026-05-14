import { describe, expect, it } from 'vitest';
import { GrokImagineClient } from '../src';

describe('GrokImagineClient', () => {
  it('exposes the six endpoint resources', () => {
    const client = new GrokImagineClient({ apiKey: 'test-key' });

    expect(client.textToVideos).toBeDefined();
    expect(client.imageToVideos).toBeDefined();
    expect(client.textToImages).toBeDefined();
    expect(client.imageToImages).toBeDefined();
    expect(client.extensions).toBeDefined();
    expect(client.upscales).toBeDefined();
  });
});
