package com.zosh.repository;

import com.zosh.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    @Query("SELECT r FROM Rating r Where r.product.id=:productId")


    public List<Rating> getAllProductsRating(@Param("product_id")Long productId);
}
