# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::GrokImagine::Client do
  before do
    allow(ConnectionPool).to receive(:new).and_return(instance_double(ConnectionPool))
  end

  after { RunApi.api_key = nil }

  it "exposes all seven resource accessors" do
    client = described_class.new(api_key: "test-key")
    expect(client.text_to_video).to be_a(RunApi::GrokImagine::Resources::TextToVideo)
    expect(client.image_to_video).to be_a(RunApi::GrokImagine::Resources::ImageToVideo)
    expect(client.text_to_image).to be_a(RunApi::GrokImagine::Resources::TextToImage)
    expect(client.segment_map).to be_a(RunApi::GrokImagine::Resources::SegmentMap)
    expect(client.edit_image).to be_a(RunApi::GrokImagine::Resources::EditImage)
    expect(client.extensions).to be_a(RunApi::GrokImagine::Resources::Extensions)
    expect(client.upscales).to be_a(RunApi::GrokImagine::Resources::Upscales)
  end
end
