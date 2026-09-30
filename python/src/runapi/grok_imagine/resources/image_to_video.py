"""Grok-Imagine image-to-video generation resource.

Accepts either an external source_image_url or a prior text-to-image
source_task_id (+ index).
"""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import Resource, RequestOptions

from ..types import (
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class ImageToVideo(Resource):
    """Generate videos from a source image with Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/image_to_video"
    CLASSIC_MODEL = "grok-imagine-image-to-video"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create an image-to-video task and poll until it completes.

        Args:
            **params: Image-to-video parameters (model, prompt, ...).

        Returns:
            The completed image-to-video response.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create an image-to-video task and return immediately with an id.

        Args:
            **params: Image-to-video parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of an image-to-video task.

        Args:
            id: Task id.

        Returns:
            The current image-to-video status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
