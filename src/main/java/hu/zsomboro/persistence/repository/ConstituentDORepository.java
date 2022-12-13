package hu.zsomboro.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.persistence.entity.ConstituentDO;

public interface ConstituentDORepository extends CrudRepository<ConstituentDO, Long> {

  ConstituentDO findById(long id);

}
