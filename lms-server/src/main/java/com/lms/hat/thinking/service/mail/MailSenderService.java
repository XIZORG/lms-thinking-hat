package com.lms.hat.thinking.service.mail;

import com.lms.hat.thinking.config.security.jwt.JwtUtils;
import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.repository.UserEntityRepository;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

@Component
@Transactional
public class MailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final UserEntityRepository userRepository;
    private final JavaMailSender emailSender;

    @Autowired
    public MailSenderService(UserEntityRepository userRepository, JavaMailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    private static String createICalInvitation(Course course) {

        String location = "Location - Kharkiv";
        String hostEmail = "leotextxi@gmail.com";

        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        java.util.Date dateFromCourse = Date.from(course.getBeginDate());
        Date startDt = new Date(dateFromCourse);

        VEvent meeting = new VEvent(startDt, "Course reminder: " + course.getName());
        meeting.getProperties().add(new Description("The event is about to start!"));
        meeting.getProperties().add(new Location(location));

        try {
            meeting.getProperties().add(new Organizer(hostEmail));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        calendar.getComponents().add(meeting);
        return calendar.toString();
    }

    @Scheduled(fixedRate = 72000000)
    public void scheduleMessageSender() throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        message.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");
        message.setSubject("Course reminder");
        logger.info("Start mailing");
        List<UserEntity> users = userRepository.findAll();

        for (UserEntity user : users) {
            for (Course course : user.getCourses()) {
                long beginMils = course.getBeginDate().toEpochMilli();
                long nowMils = Instant.now().toEpochMilli();
                if (beginMils < (nowMils + 86400000) && user.getEmail() != null && beginMils > nowMils) {
                    helper.setTo(user.getEmail());

                    DataSource iCalData = new ByteArrayDataSource(createICalInvitation(course),
                            "text/calendar; charset=UTF-8");
                    message.setDataHandler(new DataHandler(iCalData));

                    try {
                        emailSender.send(message);
                    } catch (Exception ex) {
                        logger.error("Can`t sent message, text: {}", ex.toString());
                    }
                }
            }
        }
    }
}
