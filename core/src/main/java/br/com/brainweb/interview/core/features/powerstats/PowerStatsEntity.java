package br.com.brainweb.interview.core.features.powerstats;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("power_stats")
public class PowerStatsEntity {

    @Id
    @With
    @EqualsAndHashCode.Include
    protected UUID id;

    private Integer strength;
    private Integer agility;
    private Integer dexterity;
    private Integer intelligence;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
