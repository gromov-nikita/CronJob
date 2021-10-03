package service.handler.Cron;

import service.db.query.Queries;
import java.util.concurrent.TimeUnit;

public class CronJob implements Runnable {
    private Queries queries;
    private int min = 1;
    public CronJob(Queries queries) {
        this.queries = queries;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMin() {
        return min;
    }
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(min));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
