package telran.java2022.book.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "name")
@Entity
public class Author implements Serializable {

	private static final long serialVersionUID = -2534552141216260206L;
	@Id
	String name;
	LocalDate birthDate;
	@ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
	// каскад что-бы удалить все связанные с авторам книги книги
	Set<Book> books;

	public Author(String name, LocalDate birthDate) {
		this.name = name;
		this.birthDate = birthDate;
	}

}
