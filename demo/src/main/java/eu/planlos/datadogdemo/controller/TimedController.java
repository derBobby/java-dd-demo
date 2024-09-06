package eu.planlos.datadogdemo.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;

public class TimedController {

    @Timed(value = "timedApi", description = "A timed API in a demo")
    @GetMapping("/random")
    public ResponseEntity<String> random() throws InterruptedException {
        Random random = new Random();
        long randomLong = random.nextLong(2001);
        Thread.sleep(randomLong);
        return ResponseEntity.ok("Waited " + randomLong + "ms");
    }

    @Scheduled(fixedDelay = 5000)
    @Timed(value = "timedScheduler", description = "A timed API in a demo")
    public void scheduled() throws InterruptedException {
        Random random = new Random();
        long randomLong = random.nextLong(2001);
        Thread.sleep(randomLong);
        System.out.println(randomLong);
    }
}
