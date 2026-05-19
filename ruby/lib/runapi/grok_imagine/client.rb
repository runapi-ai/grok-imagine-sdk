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
    #     resolution: "720p"
    #   )
    class Client
      # @return [Resources::TextToVideo]
      attr_reader :text_to_video
      # @return [Resources::ImageToVideo]
      attr_reader :image_to_video
      # @return [Resources::TextToImage]
      attr_reader :text_to_image
      # @return [Resources::ImageToImage]
      attr_reader :image_to_image
      # @return [Resources::Extensions]
      attr_reader :extensions
      # @return [Resources::Upscales]
      attr_reader :upscales

      def initialize(api_key: nil, **options)
        @api_key = Core::Auth.resolve_api_key(api_key)

        client_options = Core::ClientOptions.new(api_key: @api_key, **options)
        http = client_options.http_client || Core::HttpClient.new(client_options)

        @text_to_video = Resources::TextToVideo.new(http)
        @image_to_video = Resources::ImageToVideo.new(http)
        @text_to_image = Resources::TextToImage.new(http)
        @image_to_image = Resources::ImageToImage.new(http)
        @extensions = Resources::Extensions.new(http)
        @upscales = Resources::Upscales.new(http)
      end
    end
  end
end
