package com.icesi.hospital.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.icesi.hospital.repositories.*;
import com.icesi.hospital.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class InventaryService implements IInventaryService {

	@Autowired
	private IInventaryRepository inventaryRepository;

	@Autowired
	private IMedicineRepository medicineRepository;

	@Override
	public InventaryMedicine addInventaryMedicine(InventaryMedicine inventary) {
		if (inventary == null) {
			throw new NullPointerException("inventary was null");
		}

		Optional<Medicine> opt = medicineRepository.findById(inventary.getMedicine().getConsecutive());

		if (!opt.isPresent()) {
			throw new IllegalStateException("Medicine doesn't exist");
		}

		return inventaryRepository.save(inventary);
	}

	@Override
	public InventaryMedicine updateInventaryMedicine(InventaryMedicine inventary) {
		if (inventary == null) {
			throw new NullPointerException("inventary was null");
		}

		Optional<Medicine> opt = medicineRepository.findById(inventary.getMedicine().getConsecutive());

		if (!opt.isPresent()) {
			throw new IllegalStateException("Medicine doesn't exist");
		}

		return inventaryRepository.save(inventary);

	}

	@Override
	public List<InventaryMedicine> removeInventaryMedicine(String medicineConsecutive) {

		if (medicineConsecutive == null) {
			throw new NullPointerException("consecutive was null");
		}

		
		Optional<Medicine> opt = medicineRepository.findById(medicineConsecutive);

		if (!opt.isPresent()) {
			throw new IllegalStateException("Medicine doesn't exist");
		}

		List<InventaryMedicine> inventaries = inventaryRepository.findByMedicine(opt.get());
		inventaryRepository.deleteAll(inventaries);
		return inventaries;

	}

	@Override
	public List<InventaryMedicine> getInventaryMedicine(String medicineConsecutive) {
		if (medicineConsecutive == null) {
			throw new IllegalArgumentException("consecutive was null");
		}


		Optional<Medicine> opt = medicineRepository.findById(medicineConsecutive);

		if (!opt.isPresent()) {
			throw new IllegalStateException("Medicine doesn't exist");
		}

		List<InventaryMedicine> inventaries = inventaryRepository.findByMedicine(opt.get());

		if (inventaries == null || inventaries.size() == 0) {
			throw new IllegalStateException("inventary doesn't exist");
		}
		return inventaries;
	}

	@Override
	public List<InventaryMedicine> supplyMedicine(String medicineConsecutive, int quantitySupplied) {
		if (medicineConsecutive == null) {
			throw new NullPointerException("consecutive was null");
		}

		if (quantitySupplied <= 0) {
			throw new IllegalArgumentException("quantity must be greater than 0");
		}

		List<InventaryMedicine> inventaries = getInventaryMedicine(medicineConsecutive);

		int totalOnInventary = 0;

		List<InventaryMedicine> updatedInventary = new ArrayList<InventaryMedicine>();

		for (InventaryMedicine inventary : inventaries) {
			int availableQuantity = inventary.getAvailableQuantity();
			if (availableQuantity > 0) {
				totalOnInventary += availableQuantity;
				updatedInventary.add(inventary);
			}

			if (totalOnInventary >= quantitySupplied) {
				break;
			}
		}

		if(totalOnInventary==0){
			throw new IllegalStateException("Error, doesn't exist any quantity on inventary (0)");

		}

		if (totalOnInventary < quantitySupplied) {
			throw new IllegalStateException("Total on inventary ("+ totalOnInventary +") must be greater than quantity supplied");
		}

		int tempSupplied = quantitySupplied;

		// actualiza los inventarios usados
		for (InventaryMedicine inventary : updatedInventary) {

			int oldQuantity = inventary.getAvailableQuantity();

			int newQuantity = oldQuantity - tempSupplied >= 0 ? oldQuantity - tempSupplied : 0;

			tempSupplied -= oldQuantity;

			inventary.setAvailableQuantity(newQuantity);

			updateInventaryMedicine(inventary);

			if (tempSupplied <= 0) {
				break;
			}
		}
		return updatedInventary;
	}

	@Override
	public int getTotalInventaryMedicine(String consecutive) {

		int total = 0;
		List<InventaryMedicine> inventaries=  getInventaryMedicine(consecutive);

		for (InventaryMedicine inventary : inventaries) {
			total+=inventary.getAvailableQuantity();
		}
		
		return total;
	}


	@Override
	public void addQuantityToInventaryMedicine(String medicineConsecutive, int quantity) {
		List<InventaryMedicine> inventaries=  getInventaryMedicine(medicineConsecutive);
		inventaries.get(0).setAvailableQuantity(inventaries.get(0).getAvailableQuantity()+quantity);
	}
}

