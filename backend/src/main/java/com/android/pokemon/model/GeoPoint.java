package com.android.pokemon.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GeoPoint {
	
	private double latitude;
	private double longitude;
	
	public GeoPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
