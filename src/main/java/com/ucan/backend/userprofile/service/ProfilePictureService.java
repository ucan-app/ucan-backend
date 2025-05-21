package com.ucan.backend.userprofile.service;

import com.ucan.backend.userprofile.model.ProfilePicture;
import com.ucan.backend.userprofile.repo.ProfilePictureRepository;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfilePictureService {

  private final S3Service s3Service;
  private final ProfilePictureRepository profilePictureRepository;

  public ProfilePictureService(
      S3Service s3Service, ProfilePictureRepository profilePictureRepository) {
    this.s3Service = s3Service;
    this.profilePictureRepository = profilePictureRepository;
  }

  public String uploadProfilePicture(MultipartFile file, String userId) throws IOException {
    String pictureUrl = s3Service.uploadProfilePicture(file, userId);

    ProfilePicture profilePicture =
        profilePictureRepository.findById(userId).orElse(new ProfilePicture());

    profilePicture.setUserId(userId);
    profilePicture.setPictureUrl(pictureUrl);
    profilePicture.setFileName(file.getOriginalFilename());

    profilePictureRepository.save(profilePicture);

    return pictureUrl;
  }

  public String getProfilePictureUrl(String userId) {
    return profilePictureRepository
        .findById(userId)
        .map(ProfilePicture::getPictureUrl)
        .orElse(null);
  }
}
