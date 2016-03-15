package com.krisztian.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by uflik on 2/1/16.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static final int PERIOD = 60000;
    public static final int DELAY = 1000;

    public static void main(String... args) throws IOException {
        LOGGER.info("Notifier application started at: {}", ZonedDateTime.now());
        List<String> collectedLinks = new ArrayList<>();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    String linksToSend = "";
                    for (String str : Collector.collect()) {
                        if (!collectedLinks.contains(str)) {
                            collectedLinks.add(str);
                            linksToSend += str + "\n\n";
                        }
                    }

                    if (linksToSend != "") {
                        MailSender.sendMailWithDefaultSettings(linksToSend);
                    }
                } catch (IOException e) {
                    LOGGER.error("Error during the application running: ", e);
                }
            }
        }, DELAY, PERIOD);
    }

    private Main() {
    }
}
