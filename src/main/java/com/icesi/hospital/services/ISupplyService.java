package com.icesi.hospital.services;

import java.util.Date;
import java.util.List;

import com.icesi.hospital.model.Supply;

public interface ISupplyService {

	public Supply getSupply(String consecutive);
	
	public Supply addSupply(Supply supply);
	
	public Supply removeSupply(String consecutive);
	
	public Supply updateSupply(Supply supply);

	public List<Supply> findByDate(Date date);

	public boolean existSupply(String consecutive);

}
