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

  it "rejects missing source_image_url" do
    expect {
      resource.create(model: "grok-imagine-edit-image")
    }.to raise_error(RunApi::Core::ValidationError, /source_image_url is required/)
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

  it "rejects missing Image 2.0 source_image_urls" do
    expect {
      resource.create(model: "grok-imagine-image-2-0", aspect_ratio: "1:1")
    }.to raise_error(RunApi::Core::ValidationError, /source_image_urls is required/)
  end

  it "rejects empty Image 2.0 source_image_urls" do
    expect {
      resource.create(model: "grok-imagine-image-2-0", source_image_urls: [], aspect_ratio: "1:1")
    }.to raise_error(RunApi::Core::ValidationError, /source_image_urls must contain between 1 and 5 items/)
  end

  it "rejects more than five Image 2.0 source_image_urls" do
    expect {
      resource.create(
        model: "grok-imagine-image-2-0",
        source_image_urls: Array.new(6, "https://cdn.runapi.ai/public/samples/source.png"),
        aspect_ratio: "1:1"
      )
    }.to raise_error(RunApi::Core::ValidationError, /source_image_urls must contain between 1 and 5 items/)
  end

  it "rejects non-array Image 2.0 source_image_urls" do
    expect {
      resource.create(
        model: "grok-imagine-image-2-0",
        source_image_urls: "https://cdn.runapi.ai/public/samples/source.png",
        aspect_ratio: "1:1"
      )
    }.to raise_error(RunApi::Core::ValidationError, /source_image_urls must be an array/)
  end

  it "rejects an invalid Image 2.0 aspect_ratio" do
    expect {
      resource.create(
        model: "grok-imagine-image-2-0",
        source_image_urls: ["https://cdn.runapi.ai/public/samples/source.png"],
        aspect_ratio: "4:3"
      )
    }.to raise_error(RunApi::Core::ValidationError, /aspect_ratio must be one of/)
  end

  it "rejects missing Image 2.0 aspect_ratio" do
    expect {
      resource.create(
        model: "grok-imagine-image-2-0",
        source_image_urls: ["https://cdn.runapi.ai/public/samples/source.png"]
      )
    }.to raise_error(RunApi::Core::ValidationError, /aspect_ratio is required/)
  end

  it "rejects deprecated segment-edit fields for Image 2.0" do
    expect {
      resource.create(
        model: "grok-imagine-image-2-0",
        source_image_urls: ["https://cdn.runapi.ai/public/samples/source.png"],
        aspect_ratio: "1:1",
        source_task_id: "segment-map-task",
        mask_indices: [1, 3]
      )
    }.to raise_error(RunApi::Core::ValidationError, /source_task_id is not allowed/)
  end

  it "rejects an Image 2.0 prompt longer than 390000 characters" do
    expect {
      resource.create(
        model: "grok-imagine-image-2-0",
        source_image_urls: ["https://cdn.runapi.ai/public/samples/source.png"],
        aspect_ratio: "1:1",
        prompt: "x" * 390_001
      )
    }.to raise_error(RunApi::Core::ValidationError, /prompt must be at most 390000 characters/)
  end
end
