"""Grok-Imagine text-to-video generation resource."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import Resource, RequestOptions

from ..types import (
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class TextToVideo(Resource):
    """Generate videos from text prompts with Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/text_to_video"
    CLASSIC_MODEL = "grok-imagine-text-to-video"

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
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a text-to-video task.

        Args:
            id: Task id.

        Returns:
            The current text-to-video status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
