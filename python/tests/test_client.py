import pytest

from runapi.core import config
from runapi.core.errors import AuthenticationError, ValidationError
from runapi.grok_imagine import GrokImagineClient
from runapi.grok_imagine.resources.edit_image import EditImage
from runapi.grok_imagine.resources.extensions import Extensions
from runapi.grok_imagine.resources.image_to_video import ImageToVideo
from runapi.grok_imagine.resources.segment_map import SegmentMap
from runapi.grok_imagine.resources.text_to_image import TextToImage
from runapi.grok_imagine.resources.text_to_video import TextToVideo
from runapi.grok_imagine.resources.upscales import Upscales
from runapi.grok_imagine.types import (
    CompletedImageTaskResponse,
    CompletedSegmentMapTaskResponse,
    CompletedVideoTaskResponse,
    ImageTaskResponse,
    SegmentMapTaskResponse,
    VideoTaskResponse,
)


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "pending"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


# --- auth -----------------------------------------------------------------


def test_accepts_api_key_parameter():
    assert isinstance(GrokImagineClient(api_key="k", http_client=FakeHttp()), GrokImagineClient)


def test_falls_back_to_global(monkeypatch):
    monkeypatch.setattr(config, "api_key", "global-key")
    assert isinstance(GrokImagineClient(http_client=FakeHttp()), GrokImagineClient)


def test_falls_back_to_env(monkeypatch):
    monkeypatch.setenv("RUNAPI_API_KEY", "env-key")
    assert isinstance(GrokImagineClient(http_client=FakeHttp()), GrokImagineClient)


def test_raises_without_api_key():
    with pytest.raises(AuthenticationError, match="API key is required"):
        GrokImagineClient()


# --- injection / accessors ------------------------------------------------


def test_uses_injected_http_client():
    fake = FakeHttp()
    client = GrokImagineClient(api_key="k", http_client=fake)
    assert client.text_to_video._http is fake
    assert client.image_to_video._http is fake
    assert client.text_to_image._http is fake
    assert client.segment_map._http is fake
    assert client.edit_image._http is fake
    assert client.extensions._http is fake
    assert client.upscales._http is fake


def test_exposes_resource_accessors():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    assert isinstance(client.text_to_video, TextToVideo)
    assert isinstance(client.image_to_video, ImageToVideo)
    assert isinstance(client.text_to_image, TextToImage)
    assert isinstance(client.segment_map, SegmentMap)
    assert isinstance(client.edit_image, EditImage)
    assert isinstance(client.extensions, Extensions)
    assert isinstance(client.upscales, Upscales)


# --- request shapes -------------------------------------------------------


def test_text_to_video_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    result = client.text_to_video.create(
        model="grok-imagine-text-to-video",
        prompt="a neon city",
        output_resolution="720p",
        motion_style=None,
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/text_to_video",
            {"model": "grok-imagine-text-to-video", "prompt": "a neon city", "output_resolution": "720p"},
        ),
    ]
    assert isinstance(result, VideoTaskResponse)


def test_text_to_video_preview_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.text_to_video.create(
        model="grok-imagine-video-1.5-preview",
        prompt="a quiet city rain scene",
        aspect_ratio="auto",
        duration_seconds=15,
        output_resolution="720p",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/text_to_video",
            {
                "model": "grok-imagine-video-1.5-preview",
                "prompt": "a quiet city rain scene",
                "aspect_ratio": "auto",
                "duration_seconds": 15,
                "output_resolution": "720p",
            },
        ),
    ]


def test_text_to_video_fast_create_accepts_durations_below_the_classic_minimum():
    fake = FakeHttp({"id": "t-fast", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.text_to_video.create(
        model="grok-imagine-video-1.5-fast",
        prompt="A paper plane crossing a sunlit room",
        aspect_ratio="16:9",
        duration_seconds=5,
        output_resolution="720p",
        reference_image_urls=["https://cdn.runapi.ai/public/samples/result.png"],
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/text_to_video",
            {
                "model": "grok-imagine-video-1.5-fast",
                "prompt": "A paper plane crossing a sunlit room",
                "aspect_ratio": "16:9",
                "duration_seconds": 5,
                "output_resolution": "720p",
                "reference_image_urls": ["https://cdn.runapi.ai/public/samples/result.png"],
            },
        ),
    ]


def test_text_to_video_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.text_to_video.get("t1")
    assert fake.calls == [("get", "/api/v1/grok_imagine/text_to_video/t1", None)]


def test_text_to_image_create_shape():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    result = client.text_to_image.create(model="grok-imagine-text-to-image", prompt="a fox", aspect_ratio="1:1")
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/text_to_image",
            {"model": "grok-imagine-text-to-image", "prompt": "a fox", "aspect_ratio": "1:1"},
        ),
    ]
    assert isinstance(result, ImageTaskResponse)


def test_image_2_text_to_image_and_segment_map_create_shapes():
    fake = FakeHttp({"id": "image_2"}, {"id": "segment_map"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.text_to_image.create(
        model="grok-imagine-image-2-0", prompt="a fox", aspect_ratio="1:1"
    )
    client.segment_map.create(
        model="grok-imagine-image-2-0", source_task_id="image_2"
    )

    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/text_to_image",
            {"model": "grok-imagine-image-2-0", "prompt": "a fox", "aspect_ratio": "1:1"},
        ),
        (
            "post",
            "/api/v1/grok_imagine/segment_map",
            {"model": "grok-imagine-image-2-0", "source_task_id": "image_2"},
        ),
    ]


def test_segment_map_create_from_image_url_shape():
    fake = FakeHttp({"id": "segment_map"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.segment_map.create(
        model="grok-imagine-image-2-0",
        image_url="https://cdn.runapi.ai/public/samples/image.jpg",
    )

    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/segment_map",
            {"model": "grok-imagine-image-2-0", "image_url": "https://cdn.runapi.ai/public/samples/image.jpg"},
        ),
    ]


def test_image_to_video_create_shape():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.image_to_video.create(
        model="grok-imagine-image-to-video", source_image_url="https://x/a.png"
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/image_to_video",
            {"model": "grok-imagine-image-to-video", "source_image_url": "https://x/a.png"},
        ),
    ]


def test_image_to_video_preview_create_shape():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.image_to_video.create(
        model="grok-imagine-video-1.5-preview",
        source_image_url="https://x/a.png",
        prompt="animate this",
        aspect_ratio="auto",
        duration_seconds=8,
        output_resolution="720p",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/image_to_video",
            {
                "model": "grok-imagine-video-1.5-preview",
                "source_image_url": "https://x/a.png",
                "prompt": "animate this",
                "aspect_ratio": "auto",
                "duration_seconds": 8,
                "output_resolution": "720p",
            },
        ),
    ]


def test_image_to_video_fast_create_shape():
    fake = FakeHttp({"id": "t-fast", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.image_to_video.create(
        model="grok-imagine-video-1.5-fast",
        source_image_url="https://cdn.runapi.ai/public/samples/result.png",
        reference_image_urls=["https://cdn.runapi.ai/public/samples/reference.png"],
        prompt="Animate the still image",
        aspect_ratio="3:2",
        duration_seconds=21,
        output_resolution="720p",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/image_to_video",
            {
                "model": "grok-imagine-video-1.5-fast",
                "source_image_url": "https://cdn.runapi.ai/public/samples/result.png",
                "reference_image_urls": ["https://cdn.runapi.ai/public/samples/reference.png"],
                "prompt": "Animate the still image",
                "aspect_ratio": "3:2",
                "duration_seconds": 21,
                "output_resolution": "720p",
            },
        ),
    ]


def test_edit_image_create_shape():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.edit_image.create(model="grok-imagine-edit-image", source_image_url="https://x/a.png")
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/edit_image",
            {"model": "grok-imagine-edit-image", "source_image_url": "https://x/a.png"},
        ),
    ]


def test_image_2_edit_image_create_shape():
    fake = FakeHttp({"id": "t2"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.edit_image.create(
        model="grok-imagine-image-2-0",
        source_image_urls=["https://x/source.png", "https://x/reference.png"],
        aspect_ratio="16:9",
        prompt="Replace the foreground",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/edit_image",
            {
                "model": "grok-imagine-image-2-0",
                "source_image_urls": ["https://x/source.png", "https://x/reference.png"],
                "aspect_ratio": "16:9",
                "prompt": "Replace the foreground",
            },
        ),
    ]


def test_image_2_edit_image_prompt_is_optional():
    fake = FakeHttp({"id": "t2"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.edit_image.create(
        model="grok-imagine-image-2-0",
        source_image_urls=["https://x/source.png"],
        aspect_ratio="auto",
    )
    assert fake.calls[0][2] == {
        "model": "grok-imagine-image-2-0",
        "source_image_urls": ["https://x/source.png"],
        "aspect_ratio": "auto",
    }


def test_segment_map_run_returns_typed_segments():
    fake = FakeHttp(
        {"id": "segment_map_1"},
        {
            "id": "segment_map_1",
            "status": "completed",
            "segments": [{"url": "https://file.runapi.ai/segment.png", "name": "subject", "index": 1}],
        },
    )
    client = GrokImagineClient(api_key="k", http_client=fake)

    result = client.segment_map.run(model="grok-imagine-image-2-0", source_task_id="image_2")

    assert isinstance(result, CompletedSegmentMapTaskResponse)
    assert result.segments[0].url == "https://file.runapi.ai/segment.png"
    assert result.segments[0].name == "subject"
    assert result.segments[0].index == 1


def test_extensions_create_uses_task_id_shape():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    result = client.extensions.create(
        source_task_id="src_1",
        prompt="keep going",
        start_seconds=4,
        extension_duration_seconds=6,
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/grok_imagine/extend_video",
            {
                "source_task_id": "src_1",
                "prompt": "keep going",
                "start_seconds": 4,
                "extension_duration_seconds": 6,
            },
        ),
    ]
    assert isinstance(result, VideoTaskResponse)


def test_upscales_create_uses_task_id_shape():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.upscales.create(source_task_id="src_1")
    assert fake.calls == [
        ("post", "/api/v1/grok_imagine/upscale_image", {"source_task_id": "src_1"}),
    ]


def test_text_to_video_non_numeric_duration_raises_validation_error():
    # Regression: a non-numeric duration must raise the SDK's ValidationError,
    # not a bare ValueError from int(). Fails if int() is unguarded again.
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="duration_seconds must be an integer"):
        client.text_to_video.create(
            model="grok-imagine-text-to-video", prompt="a fox", duration_seconds="6s"
        )


def test_image_to_video_non_numeric_index_raises_validation_error():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="index must be an integer"):
        client.image_to_video.create(
            model="grok-imagine-image-to-video", source_task_id="src_1", index="abc"
        )


def test_extensions_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.extensions.get("t1")
    assert fake.calls == [("get", "/api/v1/grok_imagine/extend_video/t1", None)]


# --- run narrowing --------------------------------------------------------


def test_run_narrows_completed_video_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "videos": [{"url": "https://x/y.mp4"}]},
    )
    client = GrokImagineClient(api_key="k", http_client=fake)
    result = client.text_to_video.run(model="grok-imagine-text-to-video", prompt="a lake")
    assert isinstance(result, CompletedVideoTaskResponse)
    assert result.videos[0].url == "https://x/y.mp4"


def test_run_narrows_completed_image_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "images": [{"url": "https://x/y.png"}]},
    )
    client = GrokImagineClient(api_key="k", http_client=fake)
    result = client.text_to_image.run(model="grok-imagine-text-to-image", prompt="a fox")
    assert isinstance(result, CompletedImageTaskResponse)
    assert result.images[0].url == "https://x/y.png"


# --- validation -----------------------------------------------------------


def test_text_to_video_requires_model():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model must be one of: grok-imagine-text-to-video"):
        client.text_to_video.create(prompt="hi")


def test_text_to_video_requires_prompt():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="prompt is required"):
        client.text_to_video.create(model="grok-imagine-text-to-video")


def test_text_to_video_rejects_bad_aspect_ratio():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match=r"aspect_ratio must be one of: 2:3, 3:2, 1:1, 16:9, 9:16"):
        client.text_to_video.create(model="grok-imagine-text-to-video", prompt="hi", aspect_ratio="4:5")


def test_text_to_video_duration_range():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="duration_seconds must be an integer between 6 and 30"):
        client.text_to_video.create(model="grok-imagine-text-to-video", prompt="hi", duration_seconds=99)


def test_text_to_video_preview_duration_range():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="duration_seconds must be between 1 and 15"):
        client.text_to_video.create(model="grok-imagine-video-1.5-preview", prompt="hi", duration_seconds=16)


def test_image_to_video_requires_a_source():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="One of source_image_url or source_task_id is required"):
        client.image_to_video.create(model="grok-imagine-image-to-video")


def test_image_to_video_rejects_both_sources():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="not both"):
        client.image_to_video.create(
            model="grok-imagine-image-to-video",
            source_image_url="https://x/a.png",
            source_task_id="src_1",
        )


def test_image_to_video_index_range():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="index must be an integer between 0 and 5"):
        client.image_to_video.create(
            model="grok-imagine-image-to-video", source_task_id="src_1", index=9
        )


def test_image_to_video_index_rejects_bool():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="index must be an integer"):
        client.image_to_video.create(
            model="grok-imagine-image-to-video", source_task_id="src_1", index=True
        )


def test_image_to_video_spicy_requires_source_task_id():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="spicy motion_style requires a source_task_id source image."):
        client.image_to_video.create(
            model="grok-imagine-image-to-video",
            source_image_url="https://x/a.png",
            motion_style="spicy",
        )


def test_edit_image_requires_source_image_url():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_url is required"):
        client.edit_image.create(model="grok-imagine-edit-image")


def test_image_2_edit_image_requires_source_image_urls():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_urls is required"):
        client.edit_image.create(model="grok-imagine-image-2-0", aspect_ratio="1:1")


@pytest.mark.parametrize(
    "source_image_urls",
    [[], [f"https://x/{index}.png" for index in range(6)]],
)
def test_image_2_edit_image_enforces_source_image_count(source_image_urls):
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(
        ValidationError,
        match="source_image_urls must contain between 1 and 5 items",
    ):
        client.edit_image.create(
            model="grok-imagine-image-2-0",
            source_image_urls=source_image_urls,
            aspect_ratio="1:1",
        )


def test_image_2_edit_image_requires_source_image_array():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_urls must be an array"):
        client.edit_image.create(
            model="grok-imagine-image-2-0",
            source_image_urls="https://x/source.png",
            aspect_ratio="1:1",
        )


def test_image_2_edit_image_requires_aspect_ratio():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="aspect_ratio is required"):
        client.edit_image.create(
            model="grok-imagine-image-2-0",
            source_image_urls=["https://x/source.png"],
        )


def test_image_2_edit_image_rejects_bad_aspect_ratio():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="aspect_ratio must be one of"):
        client.edit_image.create(
            model="grok-imagine-image-2-0",
            source_image_urls=["https://x/source.png"],
            aspect_ratio="4:5",
        )


def test_image_2_edit_image_rejects_segment_edit_fields():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(
        ValidationError,
        match="source_task_id is not allowed when model is grok-imagine-image-2-0",
    ):
        client.edit_image.create(
            model="grok-imagine-image-2-0",
            source_task_id="segment_map_1",
            mask_indices=[1],
            source_image_urls=["https://x/source.png"],
            aspect_ratio="1:1",
        )


def test_extensions_requires_source_task_id():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_task_id is required"):
        client.extensions.create(prompt="go", start_seconds=4, extension_duration_seconds=6)


def test_extensions_rejects_bad_duration():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="extension_duration_seconds must be one of: 6, 10"):
        client.extensions.create(
            source_task_id="src_1", prompt="go", start_seconds=4, extension_duration_seconds=8
        )


def test_upscales_requires_source_task_id():
    client = GrokImagineClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_task_id is required"):
        client.upscales.create()


def test_extensions_accepts_zero_start_seconds():
    # Regression: start_seconds=0 (extend from the beginning) is valid and must
    # not be rejected as missing by a truthiness check.
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = GrokImagineClient(api_key="k", http_client=fake)
    client.extensions.create(
        source_task_id="src_1", prompt="go", start_seconds=0, extension_duration_seconds=6
    )
    _, _, body = fake.calls[0]
    assert body["start_seconds"] == 0
