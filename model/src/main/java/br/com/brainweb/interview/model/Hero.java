package br.com.brainweb.interview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hero implements Serializable {

    @With
    @EqualsAndHashCode.Include
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotNull
    private Race race;

    @NotNull
    private PowerStats powerStats;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean enabled = Boolean.TRUE;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;
}
