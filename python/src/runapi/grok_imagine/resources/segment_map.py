"""Grok-Imagine Image 2.0 segment-map resource."""

from __future__ import annotations

from typing import Any, Dict, Optional

from runapi.core import Resource, RequestOptions

from ..contract_gen import CONTRACT
from ..types import CompletedSegmentMapTaskResponse, SegmentMapTaskResponse


class SegmentMap(Resource):
    """Create an editable segmentation map from an Image 2.0 text-to-image task."""

    ENDPOINT = "/api/v1/grok_imagine/segment_map"

    RESPONSE_CLASS = SegmentMapTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedSegmentMapTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)

    def _validate_params(self, params: Dict[str, Any]) -> None:
        self._validate_contract(CONTRACT["segment-map"], params)
