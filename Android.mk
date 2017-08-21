#
# Copyright (C) 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
ifneq ($(TARGET_BUILD_PDK), true)

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_PACKAGE_NAME := CarDialerApp

LOCAL_OVERRIDES_PACKAGES := Dialer

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES += android-support-v4

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_PRIVILEGED_MODULE := true

LOCAL_DEX_PREOPT := false

include packages/apps/Car/libs/car-stream-ui-lib/car-stream-ui-lib.mk
include packages/apps/Car/libs/car-apps-common/car-apps-common.mk

include $(BUILD_PACKAGE)

endif