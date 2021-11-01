package com.skithub.resultdear.model.response;

import com.google.gson.annotations.SerializedName;

public class BannerResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("actionType")
	private int actionType;

	@SerializedName("visible")
	private boolean visible;

	@SerializedName("activity")
	private String activity;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("actionUrl")
	private String actionUrl;

	@SerializedName("id")
	private String id;

	@SerializedName("error")
	private boolean error;

	public String getMsg(){
		return msg;
	}

	public int getActionType(){
		return actionType;
	}

	public boolean isVisible(){
		return visible;
	}

	public String getActivity(){
		return activity;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public String getActionUrl(){
		return actionUrl;
	}

	public String getId(){
		return id;
	}

	public boolean isError(){
		return error;
	}
}