package com.example.sophmore.pointb.model;

import com.parse.Parse;
import com.parse.ParseObject;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import java.util.Date;

/**
 * BucketItem
 *
 * Model for an item on a bucket list
 * Contains the title of the object and how many likes it has
 * Also conatins the original parse object
 * @note likes were not used in this project, but implemented on the database for future use
 */
public class BucketItem {

    private ParseObject bucketItem;
    private String title;
    private int likes;

    public BucketItem() {
        super();
    }

    public BucketItem(ParseObject bucketItem) {
        super();
        this.bucketItem = bucketItem;
        title = (String) bucketItem.get("title");
        likes = (Integer) bucketItem.get("likes");
    }

    public void like() {

    }

    public void unlike() {

    }

    public ParseObject getBucketItem() {
        return bucketItem;
    }

    //Meehod that finds out the difference between when the item was created and the current time and displays
    //a proper text
    public String getTimeString() {
        Date createdDate = bucketItem.getCreatedAt();
        Date currentDate = new Date();

        DateTime dt1 = new DateTime(createdDate);
        DateTime dt2 = new DateTime(currentDate);

        int years = Years.yearsBetween(dt1, dt2).getYears();
        int months = Months.monthsBetween(dt1, dt2).getMonths();
        int weeks = Weeks.weeksBetween(dt1, dt2).getWeeks();
        int days = Days.daysBetween(dt1, dt2).getDays();
        int hours = Hours.hoursBetween(dt1, dt2).getHours();
        int minutes = Minutes.minutesBetween(dt1, dt2).getMinutes();
        if (minutes < 1) {
            return "Just Now";
        } else if (hours < 1) {
            if (minutes == 1) {
                return "A minute ago";
            } else {
                return minutes + " minutes ago";
            }
        } else if (days < 1) {
            if (hours == 1) {
                return "An hour ago";
            } else {
                return hours + " hours ago";
            }
        } else if (weeks < 1) {
            if (days == 1) {
                return "A day ago";
            } else {
                return days + " days ago";
            }
        } else if (months < 1) {
            if (weeks == 1) {
                return "A week ago";
            } else {
                return weeks + " weeks ago";
            }
        } else if (years < 1) {
            if (months == 1) {
                return "A month ago";
            } else {
                return months + " months ago";
            }
        } else {
            if (years == 1) {
                return "A year ago";
            } else {
                return years + " years ago";
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public int getLikes() {
        return likes;
    }
}
