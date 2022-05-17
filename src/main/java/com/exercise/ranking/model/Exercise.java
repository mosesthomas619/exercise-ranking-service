package com.exercise.ranking.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

/**
 * Exercise class represent exercise entity.
 */
@Entity
@Table(
        name = "exercises", indexes = { @Index(name = "userId", columnList = "userId", unique = false) }
)
public class Exercise {

  /**
   * Id of the exercise.
   */
  @Id
  @GeneratedValue
  private Long id;

  /**
   * User who did the exercise.
   */
  private Long userId;

  /**
   * Description of the exercise.
   */
  private String description;

  /**
   * Type of the exercise.
   */
  private ExerciseType type;

  /**
   * Date and time when the user started the exercise.
   */
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime startTime;

  /**
   * Duration of the exercise in seconds.
   */
  private Long duration;

  /**
   * Calories burnt in the exercise.
   */
  private Double calories;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(final Long userId) {
    this.userId = userId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public ExerciseType getType() {
    return type;
  }

  public void setType(final ExerciseType type) {
    this.type = type;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(final LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(final Long duration) {
    this.duration = duration;
  }

  public Double getCalories() {
    return calories;
  }

  public void setCalories(final Double calories) {
    this.calories = calories;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Exercise exercise = (Exercise) o;
    return Objects.equals(id, exercise.id) && userId.equals(exercise.userId) && description.equals(
        exercise.description) && type == exercise.type && startTime.equals(exercise.startTime)
        && duration.equals(exercise.duration) && calories.equals(exercise.calories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, description, type, startTime, duration, calories);
  }

  @Override
  public String toString() {
    return "Exercise{" + "id=" + id + ", userId=" + userId + ", description='" + description + '\''
        + ", type=" + type + ", startTime=" + startTime + ", duration=" + duration + ", calories="
        + calories + '}';
  }
}

