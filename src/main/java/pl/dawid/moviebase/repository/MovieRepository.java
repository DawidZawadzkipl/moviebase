package pl.dawid.moviebase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dawid.moviebase.model.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
        select distinct m from Movie m left join m.genres g
        where (:q is null or :q = '' or lower(m.title) like lower(concat('%', :q, '%'))
            or lower(m.description) like lower(concat('%', :q, '%'))
            or lower(g.namePl) like lower(concat('%', :q, '%'))
            or lower(g.nameEn) like lower(concat('%', :q, '%')))
        """)
    Page<Movie> search(@Param("q") String q, Pageable pageable);

    @Query(
            value = """
                    select distinct m from Movie m left join m.genres g
                    where (:q is null or :q = ''
                        or lower(m.title) like lower(concat('%', :q, '%'))
                        or lower(m.originalTitle) like lower(concat('%', :q, '%'))
                        or lower(m.description) like lower(concat('%', :q, '%'))
                        or lower(g.namePl) like lower(concat('%', :q, '%'))
                        or lower(g.nameEn) like lower(concat('%', :q, '%')))
                    and (:genreId is null or g.id = :genreId)
                    and (:yearFrom is null or m.releaseYear >= :yearFrom)
                    and (:yearTo is null or m.releaseYear <= :yearTo)
                    """,
            countQuery = """
                    select count(distinct m) from Movie m left join m.genres g
                    where (:q is null or :q = ''
                        or lower(m.title) like lower(concat('%', :q, '%'))
                        or lower(m.originalTitle) like lower(concat('%', :q, '%'))
                        or lower(m.description) like lower(concat('%', :q, '%'))
                        or lower(g.namePl) like lower(concat('%', :q, '%'))
                        or lower(g.nameEn) like lower(concat('%', :q, '%')))
                    and (:genreId is null or g.id = :genreId)
                    and (:yearFrom is null or m.releaseYear >= :yearFrom)
                    and (:yearTo is null or m.releaseYear <= :yearTo)
                    """)
    Page<Movie> search(
            @Param("q") String q,
            @Param("genreId") Long genreId,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            Pageable pageable);

    @Query(
            value = """
                    select distinct m from Movie m left join m.genres g
                    where (:q is null or :q = ''
                        or lower(m.title) like lower(concat('%', :q, '%'))
                        or lower(m.originalTitle) like lower(concat('%', :q, '%'))
                        or lower(m.description) like lower(concat('%', :q, '%'))
                        or lower(g.namePl) like lower(concat('%', :q, '%'))
                        or lower(g.nameEn) like lower(concat('%', :q, '%')))
                    and (:genreId is null or g.id = :genreId)
                    and (:yearFrom is null or m.releaseYear >= :yearFrom)
                    and (:yearTo is null or m.releaseYear <= :yearTo)
                    order by (select coalesce(avg(r.ratingValue), 0) from Rating r where r.movie = m) desc,
                        m.title asc
                    """,
            countQuery = """
                    select count(distinct m) from Movie m left join m.genres g
                    where (:q is null or :q = ''
                        or lower(m.title) like lower(concat('%', :q, '%'))
                        or lower(m.originalTitle) like lower(concat('%', :q, '%'))
                        or lower(m.description) like lower(concat('%', :q, '%'))
                        or lower(g.namePl) like lower(concat('%', :q, '%'))
                        or lower(g.nameEn) like lower(concat('%', :q, '%')))
                    and (:genreId is null or g.id = :genreId)
                    and (:yearFrom is null or m.releaseYear >= :yearFrom)
                    and (:yearTo is null or m.releaseYear <= :yearTo)
                    """)
    Page<Movie> searchOrderByRating(
            @Param("q") String q,
            @Param("genreId") Long genreId,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            Pageable pageable);

    List<Movie> findTop4ByOrderByCreatedAtDesc();
}
