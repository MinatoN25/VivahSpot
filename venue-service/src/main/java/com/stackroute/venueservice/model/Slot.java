package com.stackroute.venueservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
	
	@Transient
    public static final String SEQUENCE_NAME = "slot_sequence";
	
	@Id
	private int slotId;
	private String slotTime;
	private Status slotStatus;
	public Slot(int slotId, String slotTime) {
		super();
		this.slotId = slotId;
		this.slotTime = slotTime;
	}

	
}