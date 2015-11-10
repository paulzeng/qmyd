package com.ak.qmyd.bean;

import java.io.Serializable;
import java.util.List;

public class DeliverData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	List<OrderList> mlist;
	
	public List<OrderList> getMlist() {
		return mlist;
	}
	public void setMlist(List<OrderList> mlist) {
		this.mlist = mlist;
	}
}
