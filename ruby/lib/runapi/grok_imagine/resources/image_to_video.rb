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
          request(:post, ENDPOINT, body: params, options: options)
        end

        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end
      end
    end
  end
end
