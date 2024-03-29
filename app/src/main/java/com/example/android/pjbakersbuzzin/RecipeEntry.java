package com.example.android.pjbakersbuzzin;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeEntry implements Parcelable {

    public static final Creator<RecipeEntry> CREATOR = new Creator<RecipeEntry>() {
        @Override
        public RecipeEntry createFromParcel(Parcel in) {
            return new RecipeEntry(in);
        }

        @Override
        public RecipeEntry[] newArray(int size) {
            return new RecipeEntry[size];
        }
    };

    private String recipeId;
    private String name;
    private String ingredients;
    private String steps;
    private String servings;
    private String image;

    // Constructor
    public RecipeEntry(String recipeId, String name, String ingredients,
                       String steps, String servings, String image) {
        this.recipeId = recipeId;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public String getId() {
        return recipeId;
    }

    public void setId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Parcelling part
    private RecipeEntry(Parcel in) {
        this.recipeId = in.readString();
        this.name = in.readString();
        this.ingredients = in.readString();
        this.steps = in.readString();
        this.servings = in.readString();
        this.image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.recipeId);
        dest.writeString(this.name);
        dest.writeString(this.ingredients);
        dest.writeString(this.steps);
        dest.writeString(this.servings);
        dest.writeString(this.image);
    }

    @Override
    public String toString() {
        return "MovieEntry{" +
                "recipeId='" + recipeId + '\'' +
                ", name='" + name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", steps='" + steps + '\'' +
                ", servings='" + servings + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

//public class IngredientsList {
//
//    private String quantity;
//    private String measure;
//    private String ingredient;
//
//    public IngredientsList(String quantity, String measure, String ingredient) {
//        this.quantity = quantity;
//        this.measure = measure;
//        this.ingredient = ingredient;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getMeasure() {
//        return measure;
//    }
//
//    public void setMeasure(String measure) {
//        this.measure = measure;
//    }
//
//    public String getIngredient() {
//        return ingredient;
//    }
//
//    public void setIngredient(String ingredient) {
//        this.ingredient = ingredient;
//    }
//}
//
//public class StepsList {
//    private String id;
//    private String shortDescription;
//    private String description;
//    private String videoUrl;
//    private String thumbnailUrl;
//
//    public StepsList(String id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
//        this.id = id;
//        this.shortDescription = shortDescription;
//        this.description = description;
//        this.videoUrl = videoUrl;
//        this.thumbnailUrl = thumbnailUrl;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getShortDescription() {
//        return shortDescription;
//    }
//
//    public void setShortDescription(String shortDescription) {
//        this.shortDescription = shortDescription;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getVideoUrl() {
//        return videoUrl;
//    }
//
//    public void setVideoUrl(String videoUrl) {
//        this.videoUrl = videoUrl;
//    }
//
//    public String getThumbnailUrl() {
//        return thumbnailUrl;
//    }
//
//    public void setThumbnailUrl(String thumbnailUrl) {
//        this.thumbnailUrl = thumbnailUrl;
//    }
//
//}