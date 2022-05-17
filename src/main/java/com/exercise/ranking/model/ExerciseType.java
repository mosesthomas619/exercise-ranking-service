package com.exercise.ranking.model;

/**
 * Enumeration that holds possible types of exercises and the coefficient that is used for user
 * ranking calculation.
 */
public enum ExerciseType {
  RUNNING(1.04),
  SWIMMING(1.12),
  STRENGTH_TRAINING(1.23),
  CIRCUIT_TRAINING(1.34);

  private final double durationToCaloriesRate;

  ExerciseType(final double durationToCaloriesRate) {
    this.durationToCaloriesRate = durationToCaloriesRate;
  }

  public double getDurationToCaloriesRate() {
    return durationToCaloriesRate;
  }
}

