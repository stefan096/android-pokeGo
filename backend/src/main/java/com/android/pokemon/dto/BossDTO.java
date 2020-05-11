package com.android.pokemon.dto;

import com.android.pokemon.model.Boss;
import com.android.pokemon.model.Pokemon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BossDTO {
    private Long id;
    private Pokemon pokemon;
    private int level;
    private double latitude;
    private double longitude;
    
    private double fightHealt;
    
    public BossDTO(Boss boss) {
        this.id = boss.getId();
        this.pokemon = boss.getPokemon();
        this.level = boss.getLevel();
        this.latitude = boss.getLatitude();
        this.longitude = boss.getLongitude();
        this.fightHealt = boss.getFightHealt();
    }

	@Override
	public String toString() {
		return "BossDTO [id=" + id + ", pokemon=" + pokemon + ", level=" + level + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", fightHealt=" + fightHealt + "]";
	}
}