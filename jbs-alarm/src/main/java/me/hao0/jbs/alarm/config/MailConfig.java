package me.hao0.jbs.alarm.config;

import me.hao0.jbs.common.log.Logs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class MailConfig {

    @Value("${jbs.mail.host:}")
    private String host;

    @Value("${jbs.mail.fromAddr:}")
    private String fromAddr;

    @Value("${jbs.mail.fromUser:}")
    private String fromUser;

    @Value("${jbs.mail.fromPass:}")
    private String fromPass;

    @Value("${jbs.mail.to:}")
    private String to;

    @Value("${jbs.mail.bcc:}")
    private String bcc;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromPass() {
        return fromPass;
    }

    public void setFromPass(String fromPass) {
        this.fromPass = fromPass;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    @PostConstruct
    public void init(){
        Logs.info("Mail config: {}", this);
    }

    @Override
    public String toString() {
        return "MailConfig{" +
                ", host='" + host + '\'' +
                ", fromAddr='" + fromAddr + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", fromPass='" + fromPass + '\'' +
                ", to='" + to + '\'' +
                ", bcc='" + bcc + '\'' +
                '}';
    }
}
