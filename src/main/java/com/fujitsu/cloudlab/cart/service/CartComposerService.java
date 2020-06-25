package com.fujitsu.cloudlab.cart.service;

import com.fujitsu.cloudlab.cart.model.Cart;
import com.fujitsu.cloudlab.cart.model.CartSearchCriteria;
import com.fujitsu.cloudlab.cart.repository.CartDataRepository;
import com.fujitsu.cloudlab.cart.util.CartWriterUtil;
import com.fujitsu.cloudlab.commons.exception.ApiException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CartWriterService {

  @Value("${cart.exp.duration}")
  private long expTime;

  @Autowired CartDataRepository cartDataRepository;
  @Autowired CartWriterUtil cartWriterUtil;

  public Cart createCart(CartSearchCriteria searchCriteria, String transactionId)
      throws ApiException {

    if (searchCriteria.getCartId() != null) {
      searchCriteria.setCartId(searchCriteria.getCartId());
    } else {
      searchCriteria.setCartId(UUID.randomUUID());
    }

    String offerId = "";

    if (cartDataRepository.getOffers(searchCriteria) != null) {
      offerId = cartDataRepository.getOffers(searchCriteria) + "," + searchCriteria.getOfferId();
      searchCriteria.setOfferId(offerId);
    }

    cartDataRepository.save(searchCriteria);
    Cart cart = cartWriterUtil.addOffersToCart(searchCriteria, transactionId);

    return cart;
  }
}
