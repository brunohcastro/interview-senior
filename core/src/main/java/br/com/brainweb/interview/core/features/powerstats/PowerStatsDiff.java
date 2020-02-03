package br.com.brainweb.interview.core.features.powerstats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerStatsDiff {

    @With
    private UUID referenceId;
    @With
    private UUID otherId;

    private Integer strength;
    private Integer agility;
    private Integer dexterity;
    private Integer intelligence;
}
