package com.exercise.ranking.facade;

import com.exercise.ranking.model.RankingUser;
import com.exercise.ranking.service.ExerciseService;
import com.exercise.ranking.facade.dto.RankingUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ranking", description = "The Ranking API.")
@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/ranking")
public class RankingApiController {

  private final ExerciseService exerciseService;

  public RankingApiController(final ExerciseService exerciseService) {
    this.exerciseService = exerciseService;
  }

  @Operation(summary = "Get ranking for a set of users.", description = "Calculate the ranking for the given user ids. The calculation is based on the exercises the user has done in the last 28 days. The list is sorted in descending order by user points. ")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
  @GetMapping(produces = {"application/json"})
  public ResponseEntity<List<RankingUserDTO>> ranking(
      @Parameter(description = "List of user ids to rank", required = true) @Valid @RequestParam(value = "userIds") final Set<Long> userIds) {
    List<RankingUser> userRanking = exerciseService.ranking(userIds);
    return ResponseEntity.ok(
        userRanking.stream().map(RankingUserDTO::of).collect(Collectors.toList()));
  }
}
