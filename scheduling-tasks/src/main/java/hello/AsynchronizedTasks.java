package hello;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Component
public class AsynchronizedTasks {
    private ScheduledFuture scheduledFuture;

    @Async
    public void executeTaskT() {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        Long now = System.currentTimeMillis();
        Date date = new Date(now + 10* 1000);

        scheduledFuture = new ConcurrentTaskScheduler(localExecutor)
                .schedule(() -> {
                    System.out.println("executed task!");

                    scheduledFuture.cancel(true);
                }, date);
    }

    @PostConstruct
    public void initialize() {
        this.executeTaskT();
    }
}
