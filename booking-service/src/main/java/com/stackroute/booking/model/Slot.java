package com.stackroute.booking.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
	
	@Id
	private int slotId;
	private Status slotStatus;
	private String slotTime;
	
	
}

