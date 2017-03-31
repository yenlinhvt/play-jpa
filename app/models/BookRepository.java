package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPABookRepository.class)
public interface BookRepository {

    CompletionStage<Book> add(Book book);

    CompletionStage<Book> update(Book book);

    CompletionStage<Book> delete(Integer id);

    CompletionStage<Stream<Book>> list();

    CompletionStage<Stream<Book>> search(String key);

    CompletionStage<Book> get(Integer id);
}
