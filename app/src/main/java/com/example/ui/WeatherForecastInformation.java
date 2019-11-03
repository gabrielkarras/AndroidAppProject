package com.example.ui;

public class WeatherForecastInformation {
    public enum WeatherType
    {
        COLD, CHILLING, WARM;
    }
    private double Maxtemp;
    private double Mintemp;
    private double AvgTemp;
    private boolean IsGonnaRain;
    private boolean IsGonnaSnow;
    public String[] ColdWeatherItems = {"Winter Coat", "Winter Boots", "Gloves"};
    public String[] ChillingWeatherItems = {"Jacket", "Warm Shoes", "Scarf"};
    public String[] WarmWeatherItems = {"T-shirt", "Light Shoes", "Shorts"};
    private String[] RainItems = {"Umbrella"};
    private String[] SnowItems = {"Winter Boots"};

    public double getMaxtemp() {
        return Maxtemp;
    }

    public void setMaxtemp(double maxtemp) {
        Maxtemp = maxtemp;
    }

    public double getMintemp() {
        return Mintemp;
    }

    public void setMintemp(double mintemp) {
        Mintemp = mintemp;
    }

    public double getAvgTemp() {
        return ((Maxtemp + Mintemp)/2);
    }

    public boolean isGonnaRain() {
        return IsGonnaRain;
    }

    public void setGonnaRain(int RainProbability) {
        if (RainProbability > 40)
            IsGonnaRain = true;
        else
            IsGonnaRain = false;
    }

    public boolean isGonnaSnow() {
        return IsGonnaSnow;
    }

    @Override
    public String toString() {
        return "WeatherForecastInformation{" +
                "Maxtemp=" + Maxtemp +
                ", Mintemp=" + Mintemp +
                ", AvgTemp=" + getAvgTemp() +
                ", IsGonnaRain=" + IsGonnaRain +
                ", IsGonnaSnow=" + IsGonnaSnow +
                '}';
    }

    public void setGonnaSnow(int SnowProbability) {
        if (SnowProbability > 40)
            IsGonnaSnow = true;
        else
            IsGonnaSnow = false;
    }

    public WeatherType getWeatherType()
    {
        double AvgTemp = (Maxtemp + Mintemp)/2;

        if (AvgTemp < 32)
        {
            return WeatherType.COLD;
        }
        else if (AvgTemp < 70)
        {
            return WeatherType.CHILLING;
        }
        else
        {
            return WeatherType.WARM;
        }

    }
}
