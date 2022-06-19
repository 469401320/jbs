package me.hao0.jbs.alarm.notify;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import me.hao0.jbs.alarm.alarmer.AlarmContext;
import me.hao0.jbs.alarm.config.MailConfig;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.enums.AlarmNotifyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


@Component
@NotifierMeta(type = AlarmNotifyType.EMAIL)
public class EmailNotifier implements Notifier {

    @Autowired
    private MailConfig mailConfig;

    private final Properties mailProps = new Properties();

    @PostConstruct
    public void init(){
        mailProps.put("mail.smtp.host", mailConfig.getHost());
        mailProps.put("mail.smtp.auth", "true");
    }


    @Override
    public Boolean notify(AlarmContext context) {
        Transport transport;

        try {

            Session session = Session.getInstance(mailProps);

            MimeMessage message = new MimeMessage(session);


            message.setFrom(new InternetAddress(mailConfig.getFromAddr()));


            message.setRecipients(Message.RecipientType.TO, mailConfig.getTo());


            if (!Strings.isNullOrEmpty(mailConfig.getBcc())){
                message.setRecipients(Message.RecipientType.BCC, mailConfig.getBcc());
            }

            message.setSubject(context.getSubject());
            message.setText(context.getBody(), "UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();

            transport = session.getTransport("smtp");

            transport.connect(mailConfig.getFromUser(), mailConfig.getFromPass());

            transport.sendMessage(message, message.getAllRecipients());

        } catch (Exception e){
            Logs.error("failed to alarm(context={}), cause: {}", context, Throwables.getStackTraceAsString(e));
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
