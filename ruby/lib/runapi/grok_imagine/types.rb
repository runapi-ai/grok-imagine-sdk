# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Types
      TEXT_TO_VIDEO_MODEL = "grok-imagine-text-to-video"
      IMAGE_TO_VIDEO_MODEL = "grok-imagine-image-to-video"
      TEXT_TO_IMAGE_MODEL = "grok-imagine-text-to-image"
      EDIT_IMAGE_MODEL = "grok-imagine-edit-image"

      ASPECT_RATIOS = %w[2:3 3:2 1:1 16:9 9:16].freeze
      MOTION_STYLES = %w[fun normal spicy].freeze
      RESOLUTIONS = %w[480p 720p].freeze
      DURATION_RANGE = (6..30)
      EXTENSION_DURATION_SECONDS = [6, 10].freeze
      INDEX_RANGE = (0..5)

      class MediaUrl < RunApi::Core::BaseModel
        optional :url, String
      end

      class AsyncTaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
      end

      class VideoTaskResponse < AsyncTaskResponse
        optional :videos, [-> { MediaUrl }]
        optional :error, String
      end

      class CompletedVideoTaskResponse < VideoTaskResponse
        required :videos, [-> { MediaUrl }]
      end

      class ImageTaskResponse < AsyncTaskResponse
        optional :images, [-> { MediaUrl }]
        optional :error, String
      end

      class CompletedImageTaskResponse < ImageTaskResponse
        required :images, [-> { MediaUrl }]
      end
    end
  end
end
