/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.*;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Parcel;

import com.android.internal.telephony.RILConstants;

/**
 * Custom RIL for Optimus G with mako RIL stack and stock ATT JB modem
 * 
 * @author dvhexer
 */
public class GeebRIL extends RIL implements CommandsInterface {

	protected final static String MARITIME = "Maritime";
	protected final static String ATT = "AT&T";

	// probably, only 310410 is needed, but but just in check other att network
	// codes
	protected final static String[] ATT_MCCMNCs = { "310410", "310070",
			"310311", "310380", "310490", "310560", "310680", "310990" };

	public GeebRIL(Context context, int networkMode, int cdmaSubscription) {
		super(context, networkMode, cdmaSubscription);
	}

	protected RILRequest findRequestInList(int serial) {
		synchronized (mRequestList) {
			for (int i = 0, s = mRequestList.size(); i < s; i++) {
				RILRequest rr = mRequestList.get(i);

				if (rr.mSerial == serial) {
					return rr;
				}
			}
		}

		return null;
	}

	@Override
	protected void processSolicited(Parcel p) {
		int serial, error;
		boolean found = false;

		int dataPos = p.dataPosition();

		serial = p.readInt();
		error = p.readInt();

		RILRequest rr;

		// only pick request from list
		rr = findRequestInList(serial);

		if ((error == 0 || p.dataAvail() > 0) && rr != null
				&& rr.mRequest == RIL_REQUEST_OPERATOR) {
			// remove request from list
			rr = findAndRemoveRequestFromList(serial);

			String[] ret = p.readStringArray();

			if (RILJ_LOGD) {
				riljLog(rr.serialString() + "< " + requestToString(rr.mRequest)
						+ " " + retToString(rr.mRequest, ret));
			}
			
			if (ret.length >= 3 && MARITIME.equals(ret[0])) {
				boolean att = false;
				for(int i=0; !att && i<ATT_MCCMNCs.length; i++) {
					if(ATT_MCCMNCs[i].equals(ret[2])) {
						att = true;
					}
				}
				
				if(att) {
					if (RILJ_LOGD) {
						riljLog("Overriding " + MARITIME + " with " + ATT);
					}

					ret[0] = ATT;
				}
			}

			if (rr.mResult != null) {
				AsyncResult.forMessage(rr.mResult, ret, null);
				rr.mResult.sendToTarget();
			}

			rr.release();
		} else {
			p.setDataPosition(dataPos);

			super.processSolicited(p);
		}
	}
}
