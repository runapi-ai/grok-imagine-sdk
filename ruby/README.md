# Grok Imagine API Ruby SDK for RunAPI

The grok imagine api Ruby SDK is the language-specific package for Grok Imagine on RunAPI. Use this grok imagine api package for text-to-image, image editing, and creative production flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Ruby.

This grok imagine api README is the Ruby package guide inside the public `grok-imagine-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/grok-imagine; for API reference, use https://runapi.ai/docs#grok-imagine; for SDK docs, use https://runapi.ai/docs#sdk-grok-imagine.

## Install

```bash
gem install runapi-grok_imagine
```

## Quick start

```ruby
require "runapi-grok_imagine"

client = RunApi::GrokImagine::Client.new
task = client.text_to_video.create(
  # Pass the Grok Imagine JSON request body from https://runapi.ai/docs#grok-imagine.
)
status = client.text_to_video.get(task.id)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use Ruby keyword arguments and the `RunApi::GrokImagine` error classes when building image jobs, Rails workers, or scripts. The available resources include text to videos, image to videos, text to images, image to images, extensions, and upscales. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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
