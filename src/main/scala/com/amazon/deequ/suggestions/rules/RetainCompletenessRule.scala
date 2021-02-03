/**
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. A copy of the License
 * is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package com.amazon.deequ.suggestions.rules

import com.amazon.deequ.constraints.Constraint.completenessConstraint
import com.amazon.deequ.profiles._
import com.amazon.deequ.suggestions.ConstraintSuggestion
import scala.math.BigDecimal.RoundingMode

/**
  * If a column is incomplete in the sample, we model its completeness as a binomial variable,
  * estimate a confidence interval and use this to define a lower bound for the completeness
  */
case class RetainCompletenessRule(
  minimumCompleteness: Double = 0.2,
  maximumCompleteness: Double = 1.0,
  z: Double = 1.96
) extends ConstraintRule[ColumnProfile] {

  override def shouldBeApplied(profile: ColumnProfile, numRecords: Long): Boolean = {
    profile.completeness > minimumCompleteness && profile.completeness < maximumCompleteness
  }

  override def candidate(profile: ColumnProfile, numRecords: Long): ConstraintSuggestion = {
    val p = profile.completeness
    val n = numRecords

    // TODO this needs to be more robust for p's close to 0 or 1
    val targetCompleteness = BigDecimal(p - z * math.sqrt(p * (1 - p) / n))
      .setScale(2, RoundingMode.DOWN).toDouble

    val boundInPercent = ((1.0 - targetCompleteness) * 100).toInt
    val description = s"'${profile.column}' has less than $boundInPercent% missing values"
    val constraint = completenessConstraint(profile.column, _ >= targetCompleteness,
      hint = Some(description))
    val hintCode = ConstraintRule.genHintCode(description)

    ConstraintSuggestion(
      constraint,
      profile.column,
      "Completeness: " + profile.completeness.toString,
      description,
      this,
      s""".hasCompleteness("${profile.column}", _ >= $targetCompleteness,
         | hint = ${hintCode})"""
        .stripMargin.replaceAll("\n", "")
    )
  }

  override val ruleDescription: String = "If a column is incomplete in the sample, " +
    "we model its completeness as a binomial variable, estimate a confidence interval " +
    "and use this to define a lower bound for the completeness"
}
