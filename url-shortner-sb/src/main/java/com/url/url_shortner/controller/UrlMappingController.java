package com.url.url_shortner.controller;


import com.url.url_shortner.dtos.ClickEventDTO;
import com.url.url_shortner.dtos.UrlMappingDTO;
import com.url.url_shortner.models.ClickEvent;
import com.url.url_shortner.models.User;
import com.url.url_shortner.service.UrlMappingService;
import com.url.url_shortner.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
@RequestMapping("/api/urls")
@RestController
@AllArgsConstructor
public class UrlMappingController {


    private UrlMappingService urlMappingService;

//    fetch the user details
    private UserService userService;


//we are accepting the priniciple because we also want to know in url table which user created the url for that reason we accepting principal..spring sec knows the user
//    {"origUrl":"https://google.com"}
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> request,


                                                        Principal principal)
    {

        String originalUrl=request.get("originalUrl");
//        EVERY SHORTURL mapped with the user so thats why user info fetch
        User user=userService.findByUsername(principal.getName());

UrlMappingDTO urlMappingDTO= urlMappingService.createShortUrl(originalUrl,user);
return  ResponseEntity.ok(urlMappingDTO);

    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
//    princioal is auto injected if the user is authenticated
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
//        int this entire process we make use help of security context to finding the user
        User user =userService.findByUsername(principal.getName());
        List<UrlMappingDTO> urls=urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);

    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
//    princioal is auto injected if the user is authenticated
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate,

                                                               @RequestParam ("endDate") String endDate
                                                               ){


        DateTimeFormatter formatter =DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        // iso format is 2024-12-0100:00:00.as it is good because if you want to filter by time then also you can also do it

        LocalDateTime start=LocalDateTime.parse(startDate,formatter);
        LocalDateTime end=LocalDateTime.parse(endDate,formatter);
        List<ClickEventDTO> clickEventDTOS= urlMappingService.getClickEventsByDate(shortUrl,start,end);
        return ResponseEntity.ok(clickEventDTOS);





    }


    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
//    princioal is auto injected if the user is authenticated
    public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate(Principal principal,@RequestParam("startDate") String startDate,

                                                                    @RequestParam ("endDate") String endDate){


        DateTimeFormatter formatter =DateTimeFormatter.ISO_LOCAL_DATE;
        // iso format is 2024-12-0100:00:00.as it is good because if you want to filter by time then also you can also do it
User user=userService.findByUsername(principal.getName());
        LocalDate start=LocalDate.parse(startDate,formatter);
        LocalDate end=LocalDate.parse(endDate,formatter);
        Map<LocalDate,Long> totalClicks= urlMappingService.getTotalClicksByUserAndDate(user,start,end);
        return ResponseEntity.ok(totalClicks);


    }

}
