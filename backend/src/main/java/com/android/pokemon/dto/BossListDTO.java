package com.android.pokemon.dto;

import java.util.ArrayList;
import java.util.List;

import com.android.pokemon.model.Boss;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BossListDTO {

    List<BossDTO> pokemonBosses = new ArrayList<BossDTO>();

    public BossListDTO(List<Boss> bosses) {
        for(Boss up : bosses){
            BossDTO boss = new BossDTO(up);
            pokemonBosses.add(boss);
        }
    }
}
