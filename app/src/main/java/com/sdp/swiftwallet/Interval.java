package com.sdp.swiftwallet;

import java.util.ArrayList;

public enum Interval {
    oneMinute ("1 Minute", "1m"),
    threeMinutes ("3 Minutes", "3m"),
    fiveMinutes ("5 Minutes", "5m"),
    fifteenMinutes ("15 Minutes", "15m"),
    thrityMinutes ("30 Minutes", "30m"),
    oneHour ("1 Hour", "1h"),
    twoHours ("2 Hours", "2h"),
    fourHours ("4 Hours", "4h"),
    sixHours ("6 Hours", "6h"),
    eightHours ("8 Hours","8h"),
    twelveHours ("12 Hours","12h"),
    oneDay ("1 Day", "1d"),
    threeDays ("3 Days", "3d"),
    oneWeek ("1 Week", "1w"),
    oneMonth ("1 Month", "1M");

    private final String text;
    private final String intervalRequest;

    Interval(String text, String intervalRequest){
        this.text = text;
        this.intervalRequest = intervalRequest;
    }

    private String getText(){
        return text;
    }

    private String getIntervalRequest(){
        return intervalRequest;
    }

    public ArrayList<String> getTextToShowUser(){
        ArrayList<String> toReturn = new ArrayList<>();
        for(Interval interval : Interval.values()){
            toReturn.add(interval.getText());
        }
        return toReturn;
    }

    public ArrayList<String> getIntervalForRequest(){
        ArrayList<String> toReturn = new ArrayList<>();
        for(Interval interval : Interval.values()){
            toReturn.add(interval.getIntervalRequest());
        }
        return toReturn;
    }
}
