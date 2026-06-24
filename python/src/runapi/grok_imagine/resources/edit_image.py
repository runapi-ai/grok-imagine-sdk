"""Grok-Imagine prompt-guided image editing resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..contract_gen import CONTRACT
from ..types import (
    CompletedImageTaskResponse,
    ImageTaskResponse,
)


class EditImage(Resource):
    """Edit an image with a text prompt using Grok-Imagine."""

    ENDPOINT = "/api/v1/grok_imagine/edit_image"

    RESPONSE_CLASS = ImageTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedImageTaskResponse

    def run(self, **params: Any) -> Any:
        """Create an edit-image task and poll until it completes.

        Args:
            **params: Edit-image parameters (model, prompt, ...).

        Returns:
            The completed edit-image response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create an edit-image task and return immediately with an id.

        Args:
            **params: Edit-image parameters (model, prompt, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of an edit-image task.

        Args:
            id: Task id.

        Returns:
            The current edit-image status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        self._validate_contract(CONTRACT["edit-image"], params)
        if not params.get("source_image_url"):
            raise ValidationError("source_image_url is required")
