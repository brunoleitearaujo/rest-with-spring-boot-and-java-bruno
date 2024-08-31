package br.com.bruno.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.bruno.controllers.PersonController;
import br.com.bruno.data.vo.v1.PersonVO;
import br.com.bruno.exceptions.RequiredObjectsIsNullException;
import br.com.bruno.exceptions.ResourceNotFoundException;
import br.com.bruno.mapper.DozerMapper;
import br.com.bruno.model.Person;
import br.com.bruno.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	public List<PersonVO> findAll() {
		logger.info("Finding all Persons!");

		var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		persons.stream()
			.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

		return persons;
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one Person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

		return vo;
	}

	public PersonVO create(PersonVO personVO) {
		if (personVO == null) throw new RequiredObjectsIsNullException();

		logger.info("Creating one Person!");

		var entity = DozerMapper.parseObject(personVO, Person.class);

		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

		return vo;
	}

	public PersonVO update(PersonVO person) {
		if (person == null) throw new RequiredObjectsIsNullException();

		logger.info("Updating one Person!");

		var entity = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting one Person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		repository.delete(entity);
	}
}
