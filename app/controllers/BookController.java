package controllers;

import models.Book;
import models.BookRepository;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class BookController extends Controller {

    private final FormFactory formFactory;
    private final BookRepository bookRepository;
    private final HttpExecutionContext ec;

    @Inject
    public BookController(FormFactory formFactory, BookRepository bookRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.bookRepository = bookRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> addBook() {
        Book book = formFactory.form(Book.class).bindFromRequest().get();
        return bookRepository.add(book).thenApplyAsync(p -> {
            return redirect(routes.BookController.index());
        }, ec.current());
    }

    public CompletionStage<Result> getBooks() {
        return bookRepository.list().thenApplyAsync(bookStream -> {
            return ok(toJson(bookStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> searchBooks() {
        String[] keys = Controller.request().queryString().get("key");
        return bookRepository.search(keys[0]).thenApplyAsync(bookStream -> {
            return ok(toJson(bookStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getBook() {
        String[] ids = Controller.request().queryString().get("id");
        Integer id = 0;
        try {
            id = Integer.parseInt(ids[0]);
        } catch (Exception ex) {
        }
        return bookRepository.get(id).thenApplyAsync(book -> {
            return ok(toJson(book));
        }, ec.current());
    }

    public CompletionStage<Result> updateBook() {
        Book book = formFactory.form(Book.class).bindFromRequest().get();
        return bookRepository.add(book).thenApplyAsync(p -> {
            return redirect(routes.BookController.index());
        }, ec.current());
    }

    public CompletionStage<Result> deleteBook() {
//        DynamicForm form = formFactory.form().bindFromRequest();
        String[] ids = Controller.request().queryString().get("id");
        Integer id = 0;
        try {
            id = Integer.parseInt(ids[0]);
        } catch (Exception ex) {
        }
        return bookRepository.delete(id).thenApplyAsync(p -> {
            return redirect(routes.BookController.index());
        }, ec.current());
    }

}
