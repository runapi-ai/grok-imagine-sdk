# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine image-to-image generation resource.
      class ImageToImages
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/image_to_images"

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
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::IMAGE_TO_IMAGE_MODEL
          image_urls = param(params, :image_urls)
          unless image_urls && !image_urls.empty?
            raise Core::ValidationError, "image_urls is required"
          end
          if image_urls.size > 1
            raise Core::ValidationError, "image_urls supports at most 1 entry"
          end
        end
      end
    end
  end
end
