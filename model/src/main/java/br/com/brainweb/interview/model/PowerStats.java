package br.com.brainweb.interview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PowerStats {

    @With
    @EqualsAndHashCode.Include
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected UUID id;

    @NotNull
    @Max(32767)
    private Integer strength;

    @NotNull
    @Max(32767)
    private Integer agility;

    @NotNull
    @Max(32767)
    private Integer dexterity;

    @NotNull
    @Max(32767)
    private Integer intelligence;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
