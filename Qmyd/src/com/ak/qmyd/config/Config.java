package com.ak.qmyd.config;

public class Config {

	/**
	 * 图片上传有关的what
	 */
	public static final int RESULT_CODE = 1;
	public static final int DATA_NULL = 2;
	public static final int SHOW_PHOTO = 3;
	public static final int UP_PHOTO_SUCCES = 4;
	public static final int UN_PHOTO_TYPE = 5;
	public static final int UP_PHOTO_FAIL = 6;
	public static final int UP_PHOTO_LOADING = 7;
	public static final int GET_USER_INFO_SUCCESS = 8;
	public static final int GET_USER_INFO_FAIL = 9;

	// public static final String BASE_URL = "http://192.168.102.82:7171/qmyd";
	public static final String BASE_URL = "http://aikong.vicp.net:7171/qmyd";
	public static final String TRAIN_LIST_URL = BASE_URL
			+ "/api/rest/train/List";
	public static final String TRAIN_DETAIL_URL = BASE_URL
			+ "/api/rest/train/Details";
	public static final String API_URL = BASE_URL + "/api/rest/";
	public static final String SYNCHRONOUS_TRAIN_URL = BASE_URL
			+ "/api/rest/train/synchronization";
	public static final String CHAPTER_LIST_URL = BASE_URL
			+ "/api/rest/train/section/List";
	public static final String TRAIN_HISTORY_URL = BASE_URL
			+ "/api/rest/train/History";
	public static final String TRAIN_HISTORY_DETAIL_URL = BASE_URL
			+ "/api/rest/train/History/Detail";
	public static final String TRAIN_END_URL = BASE_URL + "/api/rest/train/End";
	public static final String VENUES_APPOINTMENT_URL = BASE_URL
			+ "/api/rest/venues/ListVenues";
	public static final String TRAIN_SKILL_URL = BASE_URL
			+ "/api/rest/train/trainSkill/List";
	public static final String TRAIN_SKILL_DETAIL_URL = BASE_URL
			+ "/api/rest/train/trainSkill/Detail";
	public static final String BUSINESS_CIRCLE_URL = BASE_URL
			+ "/api/rest/venues/ListArea";
	public static final String VENUES_DETAILS_URL = BASE_URL
			+ "/api/rest/venues/ObjectVenues";
	public static final String VENUES_BOOK_URL = BASE_URL
			+ "/api/rest/venues/ListSupport";
	public static final String VENUES_BOOK_POST_URL = BASE_URL
			+ "/api/rest/venues/addVenues";
	public static final String VENUES_PAY_POST_URL = BASE_URL
			+ "/api/rest/venues/confirmVenues";
	public static final String VENUES_EVALUATION_URL = BASE_URL
			+ "/api/rest/venues/ListAppraise";
	public static final String ORDER_BOOKING_POST_URL = BASE_URL
			+ "/api/rest/venues/cancleVenues";

	public static final String MY_ORDER_BOOKING = BASE_URL
			+ "/api/rest/venues/ListOrder";
	public static final String MY_ORDER_CONSUMPTION = BASE_URL
			+ "/api/rest/venues/ListOrder";
	public static final String MY_ORDER_FINISHED = BASE_URL
			+ "/api/rest/venues/ListOrder";
	public static final String MY_ORDER_EXPIRED = BASE_URL
			+ "/api/rest/venues/ListOrder";

	public static final String USER_LOGIN_URL = BASE_URL
			+ "/api/rest/admin/login";
	public static final String USER_LOGINOUT_URL = BASE_URL
			+ "/api/rest/admin/loginout";
	public static final String USER_REGISTER_URL = BASE_URL
			+ "/api/rest/admin/identifyingCode";
	public static final String USER_FORGET_PWD_URL = BASE_URL
			+ "/api/rest/admin/getUpdatePswCode";
	public static final String USER_FORGET_PWD_COMPLETE_URL = BASE_URL
			+ "/api/rest/admin/commitUpdatePswCode";
	public static final String USER_REGISTER_DETAIL_URL = BASE_URL
			+ "/api/rest/admin/commitIdentifying";
	public static final String SEND_MY_TYPE_URL = BASE_URL
			+ "/api/rest/admin/home/type/add";
	public static final String MY_TRAIN_URL = BASE_URL
			+ "/api/rest/train/History/Type";

	public static final String MY_MESSAGE_URL = BASE_URL
			+ "/api/rest/admin/push";
	public static final String MYINFO_URL = BASE_URL + "/api/rest/admin/myInfo";
	public static final String CIRCLE_NOTE_DETAIL_URL = BASE_URL
			+ "/api/rest/circle/queryCircleDetailInfo";
	public static final String CIRCLE_REPLAY_URL = BASE_URL
			+ "/api/rest/circle/replayCircle";
	public static final String CIRCLE_LIST_URL = BASE_URL
			+ "/api/rest/circle/queryMyCircleList";
	public static final String CIRCLE_DETAIL_URL = BASE_URL
			+ "/api/rest/circle/queryOneCircleInfo";
	public static final String CIRCLE_MEMBER_LIST_URL = BASE_URL
			+ "/api/rest/circle/queryCirclePersonList";
	public static final String CIRCLE_GOOD_NOTE_URL = BASE_URL
			+ "/api/rest/circle/queryHotCircleDetailInfo";
	public static final String JOIN_OR_CANCEL_JOIN_CIRCLE_URL = BASE_URL
			+ "/api/rest/circle/addOrDelRelationCircle";
	public static final String FOCUS_OR_CANCEL_FOCUS_URL = BASE_URL
			+ "/api/rest/circle/addOrDelRelationPerson";
	public static final String DELETE_CIRCLE_REPLAY = BASE_URL
			+ "/api/rest/circle/delCircleCommentInfo";
	public static final String CIRCLE_NOTE_ZAN = BASE_URL
			+ "/api/rest/circle/praiseCircleOperation";
	public static final String DELETE_CIRCLE_NOTE_URL = BASE_URL
			+ "/api/rest/circle/deleteCircleInfo";
	public static final String POST_NOTE_URL = BASE_URL
			+ "/api/rest/circle/addCircleInfo";
	public static final String QUERY_SPORTS_TYPE_URL = BASE_URL
			+ "/api/rest/circle/queryAllSportsList";
	public static final String CREATE_CIRCLE_URL = BASE_URL
			+ "/api/rest/circle/addCircleObject";
	public static final String CHANGE_CIRCLE_URL = BASE_URL
			+ "/api/rest/circle/updateCircleObject";
	public static final String HOT_CIRCLE_LIST_URL = BASE_URL
			+ "/api/rest/circle/queryHotCircleList";
	public static final String VENUE_ACTIVITY = BASE_URL
			+ "/api/rest/venues/queryVenuesEventList";
}
