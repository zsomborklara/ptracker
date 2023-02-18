package hu.zsomboro.ptracker.client;

import hu.zsomboro.ptracker.core.Price;

public interface PriceClient {

  Price getTodayPrice(String identifier);

}
