package hu.zsomboro.ptracker.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import hu.zsomboro.ptracker.persistence.entity.PriceId;

public interface PriceDORepository extends CrudRepository<PriceDO, PriceId> {

  List<PriceDO> findByAsOfDate(LocalDate asOfDate);

  List<PriceDO> findByIdentifier(String identifier);

}
