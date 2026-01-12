## Configuration (2025-11-05_15-27+0100 -- config.json_4f6b39cd-57c8-33d9-9c92-f2bafd27ff21)
```json
{
  "cache_dir" : "./cache-r2r/WARC-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/WARC/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/WARC/high"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/WARC/low"
    }
  },
  "source_preprocessor" : {
    "name" : "artifact",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "paraphrase_openai",
    "args" : {
      "model" : "gpt-4o-2024-08-06",
      "superseed" : "1337420",
      "temperature" : "1",
      "count" : "5",
      "template" : "Here is the text of a software {artifact_type}:\n\n{content}\n\nParaphrase the requirement, retaining its original meaning.\nAnswer with the paraphrased text only, without any additional explanation or formatting.\n"
    }
  },
  "embedding_creator" : {
    "name" : "openai",
    "args" : {
      "model" : "text-embedding-3-large"
    }
  },
  "source_store" : {
    "name" : "custom",
    "args" : { }
  },
  "target_store" : {
    "name" : "paraphrased_to_artifacts",
    "args" : {
      "max_results" : "4"
    }
  },
  "classifier" : {
    "name" : "reasoning_openai",
    "args" : {
      "model" : "gpt-4o-2024-08-06",
      "seed" : "133742243",
      "temperature" : "0.0",
      "prompt" : "Below are two artifacts from the same software system. Is there a traceability link between (1) and (2)? Give your reasoning and then answer with 'yes' or 'no' enclosed in <trace> </trace>.\n (1) {source_type}: '''{source_content}''' \n (2) {target_type}: '''{target_content}''' ",
      "use_original_artifacts" : "false",
      "use_system_message" : "true"
    }
  },
  "result_aggregator" : {
    "name" : "any_connection",
    "args" : {
      "source_granularity" : "0",
      "target_granularity" : "0"
    }
  },
  "tracelinkid_postprocessor" : {
    "name" : "req2req",
    "args" : { }
  }
}
```

## Stats
* #TraceLinks (GS): 136
* #Source Artifacts: 63
* #Target Artifacts: 89
## Results
* True Positives: 89
* False Positives: 84
* False Negatives: 47
* Precision: 0.5144508670520231
* Recall: 0.6544117647058824
* F1: 0.5760517799352752
