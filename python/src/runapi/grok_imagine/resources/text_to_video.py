"""Grok-Imagine text-to-video generation resource."""

from __future__ import annotations

from typing import Any, Dict, Optional

from runapi.core import Resource, ValidationError, RequestOptions

from ..contract_gen import CONTRACT
from ..types import (
    DURATION_RANGE,
    PREVIEW_DURATION_RANGE,
    PREVIEW_MODEL,
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class TextToVideo(Resource):
    """Generate videos from text prompts with Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/text_to_video"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a text-to-video task and poll until it completes.

        Args:
            **params: Text-to-video parameters (model, prompt, ...).

        Returns:
            The completed text-to-video response.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a text-to-video task and return immediately with an id.

        Args:
            **params: Text-to-video parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a text-to-video task.

        Args:
            id: Task id.

        Returns:
            The current text-to-video status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)

    def _validate_params(self, params: Dict[str, Any]) -> None:
        self._validate_contract(CONTRACT["text-to-video"], params)
        if not params.get("prompt"):
            raise ValidationError("prompt is required")

        duration_seconds = params.get("duration_seconds")
        if duration_seconds:
            duration_range = PREVIEW_DURATION_RANGE if params.get("model") == PREVIEW_MODEL else DURATION_RANGE
            try:
                value = int(duration_seconds)
            except (TypeError, ValueError):
                value = None
            if value is None or value not in duration_range:
                raise ValidationError(
                    f"duration_seconds must be an integer between {duration_range.start} and {duration_range.stop - 1}"
                )
