"""Grok-Imagine model identifiers, enums, and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required

DURATION_RANGE = range(6, 31)
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
