package com.android.pokemon.model;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UsersPokemons implements Comparable<UsersPokemons> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;
    private int level;
    private double experience;
    private Instant cooldown;


    @Column(name = "`createdAt`")
	@CreationTimestamp
	private Date createdAt;
    
//    @ManyToOne()
//    private Fight fight;
    private double fightHealt;

    @Override
    public int compareTo(UsersPokemons o) {
		// TODO Auto-generated method stub
		return o.getLevel() - this.level;
	}

    public Instant getCooldown() {
        return cooldown;
    }

    public void setCooldown(Instant cooldown) {
        this.cooldown = cooldown;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}
