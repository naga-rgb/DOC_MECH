package com.example.doc_mech;

public class Rating_class {
    String rating,review,username;

    public Rating_class(String rating, String review, String username) {
        this.rating = rating;
        this.review = review;
        this.username = username;
    }

    public Rating_class() {
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

