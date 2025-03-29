package org.example;

import lombok.extern.log4j.Log4j;
import org.example.entity.AuthorEntity;
import org.example.entity.BookEntity;
import org.example.entity.PublisherBookEntity;
import org.example.entity.PublisherEntity;
import org.example.tasks.FirstLevelDemo;
import org.example.tasks.NPlusOneProblem;
import org.example.tasks.SecondLevelDemo;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Arrays;
import java.util.Scanner;

@Log4j
public class Main {

    public static void main(String[] args) {

        createTablesAndAddEntity();

        Scanner scanner = new Scanner(System.in);
        String type;
        do {
            System.out.println("""
                Choose type demonstration:
                1 - first level cache
                2 - second level cache
                3 - solver n + 1 problem
                q - out from program
                """);
            type = scanner.next();
            switch (type) {
                case "1" -> firstLevelDemo();
                case "2" -> secondLevelDemo();
                case "3" -> n1Problem();
                case "q" -> {
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Enter another value");
            }
        } while (true);
    }

    private static void firstLevelDemo() {
        System.out.println("FIRST LEVEL DEMO");
        FirstLevelDemo.demo();
    }

    private static void secondLevelDemo() {
        System.out.println("SECOND LEVEL DEMO");
        SecondLevelDemo.demo();
    }

    private static void n1Problem() {
        System.out.println("N+1 PROBLEM DEMO");
        NPlusOneProblem.demo();
    }

    private static void createTablesAndAddEntity() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            AuthorEntity author = saveAuthor("Ivan Petrov");

            BookEntity book = saveBook("My book 1", author);
            BookEntity book2 = saveBook("My book 2", author);
            BookEntity book3 = saveBook("My book 3", author);

            AuthorEntity author2 = saveAuthor("Sergey Rankov");

            BookEntity book4 = saveBook("My book 4", author2);
            BookEntity book5 = saveBook("My book 5", author2);
            BookEntity book6 = saveBook("My book 6", author2);

            session.persist(author);
            session.persist(author2);

            PublisherEntity publisher = savePublisher(session, "One publ");

            PublisherEntity publisher2 = savePublisher(session, "Two publ");

            addBookToPublisher(session, publisher, book2, book3, book4, book5, book6);
            addBookToPublisher(session, publisher2, book, book2, book3, book4, book5);

            session.getTransaction().commit();
        }
    }

    private static PublisherEntity savePublisher(Session session, String name) {
        PublisherEntity publisher = PublisherEntity.builder()
                .name(name)
                .build();

        session.persist(publisher);

        return publisher;
    }

    private static void addBookToPublisher(Session session, PublisherEntity publisher, BookEntity... books ) {
        Arrays.stream(books)
                .map(book -> PublisherBookEntity.builder()
                        .book(book)
                        .publisher(publisher)
                        .build())
                .forEach(session::persist);
    }

    private static BookEntity saveBook(String name, AuthorEntity author) {
        BookEntity book = BookEntity.builder()
                .name(name)
                .build();

        author.addBook(book);

        return book;
    }

    private static AuthorEntity saveAuthor(String name) {
        return AuthorEntity.builder()
                .name(name)
                .build();
    }


}
