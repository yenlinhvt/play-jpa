package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPABookRepository implements BookRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPABookRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Book> add(Book book) {
        return supplyAsync(() -> wrap(em -> insert(em, book)), executionContext);
    }

    @Override
    public CompletionStage<Book> update(Book book) {
        return supplyAsync(() -> wrap(em -> update(em, book)), executionContext);
    }

    @Override
    public CompletionStage<Book> delete(Integer id) {
        return supplyAsync(() -> wrap(em -> delete(em, id)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Book>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Book>> search(String key) {
        return supplyAsync(() -> wrap(em -> search(em, key)), executionContext);
    }

    @Override
    public CompletionStage<Book> get(Integer id) {
        return supplyAsync(() -> wrap(em -> get(em, id)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Book insert(EntityManager em, Book book) {
        em.persist(book);
        return book;
    }

    private Book update(EntityManager em, Book book) {
        em.merge(book);
        return book;
    }

    private Book delete(EntityManager em, Integer id) {
        Book book = em.find(Book.class, id);
        em.remove(book);
        return book;
    }

    private Stream<Book> list(EntityManager em) {
        List<Book> books = em.createQuery("select b from Book b", Book.class).getResultList();
        return books.stream();
    }

    private Stream<Book> search(EntityManager em, String key) {
        List<Book> books = em.createQuery("select b from Book b where b.author like :key", Book.class).setParameter("key", "%" + key +"%").getResultList();
        return books.stream();
    }

    private Book get(EntityManager em, Integer id) {
        Book book = em.find(Book.class, id);
        return book;
    }
}
