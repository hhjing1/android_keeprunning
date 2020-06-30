package com.weather.service;

public class AutoUpdateService{
//    public AutoUpdateService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        updateBingPic();
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int anTime = 8 * 60 * 60 *1000;
//        long triggerAtTime = SystemClock.elapsedRealtime() + anTime;
//        Intent intentSelf = new Intent(this, AutoUpdateService.class);
//        PendingIntent pi = PendingIntent.getService(this, 0, intentSelf, 0);
//        alarmManager.cancel(pi);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
//        return super.onStartCommand(intent, flags, startId);
//    }

}
