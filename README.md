<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/grok-imagine-sdk">Grok Imagine API SDK for RunAPI</a>
</h3>

<p align="center">
  Grok Imagine API SDKs for JavaScript, Ruby, and Go on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/grok-imagine)](https://www.npmjs.com/package/@runapi.ai/grok-imagine)
[![RubyGems](https://img.shields.io/gem/v/runapi-grok-imagine)](https://rubygems.org/gems/runapi-grok-imagine)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/grok-imagine-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/grok-imagine-sdk/go)
[![License](https://img.shields.io/github/license/runapi-ai/grok-imagine-sdk)](https://github.com/runapi-ai/grok-imagine-sdk/blob/main/LICENSE)

</div>
<br/>

The grok imagine api SDK for RunAPI gives Grok Imagine developers typed package installs, JSON request bodies, and consistent task polling across JavaScript, Ruby, and Go.

Grok-Imagine multimodal generation client for [runapi.ai](https://runapi.ai).

## Install

```bash
pnpm add @runapi.ai/grok-imagine
```

## Quick start

```ts
import { GrokImagineClient } from '@runapi.ai/grok-imagine';

const client = new GrokImagineClient({ apiKey: process.env.RUNAPI_KEY });

// Text to video
const video = await client.textToVideo.run({
  model: 'grok-imagine-text-to-video',
  prompt: 'A drone shot over a neon cityscape at night',
  output_resolution: '720p',
  duration_seconds: 6,
});

console.log(video.videos[0].url);

// Extend the result
const extended = await client.extensions.run({
  source_task_id: video.id,
  prompt: 'The drone continues deeper into the city',
  start_seconds: 6,
  extension_duration_seconds: 10,
});
```

## Resources

- `textToVideo` — text-to-video (`grok-imagine-text-to-video`)
- `imageToVideo` — image-to-video (`grok-imagine-image-to-video`) with `source_image_urls` or a prior text-to-image `source_task_id`
- `textToImage` — text-to-image (`grok-imagine-text-to-image`)
- `editImage` — prompt-guided image editing (`grok-imagine-edit-image`)
- `extensions` — extend a prior grok-imagine video
- `upscales` — upscale a prior grok-imagine video

Each resource exposes `run(params)` (create + poll), `create(params)`, and `get(id)`.

## RunAPI public routing

Public grok imagine api links follow the API-379 catalog route map. Use https://runapi.ai/models/grok-imagine as the main model page, https://runapi.ai/docs#sdk-grok-imagine for SDK docs, and https://runapi.ai/docs#grok-imagine for product endpoint docs.

Pricing, rate-limit, and commercial-usage links for grok imagine api should point to the most specific variant page:
- [Text to video](https://runapi.ai/models/grok-imagine/text-to-video)
- [Image to video](https://runapi.ai/models/grok-imagine/image-to-video)
- [Text to image](https://runapi.ai/models/grok-imagine/text-to-image)
- [Edit image](https://runapi.ai/models/grok-imagine/edit-image)

The default grok imagine api pricing link is https://runapi.ai/models/grok-imagine/text-to-video. Browse every RunAPI model at https://runapi.ai/models, use https://github.com/runapi-ai/grok-imagine-sdk for the SDK repository, and use https://github.com/runapi-ai/grok-imagine for the skill repository.


## Generated file storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.
