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
                    select m.*
                    from movie m
                    left join movie_genres mg on mg.movie_id = m.id
                    left join genre g on g.id = mg.genre_id
                    left join rating r on r.movie_id = m.id
                    where (:q is null or :q = ''
                        or lower(m.title) like lower(concat('%', :q, '%'))
                        or lower(m.original_title) like lower(concat('%', :q, '%'))
                        or lower(m.description) like lower(concat('%', :q, '%'))
                        or lower(g.name_pl) like lower(concat('%', :q, '%'))
                        or lower(g.name_en) like lower(concat('%', :q, '%')))
                    and (:genreId is null or g.id = :genreId)
                    and (:yearFrom is null or m.release_year >= :yearFrom)
                    and (:yearTo is null or m.release_year <= :yearTo)
                    group by m.id
                    order by coalesce(avg(r.rating_value), 0) desc, m.title asc
                    """,
            countQuery = """
                    select count(distinct m.id)
                    from movie m
                    left join movie_genres mg on mg.movie_id = m.id
                    left join genre g on g.id = mg.genre_id
                    where (:q is null or :q = ''
                        or lower(m.title) like lower(concat('%', :q, '%'))
                        or lower(m.original_title) like lower(concat('%', :q, '%'))
                        or lower(m.description) like lower(concat('%', :q, '%'))
                        or lower(g.name_pl) like lower(concat('%', :q, '%'))
                        or lower(g.name_en) like lower(concat('%', :q, '%')))
                    and (:genreId is null or g.id = :genreId)
                    and (:yearFrom is null or m.release_year >= :yearFrom)
                    and (:yearTo is null or m.release_year <= :yearTo)
                    """,
            nativeQuery = true)
    Page<Movie> searchOrderByRating(
            @Param("q") String q,
            @Param("genreId") Long genreId,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            Pageable pageable);

    List<Movie> findTop4ByOrderByCreatedAtDesc();
}
