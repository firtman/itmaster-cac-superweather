package app.itmaster.mobile.superweather.model;

public class City {

    public City(String name, String emoji) {
        this.name = name;
        this.countryEmoji = emoji;
    }

    private String name;
    private String countryEmoji;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryEmoji() {
        return countryEmoji;
    }

    public void setCountryEmoji(String countryEmoji) {
        this.countryEmoji = countryEmoji;
    }
}
