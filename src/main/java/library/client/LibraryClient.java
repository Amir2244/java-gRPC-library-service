package library.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.generated.*;

import java.util.logging.Logger;


public class LibraryClient {
    static Logger logger = Logger.getLogger(LibraryClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5000)
                .usePlaintext()
                .build();
        LibraryServiceGrpc.LibraryServiceBlockingStub stub = LibraryServiceGrpc.newBlockingStub(channel);
        Book newBook = Book.newBuilder()
                .setIsbn("123456789")
                .setTitle("Advanced Java")
                .setAuthor(Author.newBuilder()
                        .setFirstName("Amir")
                        .setLastName("Youssef")
                        .setEmail("amir.master@example.com")
                        .addOtherBooks("Java Concurrency ")
                        .build())
                .setPublisher(Publisher.newBuilder()
                        .setName("HIAST")
                        .setCountry("Syria")
                        .setWebsite("www.Hiast.edu.sy")
                        .build())
                .setPublicationYear(2018)
                .setCategory(BookCategory.TECHNOLOGY)
                .setStatus(BookStatus.AVAILABLE)
                .setPrice(45.99)
                .addTags("Java Programming Best Seller")
                .build();

        BookResponse addResponse = stub.addBook(newBook);
        logger.info("Add Book Response: " + addResponse.getMessage());

        BookRequest getRequest = BookRequest.newBuilder()
                .setIsbn("123456789")
                .build();

        Book retrievedBook = stub.getBook(getRequest);
        logger.info("Retrieved Book: " + retrievedBook.getTitle());

        Book updatedBook = retrievedBook.toBuilder()
                .setPrice(39.99)
                .build();

        BookResponse updateResponse = stub.updateBook(updatedBook);
        logger.info("Update Book Response: " + updateResponse.getMessage());

        BookRequest deleteRequest = BookRequest.newBuilder()
                .setIsbn("123456789")
                .build();

        BookResponse deleteResponse = stub.deleteBook(deleteRequest);
        logger.info("Delete Book Response: " + deleteResponse.getMessage());

        channel.shutdown();
    }
}
