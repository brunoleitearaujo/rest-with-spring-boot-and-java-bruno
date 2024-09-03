package br.com.bruno.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bruno.data.vo.v1.BookVO;
import br.com.bruno.exceptions.RequiredObjectsIsNullException;
import br.com.bruno.model.Book;
import br.com.bruno.repositories.BookRepository;
import br.com.bruno.services.BookServices;
import br.com.bruno.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	@InjectMocks
	private BookServices service;

	@Mock
	private BookRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAll() {
		List<Book> list = input.mockEntityList();

		when(repository.findAll()).thenReturn(list);

		var book = service.findAll();

		assertNotNull(book);
		assertEquals(14, book.size());

		book.forEach(b -> {
			Long key = b.getKey();
			assertNotNull(b);
			assertNotNull(key);
			assertNotNull(b.getLinks());
			assertNotNull(b.getLaunchDate());

			assertTrue(b.toString().contains("[</api/book/v1/" + key + ">;rel=\"self\"]"));

			assertEquals("Author Test" + key, b.getAuthor());
			assertEquals(25D, b.getPrice());
			assertEquals("Title Test" + key, b.getTitle());
		});
	}

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		var result = service.findById(1L);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getLaunchDate());

		assertTrue(result.toString().contains("[</api/book/v1/1>;rel=\"self\"]"));

		assertEquals("Author Test1", result.getAuthor());
		assertEquals(25D, result.getPrice());
		assertEquals("Title Test1", result.getTitle());
	}

	@Test
	void testCreate() {
		Book entity = input.mockEntity(1);

		Book persisted = input.mockEntity(1);

		BookVO vo = input.mockVO(1);

		when(repository.save(entity)).thenReturn(persisted);

		var result = service.create(vo);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getLaunchDate());

		assertTrue(result.toString().contains("[</api/book/v1/1>;rel=\"self\"]"));

		assertEquals("Author Test1", result.getAuthor());
		assertEquals(25D, result.getPrice());
		assertEquals("Title Test1", result.getTitle());
	}

	@Test
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(
				RequiredObjectsIsNullException.class, () -> service.create(null));

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(expectedMessage.contains(actualMessage));
	}

	@Test
	void testUpdate() {
		Book entity = input.mockEntity(1);

		Book persisted = input.mockEntity(1);

		BookVO vo = input.mockVO(1);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);

		var result = service.update(vo);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getLaunchDate());

		assertTrue(result.toString().contains("[</api/book/v1/1>;rel=\"self\"]"));

		assertEquals("Author Test1", result.getAuthor());
		assertEquals(25D, result.getPrice());
		assertEquals("Title Test1", result.getTitle());
	}

	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(
				RequiredObjectsIsNullException.class, () -> service.update(null));

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(expectedMessage.contains(actualMessage));
	}

	@Test
	void testDelete() {
		Book entity = input.mockEntity(1);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		repository.findById(1L);
	}
}
