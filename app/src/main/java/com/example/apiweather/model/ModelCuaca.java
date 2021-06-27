package com.example.apiweather.model;

import java.util.ArrayList;

public class ModelCuaca {

    private ArrayList<weather> weather;

    public ArrayList<weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<ModelCuaca.weather> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "ModelCuaca{" +
                "weather=" + weather +
                '}';
    }

    public class weather{

        private Integer id;
        private String main;
        private String description;
        private String icon;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "weather{" +
                    "id=" + id +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }
}
