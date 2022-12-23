package com.fastwok.crawler.repository;

        import com.fastwok.crawler.entities.CustomerNhanh;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;

        import java.util.List;

public interface CustomerNhanhRepository extends JpaRepository<CustomerNhanh, Integer> {
    @Query(value = "SELECT * FROM nhanhCustomer order by id limit ?1 offset ?2",nativeQuery=true)
    List<CustomerNhanh> getCustomer(Integer limit, Integer offset);
}
