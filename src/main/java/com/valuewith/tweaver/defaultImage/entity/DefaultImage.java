package com.valuewith.tweaver.defaultImage.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "TBL_DEFAULT_IMAGE")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultImage {
  /**
   * Default Image PK(고유 번호)
   **/
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long defaultImageId;

  /**
   * Image Name(이미지 명)
   **/
  @NotNull
  private String ImageName;

  /**
   * Default Image Url(기본 이미지 URL)
   **/
  @NotNull
  private String DefaultImageUrl;

  /**
   * Created date time(생성 날짜)
   **/
  @CreatedDate
  private LocalDateTime createdDateTime;

}
