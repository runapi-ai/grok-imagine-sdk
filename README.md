<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/grok-imagine-sdk">Grok Imagine API SDK for RunAPI</a>
</h3>

<p align="center">
  Grok Imagine API SDKs for JavaScript, Python, Ruby, Go, and Java on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/grok-imagine)](https://www.npmjs.com/package/@runapi.ai/grok-imagine)
[![PyPI](https://img.shields.io/pypi/v/runapi-grok-imagine)](https://pypi.org/project/runapi-grok-imagine/)
[![RubyGems](https://img.shields.io/gem/v/runapi-grok_imagine)](https://rubygems.org/gems/runapi-grok_imagine)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/grok-imagine-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/grok-imagine-sdk/go)
[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-grok-imagine)](https://central.sonatype.com/artifact/ai.runapi/runapi-grok-imagine)
[![License](https://img.shields.io/github/license/runapi-ai/grok-imagine-sdk)](https://github.com/runapi-ai/grok-imagine-sdk/blob/main/LICENSE)

</div>
<br/>

The Grok Imagine API SDK packages JavaScript, Python, Ruby, Go, and Java clients for Grok Imagine on RunAPI. Use it for text-to-image, text-to-video, image-to-video, image editing, extension, and upscale workflows when your app needs typed request builders, predictable task polling, file upload helpers, account helpers, and consistent RunAPI errors.

Grok Imagine is listed in the RunAPI model catalog at https://runapi.ai/models/grok-imagine. Variant pages below carry pricing, rate-limit, and commercial-usage details. The public `grok-imagine-sdk` repository groups the language packages, examples, CI, and release tags for this model.

## Install

```bash
npm install @runapi.ai/grok-imagine
pip install runapi-grok-imagine
gem install runapi-grok_imagine
go get github.com/runapi-ai/grok-imagine-sdk/go@latest
```

Gradle:

```kotlin
dependencies {
  implementation("ai.runapi:runapi-grok-imagine:0.1.0")
}
```

Maven:

```xml
<dependency>
  <groupId>ai.runapi</groupId>
  <artifactId>runapi-grok-imagine</artifactId>
  <version>0.1.0</version>
</dependency>
```

Use the Java BOM when installing multiple RunAPI Java modules:

```kotlin
dependencies {
  implementation(platform("ai.runapi:runapi-bom:0.1.0"))
  implementation("ai.runapi:runapi-grok-imagine")
}
```

## What you can build

- Build apps, agent workflows, batch jobs, and production services around Grok Imagine requests.
- Install only the language package your app needs while keeping one model-specific repository for docs and releases.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Upload local files, URL files, or base64 files through shared RunAPI file helpers.
- Handle validation, authentication, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

## Java quick start

```java
import ai.runapi.grokimagine.GrokImagineClient;
import ai.runapi.grokimagine.types.TextToImageParams;
import ai.runapi.grokimagine.types.CompletedTextToImageResponse;
import ai.runapi.grokimagine.types.TextToImageModel;

GrokImagineClient client = GrokImagineClient.builder()
    .apiKey(System.getenv("RUNAPI_API_KEY"))
    .build();

CompletedTextToImageResponse result = client.textToImage().run(
    TextToImageParams.builder()
        .model(TextToImageModel.GROK_IMAGINE_TEXT_TO_IMAGE)
        .prompt("A neon city street after rain")
        .aspectRatio("16:9")
        .build()
);
```

Java packages target Java 8 bytecode and are tested on Java 8, 11, 17, and 21. Each model artifact depends on `ai.runapi:runapi-core`, so application code normally installs only `ai.runapi:runapi-grok-imagine`.

## Task lifecycle

Most media endpoints are asynchronous. `create()` submits a task and returns its id, `get(id)` fetches the latest task state, and `run(params)` creates the task and polls until it reaches a terminal state. In web request handlers, prefer `create()` plus webhook or later `get()` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/grok-imagine`.
- `python/` publishes `runapi-grok-imagine`.
- `ruby/` publishes `runapi-grok_imagine` when RubyGems publishing resumes.
- `go/` publishes `github.com/runapi-ai/grok-imagine-sdk/go` and depends on `github.com/runapi-ai/core-sdk/go`.
- `java/` publishes `ai.runapi:runapi-grok-imagine` and depends on `ai.runapi:runapi-core`.

## Public links

- Model page: https://runapi.ai/models/grok-imagine
- SDK docs: https://runapi.ai/docs#sdk-grok-imagine
- Product docs: https://runapi.ai/docs#grok-imagine
- SDK repository: https://github.com/runapi-ai/grok-imagine-sdk
- Skill repository: https://github.com/runapi-ai/grok-imagine
- Provider comparison: https://runapi.ai/providers/xai
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific Grok Imagine variant page for pricing, rate limits, and commercial usage:
- [Text to video](https://runapi.ai/models/grok-imagine/text-to-video)
- [Image to video](https://runapi.ai/models/grok-imagine/image-to-video)
- [Text to image](https://runapi.ai/models/grok-imagine/text-to-image)
- [Edit image](https://runapi.ai/models/grok-imagine/edit-image)

Default pricing link for the Grok Imagine SDK: https://runapi.ai/models/grok-imagine/text-to-video

## File storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## FAQ

### Which package should I install for Grok Imagine work?

Install the model package for your language: `@runapi.ai/grok-imagine` on npm, `runapi-grok-imagine` on PyPI, `runapi-grok_imagine` on RubyGems, `github.com/runapi-ai/grok-imagine-sdk/go`, or `ai.runapi:runapi-grok-imagine`. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary Grok Imagine links point to https://runapi.ai/models/grok-imagine. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/grok-imagine/text-to-video. Provider comparisons point to https://runapi.ai/providers/xai, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.
