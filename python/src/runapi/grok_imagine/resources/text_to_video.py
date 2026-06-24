"""Grok-Imagine text-to-video generation resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..contract_gen import CONTRACT
from ..types import (
    DURATION_RANGE,
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class TextToVideo(Resource):
    """Generate videos from text prompts with Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/text_to_video"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, **params: Any) -> Any:
        """Create a text-to-video task and poll until it completes.

        Args:
            **params: Text-to-video parameters (model, prompt, ...).

        Returns:
            The completed text-to-video response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a text-to-video task and return immediately with an id.

        Args:
            **params: Text-to-video parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a text-to-video task.

        Args:
            id: Task id.

        Returns:
            The current text-to-video status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        self._validate_contract(CONTRACT["text-to-video"], params)
        if not params.get("prompt"):
            raise ValidationError("prompt is required")

        duration_seconds = params.get("duration_seconds")
        if duration_seconds:
            try:
                value = int(duration_seconds)
            except (TypeError, ValueError):
                value = None
            if value is None or value not in DURATION_RANGE:
                raise ValidationError("duration_seconds must be an integer between 6 and 30")
