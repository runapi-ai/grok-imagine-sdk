# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine prompt-guided image editing resource.
      class EditImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/edit_image"

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
          validate_contract!(CONTRACT["edit-image"], params)
          raise Core::ValidationError, "source_image_url is required" unless param(params, :source_image_url)
        end
      end
    end
  end
end
