# frozen_string_literal: true

module RunApi
  module GrokImagine
    module Resources
      # Produces an editable segmentation map from a public image URL.
      # source_task_id remains accepted as a compatibility input; use image_url instead.
      class SegmentMap
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/grok_imagine/segment_map"

        RESPONSE_CLASS = Types::SegmentMapTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedSegmentMapTaskResponse

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
          validate_contract!(CONTRACT["segment-map"], params)
        end
      end
    end
  end
end
