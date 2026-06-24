# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Types
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
