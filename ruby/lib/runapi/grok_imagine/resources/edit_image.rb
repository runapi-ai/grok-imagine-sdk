# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Prompt-guided image editing resource.
      #
      # Image 2.0 accepts one to five source_image_urls, an aspect_ratio, and
      # an optional prompt. The original model continues to accept a single
      # source_image_url and prompt.
      class EditImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/edit_image"

        RESPONSE_CLASS = Types::ImageTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedImageTaskResponse

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
          validate_contract!(CONTRACT["edit-image"], params)
        end
      end
    end
  end
end
