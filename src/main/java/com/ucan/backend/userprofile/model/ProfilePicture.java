package com.ucan.backend.userprofile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "profile_pictures")
@Data
public class ProfilePicture {

  @Id private String userId;

  private String pictureUrl;

  private String fileName;
}
