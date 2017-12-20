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
 * 作者：zhourf on 2017/11/10
 * 邮箱：zhourf@glodon.com
 */
public interface OnDateChangedListener {
	void onChanged(DateView wheel, int oldValue, int newValue);
}
