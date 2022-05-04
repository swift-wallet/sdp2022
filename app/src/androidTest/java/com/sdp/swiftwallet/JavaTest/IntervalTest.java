//package com.sdp.swiftwallet.JavaTest;
//
//import com.sdp.swiftwallet.Interval;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//
//@RunWith(JUnit4.class)
//public class IntervalTest {
//    String[] oneHour = {"1 Hour", "1h"};
//    String[] fourHours = {"4 Hours", "4h"};
//    Interval oneHourInterval = Interval.oneHour;
//    Interval fourHourInterval = Interval.fourHours;
//
//    @Test
//    public void getTextReturnsRightValue(){
//        assert(oneHourInterval.getText().equals(oneHour[0]));
//        assert(fourHourInterval.getText().equals(fourHours[0]));
//    }
//
//    @Test
//    public void getIntervalRequestReturnsRightValue(){
//        assert(oneHourInterval.getIntervalRequest().equals(oneHour[1]));
//        assert(fourHourInterval.getIntervalRequest().equals(fourHours[1]));
//    }
//
//    @Test
//    public void getTextArrayReturnsRightNumberOfValues(){
//        assert(Interval.getTextToShowUser().size()==15);
//    }
//
//    @Test
//    public void getIntervalRequestArrayReturnsRightNumberOfValues(){
//        assert(Interval.getIntervalForRequest().size()==15);
//    }
//}
