package com.zosh.repository;

import com.zosh.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository  extends JpaRepository<Cart, Long> {

    // The named parameter in the query is ':userId'
    @Query("SELECT c from Cart c Where c.user.id=:userId")
    // The @Param name must match the query parameter exactly.
    // The old name was "User-id" which caused the error.
    public Cart findByUserId(@Param("userId") Long userId);


}