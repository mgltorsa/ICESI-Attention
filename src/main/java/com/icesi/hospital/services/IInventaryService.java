package com.icesi.hospital.services;

import java.util.List;

import com.icesi.hospital.model.InventaryMedicine;




public interface IInventaryService {
	
	public InventaryMedicine addInventaryMedicine(InventaryMedicine inventary);
	public InventaryMedicine updateInventaryMedicine(InventaryMedicine inventary);
	public List<InventaryMedicine> removeInventaryMedicine(String medicineConsecutive);
	public List<InventaryMedicine> getInventaryMedicine(String medicineConsecutive);
	public List<InventaryMedicine> supplyMedicine(String medicineConsecutive, int quantitySupplied);
	public int getTotalInventaryMedicine(String consecutive);
	public void addQuantityToInventaryMedicine(String medicineConsecutive, int quantity);

}
