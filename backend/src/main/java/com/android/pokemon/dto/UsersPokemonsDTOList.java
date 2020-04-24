package com.android.pokemon.dto;

import com.android.pokemon.model.Pokemon;
import com.android.pokemon.model.UsersPokemons;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UsersPokemonsDTOList {

    List<UsersPokemonsDTO> usersPokemonsDTOList= new ArrayList<>();

    public UsersPokemonsDTOList(List<UsersPokemons> usersPokemons) {
        for(UsersPokemons up : usersPokemons){
            UsersPokemonsDTO usersPokemonsDTO = new UsersPokemonsDTO(up);
            usersPokemonsDTOList.add(usersPokemonsDTO);
    }
}



}
