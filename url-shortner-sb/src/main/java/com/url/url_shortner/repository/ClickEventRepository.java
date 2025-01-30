package com.url.url_shortner.repository;

import com.url.url_shortner.models.ClickEvent;
import com.url.url_shortner.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent,Long> {

//    jpa filter all those daata by urlmapping and also between those dates
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping mapping, LocalDateTime startDate,LocalDateTime endDate);

//    this we want to show for the totalClicks that we got ..clickeventrepo will helps us get the all click events happens fpr that particular user as we have passed urlMapping for that particular user ..summary is use to help to generate totalCLicks
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappings,LocalDateTime startDate,LocalDateTime endDate);


}
