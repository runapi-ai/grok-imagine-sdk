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
          raise Core::ValidationError, "model is required" unless param(params, :model) == Types::TEXT_TO_VIDEO_MODEL
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
          validate_optional!(params, :aspect_ratio, Types::ASPECT_RATIOS)
          validate_optional!(params, :motion_style, Types::MOTION_STYLES)
          validate_optional!(params, :output_resolution, Types::RESOLUTIONS)

          duration_seconds = param(params, :duration_seconds)
          if duration_seconds
            int = duration_seconds.to_i
            unless Types::DURATION_RANGE.cover?(int)
              raise Core::ValidationError, "duration_seconds must be an integer between 6 and 30"
            end
          end
        end
      end
    end
  end
end
