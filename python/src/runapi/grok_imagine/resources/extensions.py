"""Grok-Imagine video extension resource.

Takes a prior grok-imagine video source_task_id and extends it.
"""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    EXTENSION_DURATION_SECONDS,
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class Extensions(Resource):
    """Extend a previously generated Grok-Imagine video."""

    ENDPOINT = "/api/v1/grok_imagine/extend_video"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, **params: Any) -> Any:
        """Create a video extension task and poll until it completes.

        Args:
            **params: Video extension parameters (model, prompt, ...).

        Returns:
            The completed video extension response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a video extension task and return immediately with an id.

        Args:
            **params: Video extension parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a video extension task.

        Args:
            id: Task id.

        Returns:
            The current video extension status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if not params.get("source_task_id"):
            raise ValidationError("source_task_id is required")
        if not params.get("prompt"):
            raise ValidationError("prompt is required")
        # start_seconds is numeric and 0 (extend from the beginning) is valid, so
        # check for absence explicitly rather than treating 0 as missing.
        if params.get("start_seconds") is None:
            raise ValidationError("start_seconds is required")

        extension_duration_seconds = params.get("extension_duration_seconds")
        if not extension_duration_seconds:
            raise ValidationError("extension_duration_seconds is required")
        if extension_duration_seconds not in EXTENSION_DURATION_SECONDS:
            joined = ", ".join(str(value) for value in EXTENSION_DURATION_SECONDS)
            raise ValidationError(f"extension_duration_seconds must be one of: {joined}")
