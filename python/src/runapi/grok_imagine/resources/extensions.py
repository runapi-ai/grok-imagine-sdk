"""Grok-Imagine video extension resource.

Takes a prior grok-imagine video source_task_id and extends it.
"""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import Resource, RequestOptions

from ..types import (
    CompletedVideoTaskResponse,
    VideoTaskResponse,
)


class Extensions(Resource):
    """Extend a previously generated Grok-Imagine video."""

    ENDPOINT = "/api/v1/grok_imagine/extend_video"

    RESPONSE_CLASS = VideoTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedVideoTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a video extension task and poll until it completes.

        Args:
            **params: Video extension parameters (model, prompt, ...).

        Returns:
            The completed video extension response.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a video extension task and return immediately with an id.

        Args:
            **params: Video extension parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a video extension task.

        Args:
            id: Task id.

        Returns:
            The current video extension status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
