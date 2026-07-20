# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::ImageToVideo do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/image_to_video" }

  it "accepts source_image_url path" do
    params = {model: "grok-imagine-image-to-video", source_image_url: "https://cdn.runapi.ai/public/samples/result.png"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-1")
    resource.create(**params)
  end

  it "accepts preview source_image_url path" do
    params = {
      model: "grok-imagine-video-1.5-preview",
      source_image_url: "https://cdn.runapi.ai/public/samples/result.png",
      prompt: "animate this",
      aspect_ratio: "auto",
      duration_seconds: 8,
      output_resolution: "720p"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-preview")
    resource.create(**params)
  end

  it "posts fast params with source and reference images" do
    params = {
      model: "grok-imagine-video-1.5-fast",
      source_image_url: "https://cdn.runapi.ai/public/samples/result.png",
      reference_image_urls: ["https://cdn.runapi.ai/public/samples/reference.png"],
      prompt: "Animate the still image",
      aspect_ratio: "3:2",
      duration_seconds: 21,
      output_resolution: "720p"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-fast")

    resource.create(**params)
  end

  it "accepts source_task_id path" do
    params = {model: "grok-imagine-image-to-video", source_task_id: "prior-t2i"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-2")
    resource.create(**params)
  end

  it "rejects when both source_image_url and source_task_id provided" do
    expect {
      resource.create(model: "grok-imagine-image-to-video", source_image_url: "a", source_task_id: "b")
    }.to raise_error(RunApi::Core::ValidationError, /Provide either source_image_url or source_task_id/)
  end

  it "rejects when neither source_image_url nor source_task_id provided" do
    expect { resource.create(model: "grok-imagine-image-to-video") }
      .to raise_error(RunApi::Core::ValidationError, /One of source_image_url or source_task_id is required/)
  end

  it "accepts motion_style with source_task_id path" do
    params = {model: "grok-imagine-image-to-video", source_task_id: "prior-t2i", motion_style: "spicy"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "t-3")
    resource.create(**params)
  end

  it "rejects spicy motion_style with source_image_url" do
    expect {
      resource.create(model: "grok-imagine-image-to-video", source_image_url: "a", motion_style: "spicy")
    }.to raise_error(RunApi::Core::ValidationError, /spicy motion_style requires a source_task_id/)
  end
end
