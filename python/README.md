# Grok Imagine Python SDK for RunAPI

The Grok Imagine Python SDK is the language-specific package for Grok Imagine on RunAPI. Use this grok imagine api package for text-to-image, image editing, and creative production flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Python.

This grok imagine api README is the Python package guide inside the public `grok-imagine-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/grok-imagine; for API reference, use https://runapi.ai/docs#grok-imagine; for SDK docs, use https://runapi.ai/docs#sdk-grok-imagine.

## Install

```bash
pip install runapi-grok-imagine
```

## Quick start

```python
from runapi.grok_imagine import GrokImagineClient

client = GrokImagineClient()  # reads RUNAPI_API_KEY, or pass api_key="sk-..."

task = client.text_to_video.create(
    model="grok-imagine-text-to-video",
    prompt="A drone shot over a neon cityscape at night",
    output_resolution="720p",
)
status = client.text_to_video.get(task.id)

image = client.text_to_image.create(
    model="grok-imagine-text-to-image",
    prompt="A watercolor fox in a snowy forest",
    aspect_ratio="16:9",
)
```

Use `create` to submit a task and return quickly, `get` to fetch the latest task state, and `run` to create and poll until completion:

```python
result = client.text_to_video.run(
    model="grok-imagine-text-to-video",
    prompt="A serene mountain lake at dawn",
)
print(result.videos[0].url)
```

In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Pass parameters as keyword arguments and catch the `runapi.grok_imagine` error classes when building media jobs or scripts. The available resources are `text_to_video`, `image_to_video`, `text_to_image`, `edit_image`, `extensions`, and `upscales`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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
