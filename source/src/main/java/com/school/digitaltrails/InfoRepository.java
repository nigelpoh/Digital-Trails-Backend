package com.school.digitaltrails;

import org.springframework.data.mongodb.repository.MongoRepository;
import types.attractions.Info;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;

@Repository
public interface InfoRepository extends MongoRepository<Info, String> {
  Optional<Info> findByVid(String vid);
  
  @Query("{'suitable_comp.time': { $in: [?0] }, 'suitable_comp.weather': { $in: [?1] }}")
  Optional<List<Info>> findBySuitableComp(String time, String weather);


  @Aggregation(pipeline = { 
    "{ $sample: { size: ?0 } }" 
  })
  List<Info> findRandomAttractions(int count);
}