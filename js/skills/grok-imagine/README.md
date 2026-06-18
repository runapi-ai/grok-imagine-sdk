<p align="center">
  <a href="https://github.com/runapi-ai/grok-imagine">
    <h3 align="center">Grok Imagine API Skill for RunAPI</h3>
  </a>
</p>

<p align="center">
  Install this agent skill, inspect Grok Imagine fields, then run jobs through the RunAPI CLI.
</p>

<p align="center">
  <a href="https://runapi.ai/models/grok-imagine"><strong>Model Reference</strong></a> · <a href="https://github.com/runapi-ai/cli"><strong>CLI</strong></a> · <a href="https://github.com/runapi-ai/grok-imagine-sdk"><strong>SDK</strong></a>
</p>

<div align="center">

[![skills.sh](https://www.skills.sh/b/runapi-ai/grok-imagine)](https://www.skills.sh/runapi-ai/grok-imagine/grok-imagine)
[![ClawHub](https://img.shields.io/badge/ClawHub-runapi--grok--imagine-111827)](https://clawhub.ai/runapi-ai/runapi-grok-imagine)
[![License](https://img.shields.io/github/license/runapi-ai/grok-imagine)](https://github.com/runapi-ai/grok-imagine/blob/main/LICENSE)

</div>
<br/>

Generate video and images with Grok Imagine text-to-video, image-to-video, text-to-image, and edit-image. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Grok Imagine through RunAPI.

The canonical agent file is `skills/grok-imagine/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/grok-imagine -g
```

Or paste this prompt to your AI agent:

```text
Install the grok-imagine skill for me:

1. Clone https://github.com/runapi-ai/grok-imagine
2. Copy the skills/grok-imagine/ directory into your
   user-level skills directory (e.g. ~/.claude/skills/
   for Claude Code, ~/.codex/skills/ for Codex).
3. Verify that SKILL.md is present.
4. Confirm the install path when done.
```

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
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Text to video](https://runapi.ai/models/grok-imagine/text-to-video)
- [Image to video](https://runapi.ai/models/grok-imagine/image-to-video)
- [Text to image](https://runapi.ai/models/grok-imagine/text-to-image)
- [Edit image](https://runapi.ai/models/grok-imagine/edit-image)

## Agent rules

- Integration work uses the target language SDK; one-off generation, manual smoke tests, debugging, or user-requested CLI runs use the RunAPI CLI skill: https://github.com/runapi-ai/cli-skill
- RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.
- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For grok imagine api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.
