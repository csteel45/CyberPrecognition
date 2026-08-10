# CyberPrecognition

A machine-learning experiment (developed **2014**) that classifies network-attack
patterns from packet captures — an exploration of whether supervised models could
"anticipate" cyber events from traffic features.

> **Note on history:** this repository was re-created from a clean, sanitized snapshot,
> so its git history begins in 2026 even though the work itself dates to 2014 (see the
> `2014` copyright/`@author` stamps in the source). The original commit history and the
> large sample packet captures are not included here.

## What it does

- **`pcap`** — ingests packet captures (`PcapIngest`, `HttpBodyPcapReader`) and extracts
  per-packet / per-flow features.
- **`classification`** — trains and applies classifiers (`RandomForestClassifier`,
  `AttackPatternClassifier`, `PcapTrainer`) over those features to label attack patterns.
- **`events`** — models sequences of derived cyber events (`CyberEventSequence`).

## Tech

Java (Maven), with Java-ML and the hadoop-pcap libraries for capture parsing, plus the
classic Iris dataset (`data/iris.data`) as a tiny worked example.

## Status

Historical / archival. The bundled sample packet captures and on-disk cache are excluded
from the repository (see `.gitignore`); only the code and the small Iris sample are kept.
