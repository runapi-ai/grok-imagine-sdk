# Grok Imagine Go SDK for RunAPI

The Grok Imagine Go SDK is the language-specific package for Grok Imagine on RunAPI. Use this package for image and video generation, image editing, and creative production workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Go.

This README is the Go package guide inside the public `grok-imagine-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/grok-imagine; for API reference, use https://runapi.ai/docs#grok-imagine; for SDK docs, use https://runapi.ai/docs#sdk-grok-imagine.

## Install

```bash
go get github.com/runapi-ai/grok-imagine-sdk/go@latest
```

## Quick start

```go
import (
  "context"

  "github.com/runapi-ai/grok-imagine-sdk/go/grokimagine"
)

client, err := grokimagine.NewClient()
task, err := client.TextToVideo.Create(context.Background(), grokimagine.TextToVideoParams{
  // Pass the Grok Imagine JSON request body from https://runapi.ai/docs#grok-imagine.
})
status, err := client.TextToVideo.Get(context.Background(), task.ID)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use the public Go module with `github.com/runapi-ai/core-sdk/go` options when building media services, CLIs, or workers. The available resources are `TextToVideo`, `ImageToVideo`, `TextToImage`, `EditImage`, `Extensions`, and `Upscales`; use `ModelTextToVideo15Preview` or `ModelImageToVideo15Preview` for the preview video model. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/grok-imagine
- SDK docs: https://runapi.ai/docs#sdk-grok-imagine
- Product docs: https://runapi.ai/docs#grok-imagine
- Video 1.5 Preview pricing and rate limits: https://runapi.ai/models/grok-imagine/video-1.5-preview
- Text-to-video pricing and rate limits: https://runapi.ai/models/grok-imagine/text-to-video
- Image-to-video pricing and rate limits: https://runapi.ai/models/grok-imagine/image-to-video
- Provider comparison: https://runapi.ai/providers/xai
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/grok-imagine-sdk

## License

Licensed under the Apache License, Version 2.0.
