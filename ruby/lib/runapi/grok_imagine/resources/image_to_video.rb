# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine image-to-video generation resource.
      # Accepts either an external source_image_url or a prior text-to-image source_task_id (+ index).
      class ImageToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/image_to_video"

        RESPONSE_CLASS = Types::VideoTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedVideoTaskResponse

        def initialize(http)
          @http = http
        end

        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        def create(options: nil, **params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["image-to-video"], params)

          source_image_url = param(params, :source_image_url)
          source_task_id = param(params, :source_task_id)

          if source_image_url && source_task_id
            raise Core::ValidationError, "Provide either source_image_url or source_task_id, not both"
          end
          unless source_image_url || source_task_id
            raise Core::ValidationError, "One of source_image_url or source_task_id is required"
          end

          if source_task_id && (index = param(params, :index))
            int = index.to_i
            unless Types::INDEX_RANGE.cover?(int)
              raise Core::ValidationError, "index must be an integer between 0 and 5"
            end
          end

          if param(params, :motion_style).to_s == "spicy" && source_image_url
            raise Core::ValidationError, "spicy motion_style requires a source_task_id source image."
          end
        end
      end
    end
  end
end
