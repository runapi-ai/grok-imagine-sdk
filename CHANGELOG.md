# Changelog

## [js/v0.2.14](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.14), [ruby/v0.2.15](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.15), [go/v0.2.14](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.14), [python/v0.2.3](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.2.3), [java/v0.1.6](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.6) - 2026-08-21

### Added
- Add typed Image 2.0 text-to-image, segment-map, and segment-backed image editing support.


## [ruby/v0.2.14](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.14) - 2026-08-18

### Changed
- Allow Ruby clients to install the core SDK release that adds persistent Files and multipart Uploads alongside this model SDK.


## [js/v0.2.13](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.13), [ruby/v0.2.13](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.13), [go/v0.2.13](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.13), [python/v0.2.2](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.2.2) - 2026-08-07

### Changed
- Accept Preview 1080p output and enforce the documented reference image limits.


## [js/v0.2.12](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.12), [ruby/v0.2.12](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.12), [go/v0.2.12](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.12), [python/v0.2.1](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.2.1), [java/v0.1.5](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.5) - 2026-07-28

### Changed
- Describe supported request fields, validate existing model-specific requirements, and expose video results for upscale responses.


## [go/v0.2.11](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.11) - 2026-07-28

### Added
- Expose persisted billing facts on task responses.

## [js/v0.2.11](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.11) - 2026-07-28

### Added
- Type task billing facts on task responses.

## [ruby/v0.2.11](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.11) - 2026-07-28

### Added
- Expose live pricing through the shared core SDK.


## [python/v0.2.0](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.2.0) - 2026-07-24

### Added
- Expose shared Files, Account, and Pricing resources plus typed Task Billing Facts through the Provider Client.


## [js/v0.2.10](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.10), [ruby/v0.2.10](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.10), [go/v0.2.10](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.10), [python/v0.1.3](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.1.3), [java/v0.1.4](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.4) - 2026-07-20

### Breaking
- Replace Grok Imagine image-to-video `source_image_urls` with scalar `source_image_url`.
  Migration: Pass the source image as `source_image_url`; keep `reference_image_urls` only for optional Fast reference images.


## [js/v0.2.9](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.9), [ruby/v0.2.9](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.9), [go/v0.2.9](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.9), [python/v0.1.2](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.1.2), [java/v0.1.3](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.3) - 2026-07-17

### Changed
- Add typed Fast text-to-video and image-to-video requests across all SDK languages.
- Add Fast model constraints, reference images, 480p and 720p output, and duration support.

## [js/v0.2.8](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.8), [ruby/v0.2.8](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.8), [go/v0.2.8](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.8), [python/v0.1.1](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.1.1), [java/v0.1.2](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.2) - 2026-07-16

### Changed
- Add `grok-imagine-video-1.5-preview` to Grok Imagine text-to-video and image-to-video SDK contracts and typed request surfaces.
- Refresh Grok Imagine SDK public READMEs with canonical variant links.

## [js/v0.2.7](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.7), [ruby/v0.2.7](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.7), [go/v0.2.7](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.7) - 2026-07-02

### Fixed
- Request validation now derives allowed values (aspect ratios, output resolutions, formats) from the RunAPI request contract, so valid requests are no longer rejected client-side.
- Corrected field names and widened enum coverage for image generation endpoints.
- Documented reference image URL parameters where supported.

## [java/v0.1.1](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.1) - 2026-06-25

### Fixed
- Fixed Java retry handling for Retry-After response headers.
- Fixed Java contract validation for action-level conditional rules.
- Refreshed Java SDK metadata for v0.1.1.

## [java/v0.1.0](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/java%2Fv0.1.0) - 2026-06-24

### Added
- Publish `ai.runapi:runapi-grok-imagine` for Java SDK consumers.
- Include typed Java builders, synchronous client resources, sources, and Javadocs.

## [js/v0.2.6](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.6), [ruby/v0.2.6](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.6), [go/v0.2.6](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.6), [python/v0.1.0](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/python%2Fv0.1.0) - 2026-06-18

### Changed
- Per-method documentation for all resource methods

## [js/v0.2.5](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.5), [ruby/v0.2.5](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.5), [go/v0.2.5](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.5) - 2026-06-01

### Changed
- Align SDK with upstream Input Contract and public API vocabulary changes
- Update endpoint definitions and field constraints

## [js/v0.2.4](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/js%2Fv0.2.4), [ruby/v0.2.4](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/ruby%2Fv0.2.4), [go/v0.2.4](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/go%2Fv0.2.4) - 2026-05-22

### Changed
- Publish JavaScript, Ruby, and Go SDK artifacts for grok-imagine with per-language GitHub release tags.
- Refresh public README metadata.

## [v0.2.3](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/v0.2.3) - 2026-05-22

### Changed
- Publish grok-imagine-sdk v0.2.3 with refreshed README header, package metadata, and current SDK source.

## [v0.2.1](https://github.com/runapi-ai/grok-imagine-sdk/releases/tag/v0.2.1) - 2026-05-19

Initial release.
