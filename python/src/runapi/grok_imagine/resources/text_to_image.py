"""Grok-Imagine text-to-image generation resource."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import Resource, RequestOptions

from ..types import (
    CompletedImageTaskResponse,
    ImageTaskResponse,
)


class TextToImage(Resource):
    """Generate images from text prompts with Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/text_to_image"

    RESPONSE_CLASS = ImageTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedImageTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a text-to-image task and poll until it completes.

        Args:
            **params: Text-to-image parameters (model, prompt, ...).

        Returns:
            The completed text-to-image response.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a text-to-image task and return immediately with an id.

        Args:
            **params: Text-to-image parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a text-to-image task.

        Args:
            id: Task id.

        Returns:
            The current text-to-image status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
