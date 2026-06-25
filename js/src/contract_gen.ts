export const contract = {
  "edit-image": {
    "models": [
      "grok-imagine-edit-image"
    ],
    "fields_by_model": {
      "grok-imagine-edit-image": {}
    }
  },
  "extend": {
    "models": [],
    "fields_by_model": {
      "_": {
        "extension_duration_seconds": {
          "enum": [
            6,
            10
          ],
          "type": "integer"
        },
        "start_seconds": {
          "type": "integer"
        }
      }
    }
  },
  "image-to-video": {
    "models": [
      "grok-imagine-image-to-video"
    ],
    "fields_by_model": {
      "grok-imagine-image-to-video": {
        "aspect_ratio": {
          "enum": [
            "2:3",
            "3:2",
            "1:1",
            "16:9",
            "9:16"
          ]
        },
        "duration_seconds": {
          "type": "integer"
        },
        "index": {
          "type": "integer"
        },
        "motion_style": {
          "enum": [
            "fun",
            "normal",
            "spicy"
          ]
        },
        "output_resolution": {
          "enum": [
            "480p",
            "720p"
          ]
        }
      }
    }
  },
  "text-to-image": {
    "models": [
      "grok-imagine-text-to-image"
    ],
    "fields_by_model": {
      "grok-imagine-text-to-image": {
        "aspect_ratio": {
          "enum": [
            "2:3",
            "3:2",
            "1:1",
            "16:9",
            "9:16"
          ]
        }
      }
    }
  },
  "text-to-video": {
    "models": [
      "grok-imagine-text-to-video"
    ],
    "fields_by_model": {
      "grok-imagine-text-to-video": {
        "aspect_ratio": {
          "enum": [
            "2:3",
            "3:2",
            "1:1",
            "16:9",
            "9:16"
          ]
        },
        "duration_seconds": {
          "type": "integer"
        },
        "motion_style": {
          "enum": [
            "fun",
            "normal",
            "spicy"
          ]
        },
        "output_resolution": {
          "enum": [
            "480p",
            "720p"
          ]
        }
      }
    }
  },
  "upscale-image": {
    "models": [],
    "fields_by_model": {
      "_": {}
    }
  }
} as const;
