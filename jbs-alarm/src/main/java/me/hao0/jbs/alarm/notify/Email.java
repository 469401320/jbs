package me.hao0.jbs.alarm.notify;

public class Email {


    private String to;
    

    private String bcc;
    

    private String subject;
    

    private String content;

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Email{" +
				"to='" + to + '\'' +
				", bcc='" + bcc + '\'' +
				", subject='" + subject + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}