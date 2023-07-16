package com.garkavaya.Project2Boot.services;

import com.garkavaya.Project2Boot.models.Book;
import com.garkavaya.Project2Boot.models.Person;
import com.garkavaya.Project2Boot.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setBookId(id);
        booksRepository.save(updatedBook); //метод увидит по id, что такая книга уже есть и просто обновит её
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void appoint(int id, Person person) {
        Book book = booksRepository.findById(id).orElse(null);
        if (person == null) {
            book.setOwner(null);
            book.setTakenAt(null);
        } else {
            book.setOwner(person);
            book.setTakenAt(new Date());
        }
    }

    public List<Book> findByNameStartingWith(String prefix) {
        return booksRepository.findByNameStartingWith(prefix);
    }
}
