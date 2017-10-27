/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.glodon.bim.customview.datepicker;

/**
 * Description:当日期改变时的回调接口
 * Created by 周瑞峰
 * Job number:136597
 * Phone:15001340978
 * Email:zhouruifeng@syswin.com
 * Person in charge:周瑞峰
 * Leader:周瑞峰
 */
public interface TNBOnDateChangedListener {
	void onChanged(TNBDateView wheel, int oldValue, int newValue);
}
