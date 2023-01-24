package hu.zsomboro.ptracker.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;

public interface PortfolioDORepository extends CrudRepository<PortfolioDO, Long> {

  PortfolioDO findById(long id);

  List<PortfolioDO> findByName(String name);

}
