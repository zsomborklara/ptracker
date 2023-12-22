package hu.zsomboro.ptracker.persistence;

import hu.zsomboro.ptracker.persistence.entity.ConstituentId;
import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;

import java.util.Optional;

public interface ConstituentDORepository extends CrudRepository<ConstituentDO, ConstituentId> {

  Optional<ConstituentDO> findById(ConstituentId id);

}
