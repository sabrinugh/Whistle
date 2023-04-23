package com.example.ddating;

import android.graphics.Bitmap;

public class CourseModal {

    // variables for our coursename,
    // description,tracks and duration,imageId.
    private String courseName;
    private String courseDuration;
    private String courseTracks;
    private String courseDescription;


    // Get Image
    private int imgId;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }


    private Bitmap dogImage;

    public Bitmap getDogImage() {
        return dogImage;
    }

    public void setDogImage(Bitmap dogImage) {
        this.dogImage = dogImage;
    }
    // End Get Image

    // creating getter and setter methods
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseTracks() {
        return courseTracks;
    }

    public void setCourseTracks(String courseTracks) {
        this.courseTracks = courseTracks;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    // constructor.
    public CourseModal(String courseName, String courseDuration, String courseTracks, String courseDescription, Bitmap dogImage) {
        this.courseName = courseName;
        this.courseDuration = courseDuration;
        this.courseTracks = courseTracks;
        this.courseDescription = courseDescription;
        this.dogImage = dogImage;
    }
}
