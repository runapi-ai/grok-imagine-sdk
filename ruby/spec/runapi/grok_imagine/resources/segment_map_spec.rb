# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::SegmentMap do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/segment_map" }

  it "POSTs an Image 2.0 text-to-image task id" do
    params = {model: "grok-imagine-image-2-0", source_task_id: "image-2-task"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "segment-map-task")

    resource.create(**params)
  end

  it "rejects a missing source task id" do
    expect { resource.create(model: "grok-imagine-image-2-0") }
      .to raise_error(RunApi::Core::ValidationError, /source_task_id is required/)
  end

  it "returns typed segments from completed tasks" do
    params = {model: "grok-imagine-image-2-0", source_task_id: "image-2-task"}
    expect(http).to receive(:request).with(:post, endpoint, body: params)
      .and_return("id" => "segment-map-task")
    expect(http).to receive(:request).with(:get, "#{endpoint}/segment-map-task")
      .and_return(
        "id" => "segment-map-task",
        "status" => "completed",
        "segments" => [{"url" => "https://file.runapi.ai/segment.png", "name" => "subject", "index" => 1}]
      )

    result = resource.run(**params)

    expect(result).to be_a(RunApi::GrokImagine::Types::CompletedSegmentMapTaskResponse)
    expect(result.segments.first.name).to eq("subject")
    expect(result.segments.first.index).to eq(1)
  end
end
