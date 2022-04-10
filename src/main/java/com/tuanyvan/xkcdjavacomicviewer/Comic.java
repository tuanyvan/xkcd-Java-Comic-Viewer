package com.tuanyvan.xkcdjavacomicviewer;

import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;

public class Comic {

    // The user never changes most of these fields. They are parsed as-is from the JSON payload.
    private String title;
    private String altText;
    private LocalDate publishedDate;
    private int comicID;
    private String imgURL;

    /**
     * Instantiates a Comic object a JSONObject containing comic data.
     * @param json  The JSON object containing information about the Comic.
     */
    public Comic(JSONObject json) {
        setTitle(json.get("title").toString());
        setAltText(json.get("alt").toString());

        // Publication date held in three different fields. We can get them to instantiate a LocalDate class.
        HashMap<String, Integer> date = new HashMap<>();
        date.put("year", Integer.valueOf(json.get("year").toString()));
        date.put("month", Integer.valueOf(json.get("month").toString()));
        date.put("day", Integer.valueOf(json.get("day").toString()));

        setPublishedDate(LocalDate.of(date.get("year"), date.get("month"), date.get("day")));

        setComicID((int) json.get("num"));

        // Use setImgURL to ensure the passed JSON image URL is valid.
        setImgURL(json.get("img").toString());
    }

    // Getters
    /**
     * @return  The title of the comic as a String.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return  The alt attribute text of the comic as a String.
     */
    public String getAltText() {
        return altText;
    }

    /**
     * @return  The LocalDate object representing the Comic's creation date.
     */
    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    /**
     * @return  The comic's ID as an int.
     */
    public int getComicID() {
        return comicID;
    }

    /**
     * @return  The comic's image URL as a String.
     */
    public String getImgURL() {
        return imgURL;
    }

    // Setters (users should never have to use these)
    /**
     * Verifies the integrity and validity of the image URL using Apache Commons' validators.
     * @param imgURL    The URL to the comic's image.
     */
    public void setImgURL(String imgURL) {

        // Use Apache Commons UrlValidator to check the URL format, which is more advanced than the native check with the URL object.
        UrlValidator validator = new UrlValidator();
        if (validator.isValid(imgURL)) {

            // Check if the link actually redirects to a valid HTTPS file. If the request throws an exception, then it's not valid.
            try {
                InputStream request = new URL(imgURL).openConnection().getInputStream();
            }
            catch (IOException mue) {
                throw new IllegalArgumentException("The provided URL is not a valid link.");
            }
            this.imgURL = imgURL;

        }
        else {
            throw new IllegalArgumentException("The provided URL was improperly formatted.");
        }

    }

    /**
     * @param title A string that is at least one character long.
     */
    public void setTitle(String title) {
        if (title.length() >= 1) {
            this.title = title;
        }
        else {
            throw new IllegalArgumentException("The comic's title is too short for an official comic. It must be at least one character.");
        }
    }

    /**
     * @param altText A string that is at least one character long.
     */
    public void setAltText(String altText) {
        if (altText.length() >= 1) {
            this.altText = altText;
        }
        else {
            throw new IllegalArgumentException("The comic's alt text is too short for an official comic.");
        }
    }

    /**
     * @param date A LocalDate object that is not in the future.
     */
    public void setPublishedDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            this.publishedDate = date;
        }
        else {
            throw new IllegalArgumentException("This comic's publish date takes place in the future, which is impossible in our timeline.");
        }
    }

    /**
     * @param id An int that is at least one.
     */
    public void setComicID(int id) {
        if (id >= 1) {
            this.comicID = id;
        }
        else {
            throw new IllegalArgumentException("This comic's ID must be more than zero.");
        }
    }

    // Custom Methods
    /**
     *  Specifies the time since the comic's publication. Also includes the years and months if the comic is old enough.
     *  Example output:
     *  "1 day"
     *  "2 months, 1 day"
     *  "2 years, 1 month, 1 day"
     *  "3 years, 10 months, 0 days"
     *  @return  String representing the time since the comic's publication.
     */
    public String getTimeSincePublication() {

        // Create a Period object to get the comic's time difference.
        LocalDate today = LocalDate.now();
        Period timeDiff = Period.between(publishedDate, today);
        String monthField = determinePluralDuration("month", timeDiff.getMonths());
        String dayField = determinePluralDuration("day", timeDiff.getDays());

        // If any of the fields like year or month are at least 1, include them in the returned String.
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

    /**
     * @param duration  The duration type as a string.
     * @param length    The length that the duration takes up.
     * @return  A pluralized version of the duration string if it has a non-one length.
     */
    private String determinePluralDuration(String duration, int length) {
        return length == 1 ? duration : duration + "s";
    }

    /**
     * @return The original JSON payload with extraneous details removed.
     */
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("title", getTitle());
        json.put("alt", getAltText());
        json.put("year", getPublishedDate().getYear());
        json.put("month", getPublishedDate().getMonth().getValue());
        json.put("day", getPublishedDate().getDayOfMonth());
        json.put("num", getComicID());
        json.put("img", getImgURL());
        return json;
    }

    /**
     * @return  String with the comic's ID and title. Example output: "Comic #404 - Still Searching"
     */
    @Override
    public String toString() {
        return String.format("Comic #%d - %s", getComicID(), getTitle());
    }

}
