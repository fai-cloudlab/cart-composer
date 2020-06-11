package com.fujitsu.cloudlab.cart.repository;

import com.fujitsu.cloudlab.cart.model.CartSearchCriteria;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class CartDataRepository {

  @Value("${spring.cache.redis.time-to-live}")
  private long expTime;

  private ValueOperations<String, String> valueOperations;

  @Autowired
  CartDataRepository(RedisTemplate<String, String> redisTemplate) {
    this.valueOperations = redisTemplate.opsForValue();
  }

  public void save(CartSearchCriteria searchCriteria) {

    valueOperations.set(
        searchCriteria.getCartId().toString(),
        searchCriteria.getOfferId().toString(),
        Long.valueOf(searchCriteria.getOfferExpirationDate()),
        TimeUnit.MILLISECONDS);
  }

  public String getOffers(CartSearchCriteria searchCriteria) {

    String cartId = searchCriteria.getCartId().toString();

    if (valueOperations.get(cartId) != null) {
      return valueOperations.get(cartId);
    }

    return null;
  }
}
