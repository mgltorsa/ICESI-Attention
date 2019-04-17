package com.icesi.hospital.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.icesi.hospital.model.Patient;
import com.icesi.hospital.model.Supply;
import com.icesi.hospital.model.UrgencyAttention;
import com.icesi.hospital.repositories.IPatientRepository;
import com.icesi.hospital.repositories.IUrgencyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class UrgencyService implements IUrgencyService {

	@Autowired
	private IUrgencyRepository urgencyRepository;

	@Autowired
	private ISupplyService supplyService;

	@Autowired
	private IPatientRepository patientRepository;

	@Override
	public UrgencyAttention getAttention(String consecutive) {

		if (consecutive == null) {
			throw new NullPointerException("Consecutive was null");
		}

		Optional<UrgencyAttention> opt = urgencyRepository.findById(consecutive);
		if (!opt.isPresent()) {
			throw new IllegalStateException("Urgency with consecutive No. " + consecutive + " doesn't exists");
		}
		return opt.get();
	}

	@Override
	public UrgencyAttention addAttention(UrgencyAttention attention) {

		// verifica si la atencion esta correctamente estructurada
		verifyAttention(attention);

		// Existe la atencion
		if (existAttention(attention)) {
			throw new IllegalStateException("Attention already exists");
		}

		// Paciente existe
		if (!existPatient(attention.getPatient())) {
			throw new IllegalStateException("Patient doesn't exist");
		}

		// Verifica si los clientes en los suministros y la atencion son iguales,
		// si no lanza una excepcion
		verifyEqualsClients(attention);

		// Agrega los suministros y si hay problemas retorna una excepcion
		addSupplies(attention.getSupplies());

		return urgencyRepository.save(attention);
	}

	private boolean existPatient(@NonNull Patient patient) {
		return patientRepository.findById(patient.getDocument()).isPresent();
	}

	private boolean existAttention(UrgencyAttention attention) {
		return urgencyRepository.findById(attention.getConsecutive()).isPresent();
	}

	private void verifyAttention(UrgencyAttention attention) {
		if (attention == null) {
			throw new NullPointerException("attention was null");
		}

		if (attention.getConsecutive() == null || attention.getConsecutive().isEmpty()) {
			throw new IllegalArgumentException("consecutive was null or empty");
		}

		if (attention.getDate() == null) {
			throw new IllegalArgumentException("date was null");
		}

		if (attention.getGeneralDescription() == null || attention.getGeneralDescription().isEmpty()) {
			throw new IllegalArgumentException("general description was null or empty");
		}

		if (attention.getPatient() == null || attention.getPatient().getDocument() == null
				|| attention.getPatient().getDocument().isEmpty()) {
			throw new IllegalArgumentException("Patient was null or document was either null or empty");
		}

		if (attention.getProcedure() == null || attention.getProcedure().isEmpty()) {
			throw new IllegalArgumentException("procedure was null or empty");
		}

		if (attention.getForwarded() == null) {
			throw new IllegalArgumentException("forwarded was null");
		}

	}

	private void verifyEqualsClients(UrgencyAttention attention) {
		// Paciente de suministros no es igual al de urgencia.

		if (attention.getSupplies() != null) {
			for (Supply supply : attention.getSupplies()) {
				if (!supply.getPatient().getDocument().equals(attention.getPatient().getDocument())) {
					throw new IllegalArgumentException(
							"Supply No. " + supply.getConsecutive() + " not match with patient "
									+ attention.getPatient().getDocument() + " in urgency attention");
				}
			}
		}

	}

	private void addSupplies(List<Supply> supplies) {
		// Tiene suministros
		if (supplies != null) {

			// Ocurre un error al agregar los suministros
			// Suministros comprueba la existencia de paciente y medicamento
			try {
				for (Supply supply : supplies) {
					supplyService.addSupply(supply);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("illegal supplies - " + e.getMessage());
			}

		}

	}

	@Override
	public UrgencyAttention removeAttention(String consecutive) {
		UrgencyAttention attention = getAttention(consecutive);
		urgencyRepository.delete(attention);

		for(Supply supply : attention.getSupplies()){
			supplyService.removeSupply(supply.getConsecutive());
		}

		return attention;
	}

	@Override
	public UrgencyAttention updateAttention(UrgencyAttention attention) {
		if (attention == null) {
			throw new NullPointerException("attention was null");
		}

		verifyAttention(attention);

		if (!existAttention(attention)) {
			throw new IllegalStateException("attention doesn't exists");
		}
		if (!existPatient(attention.getPatient())) {
			throw new IllegalStateException("Patient doesn't exists");
		}

		verifyEqualsClients(attention);

		UrgencyAttention oldAttention = getAttention(attention.getConsecutive());

		verifyAttention(oldAttention);

		if (!existAttention(oldAttention)) {
			throw new IllegalStateException("attention doesn't exists");
		}

		if (!existPatient(oldAttention.getPatient())) {
			throw new IllegalStateException("Patient doesn't exists");
		}

		verifyEqualsClients(oldAttention);

		return urgencyRepository.save(attention);
	}

	@Override
	public List<UrgencyAttention> findByDate(Date date) {

		if (date == null) {
			throw new NullPointerException("date was null");
		}
		return urgencyRepository.findByDate(date);

	}

	@Override
	public boolean existAttention(String consecutive) {
		return urgencyRepository.existsById(consecutive);
	}

	@Override
	public void removeSupply(String urgencyConsecutive, String supplyConsecutive) {
		Optional<UrgencyAttention> opt = urgencyRepository.findById(urgencyConsecutive);
		if (!opt.isPresent()) {
			throw new IllegalArgumentException("Doesn't exist urgency with consecutive: " + urgencyConsecutive);
		}

		UrgencyAttention attention = opt.get();

		int supplyIndex = -1;

		List<Supply> supplies = attention.getSupplies();
		for (int i = 0; i < supplies.size(); i++) {
			if (supplies.get(i).getConsecutive().equals(supplyConsecutive)) {
				supplyIndex = i;
				break;
			}
		}

		if (supplyIndex > -1) {
			attention.getSupplies().remove(supplyIndex);
		}

		supplyService.removeSupply(supplyConsecutive);

		updateAttention(attention);

	}

}
