package pl.dawid.moviebase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dawid.moviebase.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
        select distinct m from Movie m left join m.genres g
        where (:q is null or :q = '' or lower(m.title) like lower(concat('%', :q, '%'))
            or lower(m.description) like lower(concat('%', :q, '%'))
            or lower(g.namePl) like lower(concat('%', :q, '%'))
            or lower(g.nameEn) like lower(concat('%', :q, '%')))
        """)
    Page<Movie> search(@Param("q") String q, Pageable pageable);
}
