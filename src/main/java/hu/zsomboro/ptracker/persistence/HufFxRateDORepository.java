package hu.zsomboro.ptracker.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hu.zsomboro.ptracker.persistence.entity.FxRateId;
import hu.zsomboro.ptracker.persistence.entity.HufFxRateDO;

public interface HufFxRateDORepository extends CrudRepository<HufFxRateDO, FxRateId> {

  List<HufFxRateDO> findByAsOfDate(LocalDate asOfDate);

}
