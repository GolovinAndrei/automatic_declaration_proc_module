package by.btslogistics.autorelease.dao.repository.getregistrationnumber;

import by.btslogistics.autorelease.dao.model.getregistrationnumber.GetRegistrationNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GetRegistrationNumberRepository extends CrudRepository<GetRegistrationNumber, String> {

    @Query("select n from GetRegistrationNumber n where n.logType = :logType and n.isIn = :isIn and n.reviewType = :reviewType and n.year = :year and n.house = :house and n.point = :point")
    GetRegistrationNumber getNumber(@Param("logType") String logType,
                                    @Param("isIn") Long isIn,
                                    @Param("reviewType") String reviewType,
                                    @Param("year") Integer year,
                                    @Param("house") Long house,
                                    @Param("point") Long point);

}
