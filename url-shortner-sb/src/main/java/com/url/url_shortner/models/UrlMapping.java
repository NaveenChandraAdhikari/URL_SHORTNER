package com.url.url_shortner.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;



@Entity
@Data

public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shortUrl;
    private String originalUrl;
    private int clickCount=0;
    private LocalDateTime createdDate;


//    many url mappings to one user...
    //foreign key (references users.id )
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "urlMapping")
    private List<ClickEvent> clickEvents;
}


/*
The List<ClickEvent> clickEvents field in the UrlMapping entity does not directly translate to a column in the database table. Instead, it represents a one-to-many relationship between the UrlMapping and ClickEvent entities.

This relationship is modeled in the database using a foreign key in the click_event table, pointing to the id column of the url_mapping table. Here's how it works:

    In Java (Entity Relationship):
        @OneToMany(mappedBy = "urlMapping") in the UrlMapping class.
        @ManyToOne in the ClickEvent class with a JoinColumn(name = "url_mapping_id").

    In the Database (Physical Table Representation):
        No additional column for clickEvents in the url_mapping table.
        The click_event table contains a column (url_mapping_id) to establish the link.


            This means that Hibernate or JPA will automatically fetch all related ClickEvent entries for a given UrlMapping when needed.

The ClickEvent entity has a UrlMapping urlMapping field annotated with @ManyToOne.

    This means that each ClickEvent "knows" which UrlMapping it belongs to.

    Imagine a UrlMapping for the short URL short.ly/xyz:

    This entry is stored in the url_mapping table.
    Each time someone clicks the short URL:
        A new row is added to the click_event table.
        This row links back to short.ly/xyz via the url_mapping_id column.
 */