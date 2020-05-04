package com.android.pokemon.dto;

import com.android.pokemon.model.GeoPoint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GenerateGeoDataDTO {
	private int numberOfData; 
	private GeoPoint geopoint; //start point
	private double radius; //in metres
}
