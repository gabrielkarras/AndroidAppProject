package com.example.ui;

public class WeatherForecastInformation {
    public enum WeatherType
    {
        COLD, CHILLING, WARM;
    }
    public enum WeatherCondition
    {
        SUNNY, RAIN, SNOW;
    }
    private double Maxtemp;
    private double Mintemp;
    private double AvgTemp;
    private double DecimalMaxtemp;
    private double DecimalMintemp;
    private double DecimalAvgTemp;
    private double WindSpeed;
    private double WindGustSpeed;
    private double RainTotal;
    private double SnowTotal;
    private WeatherCondition WeatherCondition;
    private String WeatherConditionPhrase;
    private boolean IsGonnaRain;
    private boolean IsGonnaSnow;
    public String[] ColdWeatherItems = {"Winter Coat", "Winter Boots", "Gloves"};
    public String[] ChillingWeatherItems = {"Jacket", "Warm Shoes", "Scarf"};
    public String[] WarmWeatherItems = {"T-shirt", "Light Shoes", "Shorts"};

    public WeatherForecastInformation.WeatherCondition getWeatherCondition() {
        return WeatherCondition;
    }

    public void setWeatherCondition(int weatherCondition) {
        if (weatherCondition < 12)
            WeatherCondition = WeatherCondition.SUNNY;
        else if (weatherCondition < 22)
            WeatherCondition = WeatherCondition.RAIN;
        else
            WeatherCondition = WeatherCondition.SNOW;
    }

    public String getWeatherConditionPhrase() {
        return WeatherConditionPhrase;
    }

    public void setWeatherConditionPhrase(String weatherConditionPhrase) {
        WeatherConditionPhrase = weatherConditionPhrase;
    }

    public double getRainTotal() {
        return RainTotal;
    }

    public void setRainTotal(double rainTotal) {
        RainTotal = rainTotal;
    }

    public double getSnowTotal() {
        return SnowTotal;
    }

    public void setSnowTotal(double snowTotal) {
        SnowTotal = snowTotal;
    }

    public double getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        WindSpeed = windSpeed;
    }

    public double getWindGustSpeed() {
        return WindGustSpeed;
    }

    public void setWindGustSpeed(double windGustSpeed) {
        WindGustSpeed = windGustSpeed;
    }

    public double getDecimalMaxtemp() {
        return DecimalMaxtemp;
    }

    public void setDecimalMaxtemp(double decimalMaxtemp) {
        DecimalMaxtemp = decimalMaxtemp;
    }

    public double getDecimalMintemp() {
        return DecimalMintemp;
    }

    public void setDecimalMintemp(double decimalMintemp) {
        DecimalMintemp = decimalMintemp;
    }

    public double getDecimalAvgTemp() {
        return ((DecimalMaxtemp + DecimalMintemp)/2);
    }

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
