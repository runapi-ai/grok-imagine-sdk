# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::EditImage do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/edit_image" }

  it "POSTs source_image_url to the correct endpoint" do
    params = {model: "grok-imagine-edit-image", source_image_url: "https://cdn.runapi.ai/public/samples/source.png", prompt: "Restyle"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-1")
    resource.create(**params)
  end

  it "rejects missing source_image_url" do
    expect {
      resource.create(model: "grok-imagine-edit-image")
    }.to raise_error(RunApi::Core::ValidationError, /source_image_url is required/)
  end

  it "POSTs an Image 2.0 edit from a segment-map task" do
    params = {
      model: "grok-imagine-image-2-0",
      source_task_id: "segment-map-task",
      mask_indices: [1, 3],
      prompt: "Replace the foreground"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-2")

    resource.create(**params)
  end
end
