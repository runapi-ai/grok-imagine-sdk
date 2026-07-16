# frozen_string_literal: true

Dir.chdir(__dir__) do

  Gem::Specification.new do |spec|
    spec.name = "runapi-grok-imagine"
    spec.version = "0.2.8"
    spec.metadata["runapi_slug"] = "grok-imagine"
    spec.authors = ["RunAPI"]
    spec.email = ["contact@runapi.ai"]

    spec.summary = "Grok Imagine Ruby SDK for RunAPI"
    spec.description = "The Grok Imagine Ruby SDK is the language-specific package for Grok Imagine on RunAPI. Use this package for image and video generation, image editing, and creative production workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Ruby."
    spec.homepage = "https://runapi.ai/models/grok-imagine"
    spec.license = "Apache-2.0"
    spec.required_ruby_version = ">= 3.1.0"
    spec.metadata["homepage_uri"] = "https://runapi.ai/models/grok-imagine"
    spec.metadata["documentation_uri"] = "https://github.com/runapi-ai/grok-imagine-sdk/blob/main/ruby/README.md"
    spec.metadata["source_code_uri"] = "https://github.com/runapi-ai/grok-imagine-sdk"
    spec.metadata["bug_tracker_uri"] = "https://github.com/runapi-ai/grok-imagine-sdk/issues"
    spec.metadata["changelog_uri"] = "https://github.com/runapi-ai/grok-imagine-sdk/blob/main/CHANGELOG.md"


    spec.files = Dir.glob("lib/**/*") + %w[LICENSE README.md]
    spec.extra_rdoc_files = ["README.md"]
        spec.require_paths = ["lib"]

    spec.add_dependency "runapi-core", "~> 0.2.11"
  end
end
