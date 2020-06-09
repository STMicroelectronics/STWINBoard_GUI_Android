/*
 * Copyright (c) 2020  STMicroelectronics – All rights reserved
 * The STMicroelectronics corporate logo is a trademark of STMicroelectronics
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * - Neither the name nor trademarks of STMicroelectronics International N.V. nor any other
 * STMicroelectronics company nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * - All of the icons, pictures, logos and other images that are provided with the source code
 * in a directory whose title begins with st_images may only be used for internal purposes and
 * shall not be redistributed to any third party or modified in any way.
 *
 * - Any redistributions in binary form shall not include the capability to display any of the
 * icons, pictures, logos and other images that are provided with the source code in a directory
 * whose title begins with st_images.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */

package com.st.STWINBoard_Gui.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.st.BlueSTSDK.Features.highSpeedDataLog.communication.DeviceModel.Sensor
import com.st.BlueSTSDK.Features.highSpeedDataLog.communication.DeviceModel.SensorType
import com.st.BlueSTSDK.Features.highSpeedDataLog.communication.DeviceModel.SubSensorDescriptor
import com.st.BlueSTSDK.Features.highSpeedDataLog.communication.DeviceModel.SubSensorStatus
import com.st.clab.stwin.gui.R

internal class SubSensorPreviewViewAdapter(
        private val sensor: Sensor,
        private val onSubSensorEnableStatusChange: OnSubSensorEnableStatusChange
        ) : RecyclerView.Adapter<SubSensorPreviewViewAdapter.ViewHolder>() {

    //SubParam List
    private val mSubSensorList: List<SubSensorDescriptor> = sensor.sensorDescriptor.subSensorDescriptors
    private val mSubStatusList: List<SubSensorStatus> = sensor.sensorStatus.subSensorStatusList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_sub_sensor_preview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subSensorDescriptor = mSubSensorList[position]
        val subSensorStatus = mSubStatusList[position]

        holder.bind(subSensorDescriptor,subSensorStatus)
    }

    override fun getItemCount(): Int {
        return mSubSensorList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mIcon: ImageView = itemView.findViewById(R.id.subSensor_icon)
        private val mEnabledSwitch:Switch = itemView.findViewById(R.id.subSensor_enable)

        private var mSubSensor:SubSensorDescriptor? = null
        private var mSubSensorStatus:SubSensorStatus? = null

        private val onCheckedChangeListener  = object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                val subSensor = mSubSensor ?: return
                onSubSensorEnableStatusChange(sensor,subSensor,isChecked)
            }
        }

        fun bind(subSensor:SubSensorDescriptor, status: SubSensorStatus){
            mSubSensor = subSensor
            mSubSensorStatus = status
            setSensorData(subSensor.sensorType)
            setEnableState(status.isActive)
        }

        private fun setEnableState(newState:Boolean){
            mEnabledSwitch.setOnCheckedChangeListener(null)
            mEnabledSwitch.isChecked = newState
            mEnabledSwitch.setOnCheckedChangeListener(onCheckedChangeListener)
        }

        private fun setSensorData(sensorType: SensorType) {
            mIcon.setImageResource(sensorType.imageResource)
        }
    }
}