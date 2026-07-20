# frozen_string_literal: true

module RunApi
  module GrokImagine
    CONTRACT = {
      "edit-image" => {
        "models" => ["grok-imagine-edit-image"],
        "fields_by_model" => {
          "grok-imagine-edit-image" => {
            "source_image_url" => {
              "required" => true
            }
          }
        }
      },
      "extend" => {
        "models" => [],
        "fields_by_model" => {
          "_" => {
            "extension_duration_seconds" => {
              "enum" => [6, 10],
              "type" => "integer"
            },
            "start_seconds" => {
              "type" => "integer"
            }
          }
        }
      },
      "image-to-video" => {
        "models" => ["grok-imagine-image-to-video", "grok-imagine-video-1.5-fast", "grok-imagine-video-1.5-preview"],
        "fields_by_model" => {
          "grok-imagine-image-to-video" => {
            "aspect_ratio" => {
              "enum" => ["2:3", "3:2", "1:1", "16:9", "9:16"]
            },
            "duration_seconds" => {
              "type" => "integer"
            },
            "index" => {
              "type" => "integer"
            },
            "motion_style" => {
              "enum" => ["fun", "normal", "spicy"]
            },
            "output_resolution" => {
              "enum" => ["480p", "720p"]
            }
          },
          "grok-imagine-video-1.5-fast" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:2", "2:3"]
            },
            "duration_seconds" => {
              "min" => 1,
              "max" => 30,
              "type" => "integer"
            },
            "index" => {
              "type" => "integer"
            },
            "output_resolution" => {
              "enum" => ["480p", "720p"]
            },
            "source_image_url" => {
              "required" => true
            }
          },
          "grok-imagine-video-1.5-preview" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:2", "2:3", "auto"]
            },
            "duration_seconds" => {
              "min" => 1,
              "max" => 15,
              "type" => "integer"
            },
            "index" => {
              "type" => "integer"
            },
            "output_resolution" => {
              "enum" => ["480p", "720p"]
            },
            "prompt" => {
              "min" => 1,
              "max" => 4096,
              "length" => true
            },
            "source_image_url" => {
              "required" => true
            }
          }
        },
        "rules" => [{
          "when" => {
            "model" => "grok-imagine-image-to-video"
          },
          "forbidden" => ["reference_image_urls"]
        }, {
          "when" => {
            "model" => "grok-imagine-video-1.5-fast"
          },
          "forbidden" => ["source_task_id", "index", "motion_style", "enable_safety_checker"]
        }, {
          "when" => {
            "model" => "grok-imagine-video-1.5-preview"
          },
          "forbidden" => ["source_task_id", "index", "reference_image_urls", "motion_style", "enable_safety_checker"]
        }]
      },
      "text-to-image" => {
        "models" => ["grok-imagine-text-to-image"],
        "fields_by_model" => {
          "grok-imagine-text-to-image" => {
            "aspect_ratio" => {
              "enum" => ["2:3", "3:2", "1:1", "16:9", "9:16"]
            }
          }
        }
      },
      "text-to-video" => {
        "models" => ["grok-imagine-text-to-video", "grok-imagine-video-1.5-fast", "grok-imagine-video-1.5-preview"],
        "fields_by_model" => {
          "grok-imagine-text-to-video" => {
            "aspect_ratio" => {
              "enum" => ["2:3", "3:2", "1:1", "16:9", "9:16"]
            },
            "duration_seconds" => {
              "type" => "integer"
            },
            "motion_style" => {
              "enum" => ["fun", "normal", "spicy"]
            },
            "output_resolution" => {
              "enum" => ["480p", "720p"]
            }
          },
          "grok-imagine-video-1.5-fast" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:2", "2:3"]
            },
            "duration_seconds" => {
              "min" => 1,
              "max" => 30,
              "type" => "integer"
            },
            "output_resolution" => {
              "enum" => ["480p", "720p"]
            },
            "prompt" => {
              "required" => true
            }
          },
          "grok-imagine-video-1.5-preview" => {
            "aspect_ratio" => {
              "enum" => ["1:1", "16:9", "9:16", "3:2", "2:3", "auto"]
            },
            "duration_seconds" => {
              "min" => 1,
              "max" => 15,
              "type" => "integer"
            },
            "output_resolution" => {
              "enum" => ["480p", "720p"]
            },
            "prompt" => {
              "required" => true,
              "min" => 1,
              "max" => 4096,
              "length" => true
            }
          }
        },
        "rules" => [{
          "when" => {
            "model" => "grok-imagine-text-to-video"
          },
          "forbidden" => ["reference_image_urls"]
        }, {
          "when" => {
            "model" => "grok-imagine-video-1.5-fast"
          },
          "forbidden" => ["motion_style", "enable_safety_checker"]
        }, {
          "when" => {
            "model" => "grok-imagine-video-1.5-preview"
          },
          "forbidden" => ["reference_image_urls", "motion_style", "enable_safety_checker"]
        }]
      },
      "upscale-image" => {
        "models" => [],
        "fields_by_model" => {
          "_" => {}
        }
      }
    }.freeze
  end
end
