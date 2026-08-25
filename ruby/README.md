# Grok Imagine Ruby SDK for RunAPI

The Grok Imagine Ruby SDK is the language-specific package for Grok Imagine on RunAPI. Use this package for image and video generation, image editing, and creative production workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Ruby.

This README is the Ruby package guide inside the public `grok-imagine-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/grok-imagine; for API reference, use https://runapi.ai/docs/api/grok-imagine/text-to-image; for SDK docs, use https://runapi.ai/docs/resources/sdks.

## Install

```bash
gem install runapi-grok-imagine
```

## Quick start

```ruby
require "runapi/grok_imagine"

client = RunApi::GrokImagine::Client.new
task = client.text_to_video.create(
  # Pass the Grok Imagine JSON request body from https://runapi.ai/docs/api/grok-imagine/text-to-image.
)
status = client.text_to_video.get(task.id)
```

## Edit with Image 2.0

Image 2.0 edits one to five source images directly. Provide the source URLs and output aspect ratio; the edit prompt is optional. The task-based `source_task_id` and `mask_indices` fields are no longer accepted by this request.

```ruby
edit = client.edit_image.create(
  model: "grok-imagine-image-2-0",
  source_image_urls: ["https://cdn.runapi.ai/public/samples/input.png"],
  aspect_ratio: "1:1",
  prompt: "Change the background to a sunset beach"
)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use Ruby keyword arguments and the `RunApi::GrokImagine` error classes when building media jobs, Rails workers, or scripts. The available resources are `text_to_video`, `image_to_video`, `text_to_image`, `edit_image`, `extensions`, and `upscales`; use `grok-imagine-video-1.5-fast` or `grok-imagine-video-1.5-preview` with either video resource. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/grok-imagine
- SDK docs: https://runapi.ai/docs/resources/sdks
- Product docs: https://runapi.ai/docs/api/grok-imagine/text-to-image
- Video 1.5 Preview pricing and rate limits: https://runapi.ai/models/grok-imagine/video-1.5-preview
- Video 1.5 Fast pricing and rate limits: https://runapi.ai/models/grok-imagine/video-1.5-fast
- Text-to-video pricing and rate limits: https://runapi.ai/models/grok-imagine/text-to-video
- Image-to-video pricing and rate limits: https://runapi.ai/models/grok-imagine/image-to-video
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/grok-imagine-sdk

## License

Licensed under the Apache License, Version 2.0.
