package com.url.url_shortner.service;

import com.url.url_shortner.dtos.ClickEventDTO;
import com.url.url_shortner.dtos.UrlMappingDTO;
import com.url.url_shortner.models.ClickEvent;
import com.url.url_shortner.models.UrlMapping;
import com.url.url_shortner.models.User;
import com.url.url_shortner.repository.ClickEventRepository;
import com.url.url_shortner.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {


    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;
    public UrlMappingDTO createShortUrl(String originalUrl, User user) {

//        actual logic of shorturl

        String shortUrl=generateShortUrl();


        UrlMapping urlMapping=new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMapping.setShortUrl(shortUrl);

//        save this urlmappung to repo
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        return convertToDto(savedUrlMapping);
    }



    private UrlMappingDTO convertToDto(UrlMapping urlMapping){

        UrlMappingDTO urlMappingDTO=new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());

        return urlMappingDTO;
    }

    private String generateShortUrl() {


        String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random=new Random();
        StringBuilder shortUrl=new StringBuilder(8);

        for (int i = 0; i <8 ; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));


        }
        return shortUrl.toString();


    }

    public List<UrlMappingDTO> getUrlsByUser(User user) {

        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {

//        logic
        //first thing we need urlmapping as  to need click events we need urkmappingid

        UrlMapping urlMapping=urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping!=null){

//            we have to find the url mapping and the click date between these staes from clickieventrepository
            //we are transforming the output from clickeventrepo into the one we need in the form of clickevetDTO

return clickEventRepository.findByUrlMappingAndClickDateBetween(
        urlMapping,start,end).stream()
        .collect(Collectors.groupingBy(click->click.getClickDate().toLocalDate(),Collectors.counting()))
        .entrySet().stream()
        .map(entry->{
            ClickEventDTO clickEventDTO=new ClickEventDTO();
            clickEventDTO.setClickDate(entry.getKey());
            clickEventDTO.setCount(entry.getValue());
            return clickEventDTO;
        }).collect(Collectors.toList());


        }

return null;
    }




    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {

        List<UrlMapping> urlMappings=urlMappingRepository.findByUser(user);

        List<ClickEvent> clickEvents=clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,start.atStartOfDay(),end.plusDays(1).atStartOfDay());

//        we group by data and count it then in the form of desired output
        return clickEvents.stream()
                .collect(Collectors.groupingBy(click->click.getClickDate().toLocalDate(),Collectors.counting()));

    }

    public UrlMapping getOriginalUrl(String shortUrl) {

        UrlMapping urlMapping=urlMappingRepository.findByShortUrl(shortUrl);


//        RECORDING THE ANALYTICS HERE
        if(urlMapping!=null){

//            updating the count in url level
            urlMapping.setClickCount(urlMapping.getClickCount()+1);
            urlMappingRepository.save(urlMapping);



//                    record click event object
            ClickEvent clickEvent=new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);

        }

        return urlMapping;
    }
}
