package library.server;

import io.grpc.stub.StreamObserver;
import proto.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class LibraryServiceImpl extends LibraryServiceGrpc.LibraryServiceImplBase {
    private static final Logger logger = Logger.getLogger(LibraryServiceImpl.class.getName());

    private final List<Book> bookList = new ArrayList<>();

    @Override
    public void addBook(Book request, StreamObserver<BookResponse> responseObserver) {
        logger.info("Received request to add book: " + request.getTitle());
        if (bookList.stream().anyMatch(b -> b.getIsbn().equals(request.getIsbn()))) {
            logger.warning("Book with ISBN " + request.getIsbn() + " already exists.");
            responseObserver.onError(new RuntimeException("Book with ISBN " + request.getIsbn() + " already exists."));
            return;
        }
        bookList.add(request);

        BookResponse response = BookResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Book added successfully!")
                .setBookId(request.getIsbn())
                .build();

        logger.info("Book added: " + request.getTitle());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBook(BookRequest request, StreamObserver<Book> responseObserver) {
        logger.info("Received request to get book with ISBN: " + request.getIsbn());

        Optional<Book> book = bookList.stream()
                .filter(b -> b.getIsbn().equals(request.getIsbn()))
                .findFirst();

        if (book.isPresent()) {
            logger.info("Returning book: " + book.get().getTitle());
            responseObserver.onNext(book.get());
        } else {
            logger.warning("Book not found with ISBN: " + request.getIsbn());
            responseObserver.onError(new RuntimeException("Book not found"));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(Book request, StreamObserver<BookResponse> responseObserver) {
        logger.info("Received request to update book: " + request.getTitle());

        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getIsbn().equals(request.getIsbn())) {
                bookList.set(i, request);
                BookResponse response = BookResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Book updated successfully!")
                        .setBookId(request.getIsbn())
                        .build();

                logger.info("Book updated: " + request.getTitle());
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        }

        logger.warning("Book not found for update: " + request.getIsbn());
        responseObserver.onError(new RuntimeException("Book not found for update"));
    }

    @Override
    public void deleteBook(BookRequest request, StreamObserver<BookResponse> responseObserver) {
        logger.info("Received request to delete book with ISBN: " + request.getIsbn());

        boolean removed = bookList.removeIf(book -> book.getIsbn().equals(request.getIsbn()));

        if (removed) {
            BookResponse response = BookResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Book deleted successfully!")
                    .setBookId(request.getIsbn())
                    .build();

            logger.info("Book deleted with ISBN: " + request.getIsbn());
            responseObserver.onNext(response);
        } else {
            logger.warning("Book not found with ISBN: " + request.getIsbn());
            responseObserver.onError(new RuntimeException("Book not found for deletion"));
        }

        responseObserver.onCompleted();
    }

}
