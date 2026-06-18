# frozen_string_literal: true

module RunApi
  module GrokImagine
    # Grok-Imagine multimodal generation API client.
    #
    # @example
    #   client = RunApi::GrokImagine::Client.new(api_key: "your-api-key")
    #   result = client.text_to_video.run(
    #     model: "grok-imagine-text-to-video",
    #     prompt: "A drone shot over a neon cityscape",
    #     output_resolution: "720p"
    #   )
    class Client < RunApi::Core::Client
      # @return [Resources::TextToVideo] Text-to-video generation operations.
      attr_reader :text_to_video
      # @return [Resources::ImageToVideo] Image-to-video generation operations.
      attr_reader :image_to_video
      # @return [Resources::TextToImage] Text-to-image generation operations.
      attr_reader :text_to_image
      # @return [Resources::EditImage] Prompt-guided image editing operations.
      attr_reader :edit_image
      # @return [Resources::Extensions] Extend a previously generated video.
      attr_reader :extensions
      # @return [Resources::Upscales] Upscale a previously generated video.
      attr_reader :upscales

      def initialize(api_key: nil, **options)
        super

        @text_to_video = Resources::TextToVideo.new(http)
        @image_to_video = Resources::ImageToVideo.new(http)
        @text_to_image = Resources::TextToImage.new(http)
        @edit_image = Resources::EditImage.new(http)
        @extensions = Resources::Extensions.new(http)
        @upscales = Resources::Upscales.new(http)
      end
    end
  end
end
