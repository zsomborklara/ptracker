package hu.zsomboro.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.persistence.entity.PortfolioDO;

public interface PortfolioDORepository extends CrudRepository<PortfolioDO, Long> {

  PortfolioDO findById(long id);

}
