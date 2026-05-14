# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Grok-Imagine video extension resource.
      # Takes a prior grok-imagine video task_id and extends it.
      class Extensions
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/extensions"

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
          raise Core::ValidationError, "task_id is required" unless param(params, :task_id)
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
          raise Core::ValidationError, "extend_at is required" unless param(params, :extend_at)

          extend_times = param(params, :extend_times)
          raise Core::ValidationError, "extend_times is required" unless extend_times
          unless Types::EXTEND_TIMES.include?(extend_times.to_s)
            raise Core::ValidationError, "extend_times must be one of: #{Types::EXTEND_TIMES.join(", ")}"
          end
        end
      end
    end
  end
end
