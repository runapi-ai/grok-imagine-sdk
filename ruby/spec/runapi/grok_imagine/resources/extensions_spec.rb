# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::Extensions do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/extend_video" }

  it "posts happy path" do
    params = {
      source_task_id: "prior-video",
      prompt: "Continue the shot",
      start_seconds: 0,
      extension_duration_seconds: 6
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "ext-1")
    resource.create(**params)
  end
end
