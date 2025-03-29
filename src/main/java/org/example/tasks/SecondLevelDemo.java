package org.example.tasks;


import org.example.entity.AuthorEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SecondLevelDemo {

    public static void demo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try(Session session1 = sessionFactory.openSession()) {

                session1.beginTransaction();
                System.out.println("Get entity first time in session1");
                AuthorEntity author1 = session1.find(AuthorEntity.class, 1L);
                System.out.println(author1);
                System.out.println("Get entity second time in session1");
                AuthorEntity author2 = session1.find(AuthorEntity.class, 1L);
                System.out.println(author2);

                session1.getTransaction().commit();
            }

            try(Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                System.out.println("Get entity first time in session2");
                AuthorEntity author1 = session2.find(AuthorEntity.class, 1L);
                System.out.println(author1);

                session2.getTransaction().commit();
            }

        }
    }

}
