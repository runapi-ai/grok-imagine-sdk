# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine text-to-image generation resource.
      class TextToImages
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/text_to_images"

        RESPONSE_CLASS = Types::ImageTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedImageTaskResponse

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
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::TEXT_TO_IMAGE_MODEL
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
          validate_optional!(params, :aspect_ratio, Types::ASPECT_RATIOS)
        end
      end
    end
  end
end
