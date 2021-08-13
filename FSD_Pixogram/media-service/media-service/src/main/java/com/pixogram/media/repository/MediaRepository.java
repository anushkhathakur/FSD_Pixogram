package com.pixogram.media.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixogram.media.entity.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

	Media findByMediaId(Long mediaId);
	Media findByUserId(Long userId);
	List<Media> findMediaByUserId(Long userId);
}
