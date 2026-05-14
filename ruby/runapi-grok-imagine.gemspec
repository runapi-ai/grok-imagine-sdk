# frozen_string_literal: true

Dir.chdir(__dir__) do

  Gem::Specification.new do |spec|
    spec.name = "runapi-grok-imagine"
    spec.version = "0.1.0"
    spec.authors = [ "RunAPI" ]
    spec.email = [ "contact@runapi.ai" ]

    spec.summary = "Grok-Imagine multimodal generation client for RunAPI"
    spec.description = "Ruby SDK for generating images and videos with Grok-Imagine via RunAPI.ai"
    spec.homepage = "https://runapi.ai"
    spec.license = "Apache-2.0"
    spec.required_ruby_version = ">= 3.1.0"

    spec.metadata["homepage_uri"] = spec.homepage
    spec.metadata["source_code_uri"] = "https://github.com/runapi-ai/grok-imagine-sdk"
    spec.metadata["changelog_uri"] = "https://github.com/runapi-ai/grok-imagine-sdk/blob/main/CHANGELOG.md"

    spec.files = Dir.glob("lib/**/*") + %w[LICENSE]
    spec.require_paths = [ "lib" ]

    spec.add_dependency "runapi-core", "~> 0.1"
  end
end
