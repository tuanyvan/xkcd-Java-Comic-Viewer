package com.tuanyvan.xkcdjavacomicviewer;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;

public class Comic {

    private String title;
    private String altText;
    private LocalDate publishedDate;
    private int comicID;
    private String imgURI;

    public Comic(JSONObject json) {
        title = json.get("title").toString();
        altText = json.get("alt").toString();

        HashMap<String, Integer> date = new HashMap<>();
        date.put("year", Integer.valueOf(json.get("year").toString()));
        date.put("month", Integer.valueOf(json.get("month").toString()));
        date.put("day", Integer.valueOf(json.get("day").toString()));

        publishedDate = LocalDate.of(date.get("year"), date.get("month"), date.get("day"));

        comicID = (int) json.get("num");
        imgURI = json.get("img").toString();
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

    // Setters
    public void setImgURI(String imgURI) {
        this.imgURI = imgURI;
    }

    // Custom Methods
    /**
     * @return String representing the time since the comic's publication. Specifies the years since and months since publication if the comic is old enough.
     */
    public String getTimeSincePublication() {
        LocalDate today = LocalDate.now();
        Period timeDiff = Period.between(publishedDate, today);
        String monthField = determinePluralDuration("month", timeDiff.getMonths());
        String dayField = determinePluralDuration("day", timeDiff.getDays());

        if (timeDiff.getYears() >= 1) {
            String yearField = determinePluralDuration("year", timeDiff.getYears());
            return String.format(
                    "%d %s, %d %s, %d %s",
                    timeDiff.getYears(),
                    yearField,
                    timeDiff.getMonths(),
                    monthField,
                    timeDiff.getDays(),
                    dayField
            );
        } else if (timeDiff.getMonths() >= 1) {
            return String.format(
                    "%d %s, %d %s",
                    timeDiff.getMonths(),
                    monthField,
                    timeDiff.getDays(),
                    dayField
            );
        } else {
            return String.format("%d %s", timeDiff.getDays(), dayField);
        }
    }

    private String determinePluralDuration(String duration, int length) {
        return length == 1 ? duration : duration + "s";
    }

}
