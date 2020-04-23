package org.newtonproject.newtoncore.android.data.entity.common;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class Wallet implements Parcelable {
    public final String address;
    public String type;

	public Wallet(String address) {
		this.address = address;
	}

	public Wallet(String address, String type) {
		this.address = address;
		this.type = type;
	}

	private Wallet(Parcel in) {
		address = in.readString();
		type = in.readString();
	}

	public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
		@Override
		public Wallet createFromParcel(Parcel in) {
			return new Wallet(in);
		}

		@Override
		public Wallet[] newArray(int size) {
			return new Wallet[size];
		}
	};

	public boolean sameAddress(String address) {
		return this.address.equals(address);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(address);
		parcel.writeString(type);
	}
}
