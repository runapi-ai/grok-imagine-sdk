"""Grok-Imagine client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ClientOptions, HttpClient, resolve_api_key

from .resources.edit_image import EditImage
from .resources.extensions import Extensions
from .resources.image_to_video import ImageToVideo
from .resources.text_to_image import TextToImage
from .resources.text_to_video import TextToVideo
from .resources.upscales import Upscales


class GrokImagineClient:
    """Grok-Imagine multimodal generation client.

    Example::

        client = GrokImagineClient(api_key="sk-...")
        result = client.text_to_video.run(
            model="grok-imagine-text-to-video",
            prompt="A drone shot over a neon cityscape",
            output_resolution="720p",
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        resolved_api_key = resolve_api_key(api_key)
        client_options = ClientOptions(api_key=resolved_api_key, **options)
        http = client_options.http_client or HttpClient(client_options)
        self.text_to_video = TextToVideo(http)
        self.image_to_video = ImageToVideo(http)
        self.text_to_image = TextToImage(http)
        self.edit_image = EditImage(http)
        self.extensions = Extensions(http)
        self.upscales = Upscales(http)
