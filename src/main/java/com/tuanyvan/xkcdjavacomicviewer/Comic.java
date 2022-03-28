package com.tuanyvan.xkcdjavacomicviewer;

import java.time.LocalDate;
import java.time.Period;

public class Comic {

    private String title;
    private String altText;
    private LocalDate publishedDate;
    private int comicID;
    private String imgURI;

    public Comic(String title, String altText, LocalDate publishedDate, int comicID, String imgURI) {
        this.title = title;
        this.altText = altText;
        this.publishedDate = publishedDate;
        this.comicID = comicID;
        this.imgURI = imgURI;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAltText() {
        return altText;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public int getComicID() {
        return comicID;
    }

    public String getImgURI() {
        return imgURI;
    }

    // Custom Methods

    /**
     * @return String representing the time since the comic's publication. Specifies the years since and months since publication if the comic is old enough.
     */
    public String getTimeSincePublication() {
        LocalDate today = LocalDate.now();
        Period timeDiff = Period.between(publishedDate, today);

        if(timeDiff.getYears() >= 1) {
            return String.format(
                    "%d years, %d months, %d days",
                    timeDiff.getYears(),
                    timeDiff.getMonths(),
                    timeDiff.getDays()
            );
        }
        else if (timeDiff.getMonths() >= 1) {
            return String.format(
                    "%d months, %d days",
                    timeDiff.getMonths(),
                    timeDiff.getDays()
            );
        }
        else {
            return String.format("%d days", timeDiff.getDays());
        }

    }

}
