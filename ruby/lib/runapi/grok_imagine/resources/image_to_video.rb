# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine image-to-video generation resource.
      # Accepts either external source_image_urls or a prior text-to-image source_task_id (+ index).
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

          source_image_urls = param(params, :source_image_urls)
          source_task_id = param(params, :source_task_id)

          if source_image_urls && !source_image_urls.empty? && source_task_id
            raise Core::ValidationError, "Provide either source_image_urls or source_task_id, not both"
          end
          if (!source_image_urls || source_image_urls.empty?) && !source_task_id
            raise Core::ValidationError, "One of source_image_urls or source_task_id is required"
          end
          if source_image_urls && source_image_urls.size > 1
            raise Core::ValidationError, "source_image_urls supports at most 1 entry"
          end

          if source_task_id && (index = param(params, :index))
            int = index.to_i
            unless Types::INDEX_RANGE.cover?(int)
              raise Core::ValidationError, "index must be an integer between 0 and 5"
            end
          end

          validate_optional!(params, :aspect_ratio, Types::ASPECT_RATIOS)
          validate_optional!(params, :motion_style, Types::MOTION_STYLES)
          validate_optional!(params, :output_resolution, Types::RESOLUTIONS)

          if param(params, :motion_style).to_s == "spicy" && source_image_urls && !source_image_urls.empty?
            raise Core::ValidationError, "spicy motion_style requires a source_task_id source image."
          end
        end
      end
    end
  end
end
