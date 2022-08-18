package com.flight.sf.common;

import java.util.Arrays;

/**
 * @author FLIGHT
 * @creationDate 15.08.2022
 */
public enum CategoryColor {

    BLUE("0", "Физическое развитие"),
    PURPLE("1", "Интеллектуальное развитие"),
    RED("11", "Собственные проекты"),
    LIGHTPINK("4", "Профессиональное развитие"),
    YELLOW("5", "Движение"),
    UNDEFINED("undefined", "undefined");


    private String id;
    private String categoryName;

    CategoryColor(String id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static String getCategoryNameById(String id) {
        if (id == null) return BLUE.getCategoryName();

        return Arrays.stream(CategoryColor.values())
                .filter(categoryColor -> categoryColor.getId().equals(id))
                .findFirst().orElse(CategoryColor.UNDEFINED).getCategoryName();
    }
}
