# Grok Imagine API JavaScript SDK for RunAPI

The grok imagine api JavaScript SDK is the language-specific package for Grok Imagine on RunAPI. Use this grok imagine api package for text-to-image, image editing, and creative production flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in JavaScript.

This grok imagine api README is the JavaScript package guide inside the public `grok-imagine-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/grok-imagine; for API reference, use https://runapi.ai/docs#grok-imagine; for SDK docs, use https://runapi.ai/docs#sdk-grok-imagine.

## Install

```bash
npm install @runapi.ai/grok-imagine
```

## Quick start

```typescript
import { GrokImagineClient } from '@runapi.ai/grok-imagine';

const client = new GrokImagineClient();
const task = await client.textToVideo.create({
  // Pass the Grok Imagine JSON request body from https://runapi.ai/docs#grok-imagine.
});
const status = await client.textToVideo.get(task.id);
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

## Language notes

Use the TypeScript types in `src/types.ts` and the resource classes under `src/resources` when building image applications. The available resources include text to videos, image to videos, text to images, image to images, extensions, and upscales. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/grok-imagine
- SDK docs: https://runapi.ai/docs#sdk-grok-imagine
- Product docs: https://runapi.ai/docs#grok-imagine
- Pricing and rate limits: https://runapi.ai/models/grok-imagine/text-to-video
- Provider comparison: https://runapi.ai/providers/xai
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/grok-imagine-sdk

## License

Licensed under the Apache License, Version 2.0.
