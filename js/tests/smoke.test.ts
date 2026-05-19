import { describe, expect, it } from 'vitest';
import { GrokImagineClient } from '../src';

describe('GrokImagineClient', () => {
  it('exposes the six endpoint resources', () => {
    const client = new GrokImagineClient({ apiKey: 'test-key' });

    expect(client.textToVideo).toBeDefined();
    expect(client.imageToVideo).toBeDefined();
    expect(client.textToImage).toBeDefined();
    expect(client.imageToImage).toBeDefined();
    expect(client.extensions).toBeDefined();
    expect(client.upscales).toBeDefined();
  });
});
