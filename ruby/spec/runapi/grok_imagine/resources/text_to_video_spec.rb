# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Resources::TextToVideo do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/grok_imagine/text_to_video" }

  it "posts happy path and returns VideoTaskResponse" do
    params = {
      model: "grok-imagine-text-to-video",
      prompt: "A drifting camera",
      motion_style: "fun",
      output_resolution: "720p",
      enable_safety_checker: true
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params)
      .and_return("id" => "task-t2v-1")

    result = resource.create(**params)
    expect(result).to be_a(RunApi::GrokImagine::Types::VideoTaskResponse)
    expect(result.id).to eq("task-t2v-1")
  end

  it "posts preview params without legacy fields" do
    params = {
      model: "grok-imagine-video-1.5-preview",
      prompt: "A quiet city rain scene",
      aspect_ratio: "auto",
      duration_seconds: 15,
      output_resolution: "720p"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params)
      .and_return("id" => "task-preview")

    resource.create(**params)
  end

  it "posts fast params with durations below the classic minimum" do
    params = {
      model: "grok-imagine-video-1.5-fast",
      prompt: "A paper plane crossing a sunlit room",
      aspect_ratio: "16:9",
      duration_seconds: 5,
      output_resolution: "720p",
      reference_image_urls: ["https://cdn.runapi.ai/public/samples/result.png"]
    }
    expect(http).to receive(:request).with(:post, endpoint, body: params)
      .and_return("id" => "task-fast")

    resource.create(**params)
  end
end
