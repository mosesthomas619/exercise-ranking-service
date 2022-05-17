package com.exercise.ranking.facade;

import com.exercise.ranking.service.ExerciseService;
import com.exercise.ranking.facade.dto.ExerciseDTO;
import com.exercise.ranking.model.Exercise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Exercises", description = "The Exercises API.")
@RestController
@RequestMapping("/exercise")
@Secured("ROLE_ADMIN")
public class ExerciseApiController {

  private final ExerciseService exerciseService;

  public ExerciseApiController(final ExerciseService exerciseService) {
    this.exerciseService = exerciseService;
  }

  @Operation(summary = "Insert a new exercise for a user.", description = "Persist a new exercise and generate its id.")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Exercise created")})
  @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
  public ResponseEntity<ExerciseDTO> insert(@Valid @RequestBody final ExerciseDTO dto) {
    Exercise exercise = ExerciseDTO.toExercise(dto);
    Exercise insertedExercise = exerciseService.insert(exercise);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ExerciseDTO.fromExercise(insertedExercise));
  }

  @Operation(summary = "Update an existing exercise for a user.", description = "Update an already persisted exercise. Exercise id, user id and exercise type are excluded from the update.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exercise updated")})
  @PutMapping(value = "/{exerciseId}", produces = {"application/json"}, consumes = {"application/json"})
  public ResponseEntity<ExerciseDTO> update(
      @Parameter(description = "Id of the exercise to update", required = true) @PathVariable("exerciseId") final Long exerciseId,
      @Parameter(description = "", required = true) @Valid @RequestBody final ExerciseDTO dto) {
    Exercise exercise = ExerciseDTO.toExercise(dto);
    Exercise updateExercise = exerciseService.update(exerciseId, exercise);
    return ResponseEntity.ok(ExerciseDTO.fromExercise(updateExercise));
  }

}
