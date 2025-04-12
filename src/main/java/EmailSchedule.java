import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

public class EmailSchedule {
    public void processEmails(List<EmailTask> immediateEmails, List<EmailTask> scheduledEmails) throws InterruptedException {
        int immediateCount = immediateEmails.size();
        CountDownLatch latch = new CountDownLatch(immediateCount);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(scheduledEmails.size(), runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

        int count = 0;
        for (EmailTask email : scheduledEmails) {
            long delay = Duration.between(LocalDate.now().atStartOfDay(), email.sendTime.atStartOfDay()).toMillis();
            if (delay > 0) {
                scheduler.schedule(() -> EmailSend.sendEmail(email.to, email.subject, email.content),
                        delay, TimeUnit.MILLISECONDS);
            }
            else {
                if(count == 0){
                    System.out.println("Email was successfully sent to ");
                    count = 1;
                }
                System.out.println(email.to + " ");
            }
        }

        latch.await();
    }

    public static class EmailTask {
        String to;
        String subject;
        String content;
        LocalDate sendTime;

        public EmailTask(String to, String subject, String content, LocalDate sendTime) {
            this.to = to;
            this.subject = subject;
            this.content = content;
            this.sendTime = sendTime;
        }
    }
}
