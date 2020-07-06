package com.fujitsu.cloudlab.cart.util;

import com.fujitsu.cloudlab.cart.model.Cart;
import com.fujitsu.cloudlab.cart.model.CartSearchCriteria;
import com.fujitsu.cloudlab.cart.model.Customer;
import com.fujitsu.cloudlab.cart.model.Offer;
import com.fujitsu.cloudlab.cart.model.Price;
import com.fujitsu.cloudlab.cart.model.Product;
import com.fujitsu.cloudlab.commons.constants.AppConstants;
import com.fujitsu.cloudlab.commons.exception.ApiException;
import com.fujitsu.cloudlab.commons.util.ResponseUtil;
import com.fujitsu.cloudlab.offer.json.model.OffersList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class CartComposerUtil {

  @Value("${cart.exp.duration}")
  private long expTime;

  @Autowired RestTemplate restTemplate;

  @Value("${offer.retriever.url}")
  String offerRetrieverUrl;

  public Cart addOffersToCart(CartSearchCriteria request, String transactionId) {

    List<String> offerIds = Arrays.asList(request.getOfferId().split(","));

    Cart cart = new Cart();
    long currDate = System.currentTimeMillis();

    cart.setCartExpirationTime(new Date(currDate + expTime).toString());
    cart.setCartId(request.getCartId().toString());

    OffersList offersList = null;
    List<Offer> cartOffers = new ArrayList<>();
    Double cartTotalAmount = 0.0;
    for (String offerId : offerIds) {

      try {
        offersList =
            ResponseUtil.process(
                OffersList.class,
                restTemplate.exchange(
                    offerRetrieverUrl + offerId,
                    HttpMethod.GET,
                    new HttpEntity<String>(createHeaders(transactionId)),
                    String.class),
                "OFFER",
                "Offer Search. ");
      } catch (RestClientException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ApiException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (Exception ex) {
    	  ex.printStackTrace();
      }

      Offer cartOffer;
      for (com.fujitsu.cloudlab.offer.json.model.Offer offer : offersList.getOffers()) {

        cartOffer = new Offer();

        cartOffer.setOfferCreationDate(offer.getOfferCreationDate());
        cartOffer.setOfferExpirationDate(offer.getOfferExpirationDate());
        cartOffer.setOfferId(offer.getOfferId());
        cartOffer.setOfferType(offer.getOfferType());
        Customer customer = new Customer();
        customer.setCustomerId(offer.getCustomer().getCustomerId());
        customer.setEmailAdr(offer.getCustomer().getEmailAddress());
        customer.setFirstName(offer.getCustomer().getFirstName());
        customer.setLastName(offer.getCustomer().getLastName());
        cartOffer.setCustomers(customer);

        // cartOffer.setPaymentTimeLimitUtcTs(offer.get);

        Price price = new Price();
        price.setCurrency(offer.getOfferPrice().getCurrency());
        price.setValue(offer.getOfferPrice().getValue());
        cartTotalAmount = cartTotalAmount + offer.getOfferPrice().getValue();
        cartOffer.setOfferPrice(price);

        Product product = new Product();
        product.setProductCategory(offer.getProduct().getProductCategory());
        product.setProductCode(offer.getProduct().getProductCode());
        product.setProductType(offer.getProduct().getProductType());

        cartOffer.setProduct(product);

        cartOffers.add(cartOffer);
      }
    }

    cart.setCartTotalAmount(cartTotalAmount);
    cart.setOffers(cartOffers);

    return cart;
  }

  private HttpHeaders createHeaders(String transactionId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(AppConstants.TRANSACTION_ID, transactionId);

    return headers;
  }
}
