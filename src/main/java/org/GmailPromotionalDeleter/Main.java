package org.GmailPromotionalDeleter;

import jakarta.mail.*;
import jakarta.mail.search.BodyTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        String host = "imap.gmail.com";
        String username = "<enter your email>";
        String password = "<enter your app password>";

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");
        try {
            Session session = Session.getDefaultInstance(properties, null);

            Store store = session.getStore("imaps");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            SubjectTerm subjectTerm = new SubjectTerm("unsubscribe");
            BodyTerm bodyTerm = new BodyTerm("unsubscribe");
            SearchTerm searchTerm = new OrTerm(subjectTerm, bodyTerm);
            Message[] messages = inbox.search(searchTerm);
            System.out.println("Number of emails containing 'unsubscribe': " + messages.length);

            System.out.println("deleting these emails");
            for(Message m:messages){
                m.setFlag(Flags.Flag.DELETED,true);
            }

            inbox.close(true);
            store.close();
        } catch (Exception e) {
            System.out.println("Something broke" + e.toString());
        }
    }
}