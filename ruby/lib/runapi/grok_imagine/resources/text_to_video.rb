# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine text-to-video generation resource.
      class TextToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/text_to_video"

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
          validate_contract!(CONTRACT["text-to-video"], params)
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)

          duration_seconds = param(params, :duration_seconds)
          if duration_seconds
            int = duration_seconds.to_i
            range = case param(params, :model)
            when Types::FAST_MODEL
              Types::FAST_DURATION_RANGE
            when Types::PREVIEW_MODEL
              Types::PREVIEW_DURATION_RANGE
            else
              Types::DURATION_RANGE
            end
            unless range.cover?(int)
              raise Core::ValidationError, "duration_seconds must be an integer between #{range.min} and #{range.max}"
            end
          end
        end
      end
    end
  end
end
