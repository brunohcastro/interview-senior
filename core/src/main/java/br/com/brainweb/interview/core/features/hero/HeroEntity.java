package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.model.PowerStats;
import br.com.brainweb.interview.model.Race;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("hero")
public class HeroEntity {

    @Id
    @With
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;
    private Race race;
    private UUID powerStatsId;
    private Boolean enabled;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
