# frozen_string_literal: true

require "runapi/core"
require_relative "grok_imagine/types"
require_relative "grok_imagine/resources/text_to_video"
require_relative "grok_imagine/resources/image_to_video"
require_relative "grok_imagine/resources/text_to_image"
require_relative "grok_imagine/resources/edit_image"
require_relative "grok_imagine/resources/extensions"
require_relative "grok_imagine/resources/upscales"
require_relative "grok_imagine/client"

module RunApi
  module GrokImagine
    AuthenticationError = RunApi::Core::AuthenticationError
    RateLimitError = RunApi::Core::RateLimitError
    InsufficientCreditsError = RunApi::Core::InsufficientCreditsError
    NotFoundError = RunApi::Core::NotFoundError
    ValidationError = RunApi::Core::ValidationError
    TaskFailedError = RunApi::Core::TaskFailedError
    TaskTimeoutError = RunApi::Core::TaskTimeoutError
  end
end
