package com.RecomendationService.constants;

public class RecomendationConstant {
	
	public static final String ROOT_PATH = "/api/v1";
	public static final String SEARCH_PATH = "/search";
	public static final String SEARCH_LOWTOHIGH_PATH = "/searchLowToHighByPrice";
	public static final String SEARCH_HIGHTOLOW_PATH = "/searchHighToLowByPrice";
	public static final String SEARCH_BYNAME_PATH = "/venue/{venueName}";
	public static final String SEARCH_BYPRICEANDCAPACITY_PATH = "venue/{venuePrice}/{venueCapacity}";
	public static final String SEARCH_BYGREATER_PATH = "/greaterThanPrice";
	public static final String SEARCH_BYLESSER_PATH  = "/lessThanPrice";
	public static final String SEARCH_BYINBETWEEN_PATH  = "/rangePrice";
	public static final String SEARCH_BY_USER_CITY  = "/userCity";
	public static final String VENUE_INFO_MSG = "Please enter a valid name of venue";
	public static final String VENUE_NOTFOUND_MSG = "There is no Venue found as per your search";
	public static final String VENUE_NOTFOUND_ASPER_PRICEANDCAPACITY_MSG = "Venue not found with this price and capacity";
	public static final String VENUE_NOTFOUND_GREATERTHAN_MSG = "There is no venue present above this price";
	public static final String VENUE_NOTFOUND_LESSERTHAN_MSG = "There is no venue present below this price";
	public static final String VENUE_NOTFOUND_INBETWEEN_MSG = "There is no venue present in between this price";
	public static final String SEARCH_CAPACITY_LOWTOHIGH_PATH = "/searchLowToHighByCapacity";
	public static final String SEARCH_CAPACITY_HIGHTOLOW_PATH = "/searchHighToLowByCapacity";
	public static final String SEARCH_CAPACITY_BYGREATER_PATH = "/greaterThanCapacity";
	public static final String SEARCH_CAPACITY_BYLESSER_PATH  = "/lessThanCapacity";
	public static final String SEARCH_CAPACITY_BYINBETWEEN_PATH  = "/rangeCapacity";
	public static final String VENUE_NOTFOUND_GREATERTHAN_CAPACITY_MSG = "There is no venue present above this capacity";
	public static final String VENUE_NOTFOUND_LESSERTHAN_CAPACITY_MSG = "There is no venue present below this capacity";
	public static final String VENUE_NOTFOUND_INBETWEEN_CAPACITY_MSG = "There is no venue present in between this capacity";

}
