package com.icesi.hospital.services;

import java.util.Date;
import java.util.List;

import com.icesi.hospital.model.UrgencyAttention;

public interface IUrgencyService {

public UrgencyAttention getAttention(String consecutive);
	
	public UrgencyAttention addAttention(UrgencyAttention attention);
	
	public UrgencyAttention removeAttention(String consecutive);
	
	public UrgencyAttention updateAttention(UrgencyAttention attention);

	public List<UrgencyAttention> findByDate(Date date);

	public boolean existAttention(String consecutive);

	public void removeSupply(String urgencyConsecutive, String supplyConsecutive);
}
