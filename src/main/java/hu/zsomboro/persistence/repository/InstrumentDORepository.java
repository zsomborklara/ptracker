package hu.zsomboro.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.persistence.entity.InstrumentDO;

public interface InstrumentDORepository extends CrudRepository<InstrumentDO, String> {

  @Override
  Optional<InstrumentDO> findById(String id);

  List<InstrumentDO> findByInstrumentType(String instrumentType);

}
