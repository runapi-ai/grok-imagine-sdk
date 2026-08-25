export const contract = {
  "edit-image": {
    "models": [
      "grok-imagine-edit-image",
      "grok-imagine-image-2-0"
    ],
    "fields_by_model": {
      "grok-imagine-edit-image": {
        "model": {
          "required": true
        },
        "source_image_url": {
          "required": true
        }
      },
      "grok-imagine-image-2-0": {
        "aspect_ratio": {
          "enum": [
            "1:1",
            "2:3",
            "3:2",
            "16:9",
            "9:16",
            "auto"
          ],
          "required": true
        },
        "model": {
          "required": true
        },
        "prompt": {
          "max": 390000,
          "length": true
        },
        "source_image_urls": {
          "required": true,
          "min_items": 1,
          "max_items": 5
        }
      }
    },
    "rules": [
      {
        "when": {
          "model": "grok-imagine-edit-image"
        },
        "forbidden": [
          "source_task_id",
          "mask_indices",
          "source_image_urls",
          "aspect_ratio"
        ]
      },
      {
        "when": {
          "model": "grok-imagine-image-2-0"
        },
        "forbidden": [
          "source_image_url",
          "source_task_id",
          "mask_indices",
          "enable_safety_checker"
        ]
      }
    ]
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
          "required": true,
          "type": "integer"
        },
        "prompt": {
          "required": true,
          "max": 5000,
          "length": true
        },
        "source_task_id": {
          "required": true
        },
        "start_seconds": {
          "required": true,
          "min": 0,
          "type": "integer"
        }
      }
    }
  },
  "image-to-video": {
    "models": [
      "grok-imagine-image-to-video",
      "grok-imagine-video-1.5-fast",
      "grok-imagine-video-1.5-preview"
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
          "min": 6,
          "max": 30,
          "type": "integer"
        },
        "index": {
          "min": 0,
          "max": 5,
          "type": "integer"
        },
        "model": {
          "required": true
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
        },
        "prompt": {
          "max": 5000,
          "length": true
        }
      },
      "grok-imagine-video-1.5-fast": {
        "aspect_ratio": {
          "enum": [
            "1:1",
            "16:9",
            "9:16",
            "3:2",
            "2:3"
          ]
        },
        "duration_seconds": {
          "min": 1,
          "max": 30,
          "type": "integer"
        },
        "index": {
          "type": "integer"
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "480p",
            "720p"
          ]
        },
        "prompt": {
          "max": 5000,
          "length": true
        },
        "source_image_url": {
          "required": true
        }
      },
      "grok-imagine-video-1.5-preview": {
        "aspect_ratio": {
          "enum": [
            "1:1",
            "16:9",
            "9:16",
            "3:2",
            "2:3",
            "auto"
          ]
        },
        "duration_seconds": {
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "index": {
          "type": "integer"
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "480p",
            "720p",
            "1080p"
          ]
        },
        "prompt": {
          "min": 1,
          "max": 4096,
          "length": true
        },
        "reference_image_urls": {
          "max_items": 6
        },
        "source_image_url": {
          "required": true
        }
      }
    },
    "rules": [
      {
        "when": {
          "model": "grok-imagine-image-to-video"
        },
        "forbidden": [
          "reference_image_urls"
        ]
      },
      {
        "when": {
          "model": "grok-imagine-video-1.5-fast"
        },
        "forbidden": [
          "source_task_id",
          "index",
          "motion_style",
          "enable_safety_checker"
        ]
      },
      {
        "when": {
          "model": "grok-imagine-video-1.5-preview"
        },
        "forbidden": [
          "source_task_id",
          "index",
          "motion_style",
          "enable_safety_checker"
        ]
      }
    ]
  },
  "segment-map": {
    "models": [
      "grok-imagine-image-2-0"
    ],
    "fields_by_model": {
      "grok-imagine-image-2-0": {
        "model": {
          "required": true
        }
      }
    },
    "rules": [
      {
        "required_any": [
          "image_url",
          "source_task_id"
        ]
      },
      {
        "when": {
          "image_url": {
            "present": true
          }
        },
        "forbidden": [
          "source_task_id"
        ]
      },
      {
        "when": {
          "source_task_id": {
            "present": true
          }
        },
        "forbidden": [
          "image_url"
        ]
      }
    ]
  },
  "text-to-image": {
    "models": [
      "grok-imagine-image-2-0",
      "grok-imagine-text-to-image"
    ],
    "fields_by_model": {
      "grok-imagine-image-2-0": {
        "aspect_ratio": {
          "enum": [
            "1:1",
            "2:3",
            "3:2",
            "16:9",
            "9:16"
          ],
          "required": true
        },
        "model": {
          "required": true
        },
        "prompt": {
          "required": true
        }
      },
      "grok-imagine-text-to-image": {
        "aspect_ratio": {
          "enum": [
            "2:3",
            "3:2",
            "1:1",
            "16:9",
            "9:16"
          ]
        },
        "model": {
          "required": true
        },
        "prompt": {
          "required": true,
          "max": 5000,
          "length": true
        }
      }
    },
    "rules": [
      {
        "when": {
          "model": "grok-imagine-image-2-0"
        },
        "forbidden": [
          "enable_safety_checker",
          "enable_pro"
        ]
      }
    ]
  },
  "text-to-video": {
    "models": [
      "grok-imagine-text-to-video",
      "grok-imagine-video-1.5-fast",
      "grok-imagine-video-1.5-preview"
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
          "min": 6,
          "max": 30,
          "type": "integer"
        },
        "model": {
          "required": true
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
        },
        "prompt": {
          "required": true,
          "max": 5000,
          "length": true
        }
      },
      "grok-imagine-video-1.5-fast": {
        "aspect_ratio": {
          "enum": [
            "1:1",
            "16:9",
            "9:16",
            "3:2",
            "2:3"
          ]
        },
        "duration_seconds": {
          "min": 1,
          "max": 30,
          "type": "integer"
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "480p",
            "720p"
          ]
        },
        "prompt": {
          "required": true,
          "max": 5000,
          "length": true
        }
      },
      "grok-imagine-video-1.5-preview": {
        "aspect_ratio": {
          "enum": [
            "1:1",
            "16:9",
            "9:16",
            "3:2",
            "2:3",
            "auto"
          ]
        },
        "duration_seconds": {
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "480p",
            "720p",
            "1080p"
          ]
        },
        "prompt": {
          "required": true,
          "min": 1,
          "max": 4096,
          "length": true
        },
        "reference_image_urls": {
          "max_items": 7
        }
      }
    },
    "rules": [
      {
        "when": {
          "model": "grok-imagine-text-to-video"
        },
        "forbidden": [
          "reference_image_urls"
        ]
      },
      {
        "when": {
          "model": "grok-imagine-video-1.5-fast"
        },
        "forbidden": [
          "motion_style",
          "enable_safety_checker"
        ]
      },
      {
        "when": {
          "model": "grok-imagine-video-1.5-preview"
        },
        "forbidden": [
          "motion_style",
          "enable_safety_checker"
        ]
      }
    ]
  },
  "upscale-image": {
    "models": [],
    "fields_by_model": {
      "_": {
        "source_task_id": {
          "required": true
        }
      }
    }
  }
} as const;
