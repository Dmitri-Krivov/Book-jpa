package telran.java2022.book.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "isbn")
@Entity
public class Book implements Serializable {

	private static final long serialVersionUID = 5412472939106770533L;
	@Id
	String isbn;
	@Column(name = "TITLE") // можно указать имя при подключении к существующей таблице
	String title;
	@ManyToMany
	@JoinTable( name = "BOOK_AUTHORS", 
				joinColumns = @JoinColumn(name = "BOOK_ISBN"), 
				inverseJoinColumns = @JoinColumn(name = "AUTHORS_NAME")
	)
	// тут тоже указываем к какой и как(это для Bidirectional таблиц )
	// В unidirectional таблицах связей между таблицами меньше(через book в данном
	// примере) соответственно данные необходимо доставать через посредника

	Set<Author> authors;
	@ManyToOne
	Publisher publisher;

}
