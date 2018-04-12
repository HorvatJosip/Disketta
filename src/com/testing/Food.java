package com.testing;

public class Food {
    private String name;
    private String price;
    private String description;
    private String calories;

    public Food(String name, String price, String description, String calories) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", calories='" + calories + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCalories() {
        return calories;
    }
}
