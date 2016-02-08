LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, java)

LOCAL_PACKAGE_NAME := MonkeyMoney
LOCAL_JAVA_LIBRARIES += vivo-framework
LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED := disabled 
include $(BUILD_PACKAGE)

##################################################
include $(CLEAR_VARS)

	
include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
include $(BUILD_MULTI_PREBUILT)

