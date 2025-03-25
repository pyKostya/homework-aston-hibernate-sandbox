package org.example;

import org.example.entity.AuthorEntity;
import org.example.entity.BookEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            AuthorEntity author = AuthorEntity.builder()
                    .name("Author")
                    .build();
            session.persist(author);

            BookEntity.builder()
                    .name("Book")
                    .author(author)
                    .build();

            session.getTransaction().commit();
        }
    }
}
