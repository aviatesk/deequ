# Changelog

All notable changes to this fork (`aviatesk/deequ`) will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

- this fork is separated from <https://github.com/awslabs/deequ/tree/fc3b30715570e7e3f558fb7bcc3abe4cff83914d>, as such all the changes below are diffs from the commit
- add a constructor option to give custom category sorter for `CategoricalRangeRule` and `FractionalCategoricalRangeRule` ([#1](https://github.com/aviatesk/deequ/pull/1))
- relax suggested numeric types ([#2](https://github.com/aviatesk/deequ/pull/2))
  - when a column is not pure `Integral`/`Fractional`, now RetainTypeRule will suggest `.hasDataType(column, Numeric)` constraint, which accepts both type of numeric values
  - so that suggested checks don't fail on the original data from which they're generated
- deequ is now built with spark-3.0.1 and scala-2.12.10 by default ([#4](https://github.com/aviatesk/deequ/pull/4))
- add hints to suggested constraint code, which should greatly improve its interpretability ([#5](https://github.com/aviatesk/deequ/pull/5))
- parameterize `RetainCompletenessRule` so that users can specify completeness threshold and z-value at will ([#6](https://github.com/aviatesk/deequ/pull/6))
