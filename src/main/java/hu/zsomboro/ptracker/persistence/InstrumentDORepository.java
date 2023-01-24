package hu.zsomboro.ptracker.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.ptracker.persistence.entity.InstrumentDO;

public interface InstrumentDORepository extends CrudRepository<InstrumentDO, String> {

  @Override
  Optional<InstrumentDO> findById(String id);

  List<InstrumentDO> findByInstrumentType(String instrumentType);

}
