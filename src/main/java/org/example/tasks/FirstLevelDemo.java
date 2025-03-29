package org.example.tasks;


import org.example.entity.AuthorEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FirstLevelDemo {

    public static void demo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            System.out.println("Get author by id");
            AuthorEntity authorDb = session.get(AuthorEntity.class, 1L);
            System.out.println(authorDb);

            System.out.println("Get this author one more time");
            AuthorEntity authorDb2 = session.get(AuthorEntity.class, 1L);
            System.out.println(authorDb2);

            session.getTransaction().commit();
        }
    }

}
