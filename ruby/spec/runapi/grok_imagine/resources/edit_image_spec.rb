# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::EditImage do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/edit_image" }

  it "POSTs the legacy source_image_url contract to the correct endpoint" do
    params = {model: "grok-imagine-edit-image", source_image_url: "https://cdn.runapi.ai/public/samples/source.png", prompt: "Restyle"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-1")
    resource.create(**params)
  end

  it "POSTs an Image 2.0 direct edit" do
    params = {
      model: "grok-imagine-image-2-0",
      source_image_urls: [
        "https://cdn.runapi.ai/public/samples/source-1.png",
        "https://cdn.runapi.ai/public/samples/source-2.png"
      ],
      aspect_ratio: "16:9",
      prompt: "Replace the foreground"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-2")

    resource.create(**params)
  end

  it "accepts an Image 2.0 direct edit without a prompt" do
    params = {
      model: "grok-imagine-image-2-0",
      source_image_urls: ["https://cdn.runapi.ai/public/samples/source.png"],
      aspect_ratio: "auto"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-3")

    resource.create(**params)
  end
end
