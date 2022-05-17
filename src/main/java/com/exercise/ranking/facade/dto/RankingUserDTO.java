package com.exercise.ranking.facade.dto;

import com.exercise.ranking.model.RankingUser;

/**
 * Data transfer object for {@link RankingUser}. The {@code latestExerciseTime} is omitted to return
 * clear answer to the clients.
 */
public class RankingUserDTO {

  /**
   * User who did the exercises.
   */
  private Long userId;

  /**
   * Points from exercises that user did.
   */
  private Double points;

  public static RankingUserDTO of(RankingUser rankingUser) {
    RankingUserDTO dto = new RankingUserDTO();
    dto.userId = rankingUser.getUserId();
    dto.points = rankingUser.getPoints();
    return dto;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(final Long userId) {
    this.userId = userId;
  }

  public Double getPoints() {
    return points;
  }

  public void setPoints(final Double points) {
    this.points = points;
  }
}

