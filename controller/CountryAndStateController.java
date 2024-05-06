package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.constant.MessageConstant;
import ng.optisoft.rosabon.dao.CountryDao;
import ng.optisoft.rosabon.dao.LgaDao;
import ng.optisoft.rosabon.dao.StateDao;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.mapper.CountryMapper;
import ng.optisoft.rosabon.mapper.LgaMapper;
import ng.optisoft.rosabon.mapper.StateMapper;
import ng.optisoft.rosabon.model.Country;
import ng.optisoft.rosabon.model.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
public class CountryAndStateController {

	private CountryDao countryDao;

	private StateDao stateDao;

	private LgaDao lgaDao;

	@GetMapping(ApiRoute.COUNTRY)
	public ResponseEntity<?> getCountries() {
		return ResponseEntity.ok(CountryMapper.mapToDto(countryDao.findAll()));
	}

	@GetMapping(ApiRoute.STATE + "/{countryId}")
	public ResponseEntity<?> getStatesByCountry(@PathVariable Long countryId) {
		Country country = countryDao.findById(countryId).orElseThrow(
				() -> new NotFoundException(MessageConstant.COUNTRY_NOT_FOUND));

		return ResponseEntity
				.ok(StateMapper.mapToDto(stateDao.findAllByCountry(country)));
	}

	@GetMapping(ApiRoute.LGA + "/{stateId}")
	public ResponseEntity<?> getLgaByState(@PathVariable Long stateId) {
		State state = stateDao.findById(stateId).orElseThrow(
				() -> new NotFoundException(MessageConstant.STATE_NOT_FOUND));

		return ResponseEntity.ok(LgaMapper.mapToDto(lgaDao.findAllByState(state)));
	}

}
