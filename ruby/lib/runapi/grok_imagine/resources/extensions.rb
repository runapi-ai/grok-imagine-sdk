# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine video extension resource.
      # Takes a prior grok-imagine video source_task_id and extends it.
      class Extensions
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/extend_video"

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
          raise Core::ValidationError, "source_task_id is required" unless param(params, :source_task_id)
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
          raise Core::ValidationError, "start_seconds is required" unless param(params, :start_seconds)

          extension_duration_seconds = param(params, :extension_duration_seconds)
          raise Core::ValidationError, "extension_duration_seconds is required" unless extension_duration_seconds
          unless Types::EXTENSION_DURATION_SECONDS.include?(extension_duration_seconds)
            raise Core::ValidationError, "extension_duration_seconds must be one of: #{Types::EXTENSION_DURATION_SECONDS.join(", ")}"
          end
        end
      end
    end
  end
end
