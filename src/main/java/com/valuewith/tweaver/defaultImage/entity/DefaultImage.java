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
@Table(name = "DEFAULT_IMAGE")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long defaultImageId;

  @NotNull
  private String imageName;

  @NotNull
  private String defaultImageUrl;

  @CreatedDate
  private LocalDateTime createdDateTime;
}
