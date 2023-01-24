package hu.zsomboro.ptracker.persistence;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;

public interface ConstituentDORepository extends CrudRepository<ConstituentDO, Long> {

  ConstituentDO findById(long id);

}
