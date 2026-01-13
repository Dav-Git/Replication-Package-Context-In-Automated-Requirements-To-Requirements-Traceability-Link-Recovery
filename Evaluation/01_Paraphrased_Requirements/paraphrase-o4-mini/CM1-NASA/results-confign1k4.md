## LissaConfiguration (2025-11-11_14-01+0100 -- config.json_bf894567-819b-32ab-bc2f-26bb554c9d6f)
```json
{
  "cache_dir" : "./cache-r2r/CM1-NASA-ensemble",
  "gold_standard_configuration" : {
    "path" : "./datasets/CM1-NASA/answer.csv",
    "hasHeader" : true,
    "swap_columns" : false,
    "name" : "gold_standard"
  },
  "context_configurations" : [ ],
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/CM1-NASA/high"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/CM1-NASA/low"
    }
  },
  "source_preprocessor" : {
    "name" : "artifact",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "paraphrase_openai",
    "args" : {
      "model" : "o4-mini-2025-04-16",
      "superseed" : "1337420",
      "temperature" : "1",
      "count" : "1",
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
    "name" : "identity",
    "args" : { }
  },
  "name" : "LiSSA"
}
```

## Stats
* #TraceLinks (GS): 45
* #Source Artifacts: 22
* #Target Artifacts: 53
## Results
* True Positives: 28
* False Positives: 31
* False Negatives: 17
* Precision: 0.4745762711864407
* Recall: 0.6222222222222222
* F1: 0.5384615384615384
