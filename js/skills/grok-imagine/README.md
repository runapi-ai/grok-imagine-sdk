# Grok Imagine API Skill for RunAPI

Generate video and images with Grok Imagine text-to-video, image-to-video, text-to-image, and image-to-image. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Grok Imagine through RunAPI.

The canonical agent file is `skills/grok-imagine/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/grok-imagine -g
```

Or manually: clone this repo and copy `skills/grok-imagine/` into your agent's skills directory.

## Quick example

```typescript
import { GrokImagineClient } from '@runapi.ai/grok-imagine';

const client = new GrokImagineClient();
const result = await client.textToVideo.run({
  model: 'grok-imagine-text-to-video',
  prompt: 'A drone shot over a neon cityscape at night',
});
```

## Routing

- Model page: https://runapi.ai/models/grok-imagine
- Product docs: https://runapi.ai/docs#grok-imagine
- SDK docs: https://runapi.ai/docs#sdk-grok-imagine
- SDK repository: https://github.com/runapi-ai/grok-imagine-sdk
- Pricing and rate limits: https://runapi.ai/models/grok-imagine/text-to-video
- Provider comparison: https://runapi.ai/providers/xai
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Text to video](https://runapi.ai/models/grok-imagine/text-to-video)
- [Image to video](https://runapi.ai/models/grok-imagine/image-to-video)
- [Text to image](https://runapi.ai/models/grok-imagine/text-to-image)
- [Image to image](https://runapi.ai/models/grok-imagine/image-to-image)

## Agent rules

- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For grok imagine api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.
