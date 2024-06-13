package org.example;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class MessageController {
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    private SessionFactory sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();


    public void createMessage(String author, String message){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createNativeQuery("INSERT INTO messages(author, text_wroten) VALUES (:author, :message)");
        query.setParameter("author", author);
        query.setParameter("message", message);
        query.executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public List<Messages> getMessages() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Messages> messages = session.createQuery("select u from Messages u", Messages.class).list();


        session.getTransaction().commit();
        session.close();

        return messages;
    }

}
