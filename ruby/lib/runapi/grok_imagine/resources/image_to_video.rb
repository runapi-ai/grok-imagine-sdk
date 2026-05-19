# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine image-to-video generation resource.
      # Accepts either external image_urls or a prior text-to-image task_id (+ index).
      class ImageToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/image_to_video"

        RESPONSE_CLASS = Types::VideoTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedVideoTaskResponse

        def initialize(http)
          @http = http
        end

        def run(**params)
          task = create(**params)
          poll_until_complete { get(task.id) }
        end

        def create(**params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end

        private

        def validate_params!(params)
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::IMAGE_TO_VIDEO_MODEL

          image_urls = param(params, :image_urls)
          task_id = param(params, :task_id)

          if image_urls && !image_urls.empty? && task_id
            raise Core::ValidationError, "Provide either image_urls or task_id, not both"
          end
          if (!image_urls || image_urls.empty?) && !task_id
            raise Core::ValidationError, "One of image_urls or task_id is required"
          end
          if image_urls && image_urls.size > 1
            raise Core::ValidationError, "image_urls supports at most 1 entry"
          end

          if task_id && (index = param(params, :index))
            int = index.to_i
            unless Types::INDEX_RANGE.cover?(int)
              raise Core::ValidationError, "index must be an integer between 0 and 5"
            end
          end

          validate_optional!(params, :aspect_ratio, Types::ASPECT_RATIOS)
          validate_optional!(params, :mode, Types::MODES)
          validate_optional!(params, :resolution, Types::RESOLUTIONS)

          if param(params, :mode).to_s == "spicy" && image_urls && !image_urls.empty?
            raise Core::ValidationError, "Spicy mode is not available with image_urls; use task_id instead"
          end
        end
      end
    end
  end
end
