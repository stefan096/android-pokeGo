package com.android.pokemon.utils;

public class CheckValidity {

	
	public static boolean nullOrEmpty(String value) {
		
		if(value == null) {
			return false;
		}
		
		if(value.equals("")) {
			return false;
		}
		
		return true;
	}
}
