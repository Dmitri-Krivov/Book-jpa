package telran.java2022.book.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.book.dao.AuthorRepository;
import telran.java2022.book.dao.BookRepository;
import telran.java2022.book.dao.PublisherRepository;
import telran.java2022.book.dto.AuthorDto;
import telran.java2022.book.dto.BookDto;
import telran.java2022.book.dto.exceptions.EntityNotFoundException;
import telran.java2022.book.model.Author;
import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		//Publisher
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));
		//Author
		Set<Author> authors = bookDto.getAuthors().stream()
									.map(a -> authorRepository.findById(a.getName())
											.orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
									.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	@Transactional
	public BookDto findBookByIsbn(String isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBook(String isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        bookRepository.delete(book);
        return bookDto;
	}

	@Override
	public BookDto updateBook(String isbn, String title) {
        Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        book.setTitle(title);
        bookRepository.save(book);
        return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
        Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		//???
        return bookRepository.findAllBooksByAuthors(author)
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
	       Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(EntityNotFoundException::new);
	       return bookRepository.findBooksByPublisher(publisher)
	                .map(book -> modelMapper.map(book, BookDto.class))
	                .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<AuthorDto> findBookAuthors(String isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        return book.getAuthors().stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<String> findPublishersByAuthor(String authorName) {
		return null;
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
//		if(bookRepository.findBooksByAuthor(authorName)){
//			throw new EntityNotFoundException();
//		}
        authorRepository.delete(author);
		return null;
	}

}
