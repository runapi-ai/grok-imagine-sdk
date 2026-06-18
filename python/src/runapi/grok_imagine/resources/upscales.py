"""Grok-Imagine video upscale resource.

Takes a prior grok-imagine video source_task_id and upscales it.
"""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class Upscales(Resource):
    """Upscale a previously generated Grok-Imagine video."""

    ENDPOINT = "/api/v1/grok_imagine/upscale_image"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, **params: Any) -> Any:
        """Create a video upscale task and poll until it completes.

        Args:
            **params: Video upscale parameters (model, prompt, ...).

        Returns:
            The completed video upscale response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a video upscale task and return immediately with an id.

        Args:
            **params: Video upscale parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a video upscale task.

        Args:
            id: Task id.

        Returns:
            The current video upscale status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if not params.get("source_task_id"):
            raise ValidationError("source_task_id is required")
