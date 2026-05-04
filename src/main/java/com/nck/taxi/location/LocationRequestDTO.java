package com.nck.taxi.location;
 

import lombok.Data;

@Data
public class LocationRequestDTO {
//
//    private String driverId;
//    private double lat;
//    private double lng;
	
	
	
	 private String driverId;
	    private String name;
	    private String plate;
	    private String model;
	    private String status;   // "Disponible" | "En course" | "Hors ligne"
	    private double lat;
	    private double lng;
	    private double speed;
	    private double rating;
	    private int trips;
	    private String phone;
}