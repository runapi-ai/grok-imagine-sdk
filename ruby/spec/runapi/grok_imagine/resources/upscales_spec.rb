# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::Upscales do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/upscale_image" }

  it "posts happy path" do
    expect(http).to receive(:request).with(:post, endpoint, body: {source_task_id: "prior-video"})
      .and_return("id" => "ups-1")
    resource.create(source_task_id: "prior-video")
  end

  it "rejects missing source_task_id" do
    expect { resource.create }
      .to raise_error(RunApi::Core::ValidationError, /source_task_id is required/)
  end
end
