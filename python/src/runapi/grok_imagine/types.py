"""Grok-Imagine model identifiers, enums, and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required

DURATION_RANGE = range(6, 31)
FAST_MODEL = "grok-imagine-video-1.5-fast"
FAST_DURATION_RANGE = range(1, 31)
PREVIEW_MODEL = "grok-imagine-video-1.5-preview"
PREVIEW_DURATION_RANGE = range(1, 16)
EXTENSION_DURATION_SECONDS = [6, 10]
INDEX_RANGE = range(0, 6)


class MediaUrl(BaseModel):
    url = optional(str)


class AsyncTaskResponse(TaskResponse):
    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)


class VideoTaskResponse(AsyncTaskResponse):
    """Task status/result for Grok-Imagine video generation."""
    videos = optional([lambda: MediaUrl])
    error = optional(str)


class CompletedVideoTaskResponse(VideoTaskResponse):
    """Narrowed video response from ``run()`` once polling observes completion."""

    videos = required([lambda: MediaUrl])


class ImageTaskResponse(AsyncTaskResponse):
    """Task status/result for Grok-Imagine image generation."""
    images = optional([lambda: MediaUrl])
    error = optional(str)


class CompletedImageTaskResponse(ImageTaskResponse):
    """Narrowed image response from ``run()`` once polling observes completion."""

    images = required([lambda: MediaUrl])


class Segment(BaseModel):
    """Editable segment-map result."""

    url = required(str)
    name = optional(str)
    index = optional(int)


class SegmentMapTaskResponse(AsyncTaskResponse):
    """Task status/result for Grok-Imagine segment-map generation."""

    segments = optional([lambda: Segment])
    error = optional(str)


class CompletedSegmentMapTaskResponse(SegmentMapTaskResponse):
    """Narrowed segment-map response from ``run()`` once polling observes completion."""

    segments = required([lambda: Segment])
