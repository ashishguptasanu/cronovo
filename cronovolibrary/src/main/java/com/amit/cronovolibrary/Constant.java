package com.amit.cronovolibrary;


public class Constant {






    public enum TimePeriod {
        DAILY(86400), WEEKLY(604800), MONTHLY(2678400), ALLTIME(0);

        private final long i;

        TimePeriod(long i) {
            this.i = i;
        }

        public long getTimePeriod() {
            return i;
        }
    }

    public enum RecoveryTime {
        THIRTYSEC(30),SIXTYSEC(60),TWENTYSEC(120);
        private final long i;

        RecoveryTime(long i) {
            this.i = i;
        }

        public long getRecoveryTime() {
            return i;
        }
    }

}
