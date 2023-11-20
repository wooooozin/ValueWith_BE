package com.valuewith.tweaver.defaultImage.repository;

import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultImageRepository extends JpaRepository<DefaultImage, Long> {

  @Query(value = "SELECT * FROM default_image where image_name = :imageName order by RAND() limit 1",nativeQuery = true)
  DefaultImage findRandomByImageName(@Param("imageName") String tripArea);
  boolean existsDefaultImageByImageName(String imageName);
  DefaultImage findByImageName(@Param("imageName") String profileImg);
}
