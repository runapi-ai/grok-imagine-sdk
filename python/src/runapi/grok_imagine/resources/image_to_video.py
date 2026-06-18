"""Grok-Imagine image-to-video generation resource.

Accepts either external source_image_urls or a prior text-to-image
source_task_id (+ index).
"""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    ASPECT_RATIOS,
    IMAGE_TO_VIDEO_MODEL,
    INDEX_RANGE,
    MOTION_STYLES,
    RESOLUTIONS,
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class ImageToVideo(Resource):
    """Generate videos from a source image with Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/image_to_video"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, **params: Any) -> Any:
        """Create an image-to-video task and poll until it completes.

        Args:
            **params: Image-to-video parameters (model, prompt, ...).

        Returns:
            The completed image-to-video response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create an image-to-video task and return immediately with an id.

        Args:
            **params: Image-to-video parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of an image-to-video task.

        Args:
            id: Task id.

        Returns:
            The current image-to-video status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if params.get("model") != IMAGE_TO_VIDEO_MODEL:
            raise ValidationError("model is required")

        source_image_urls = params.get("source_image_urls")
        source_task_id = params.get("source_task_id")

        if source_image_urls and len(source_image_urls) > 0 and source_task_id:
            raise ValidationError("Provide either source_image_urls or source_task_id, not both")
        if (not source_image_urls or len(source_image_urls) == 0) and not source_task_id:
            raise ValidationError("One of source_image_urls or source_task_id is required")
        if source_image_urls and len(source_image_urls) > 1:
            raise ValidationError("source_image_urls supports at most 1 entry")

        if source_task_id:
            index = params.get("index")
            if index is not None:
                try:
                    value = None if isinstance(index, bool) else int(index)
                except (TypeError, ValueError):
                    value = None
                if value is None or value not in INDEX_RANGE:
                    raise ValidationError("index must be an integer between 0 and 5")

        self._validate_optional(params, "aspect_ratio", ASPECT_RATIOS)
        self._validate_optional(params, "motion_style", MOTION_STYLES)
        self._validate_optional(params, "output_resolution", RESOLUTIONS)

        if str(params.get("motion_style")) == "spicy" and source_image_urls and len(source_image_urls) > 0:
            raise ValidationError("spicy motion_style requires a source_task_id source image.")
