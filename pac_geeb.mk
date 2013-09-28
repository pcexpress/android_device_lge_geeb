# Copyright (C) 2013 The CyanogenMod Project
# Copyright (C) 2013 The LiquidSmooth Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Target Release Name
PRODUCT_RELEASE_NAME := pac_geeb

# Inherit device configuration
$(call inherit-product, device/lge/geeb/full_geeb.mk)

# Standard
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# GSM RIL Parts
$(call inherit-product, vendor/cm/config/gsm.mk)

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := geeb
PRODUCT_NAME := pac_geeb
PRODUCT_BRAND := LGE
PRODUCT_MODEL := Optimus G
PRODUCT_MANUFACTURER := LGE
PRODUCT_PROPERTY_OVERRIDES += ro.buildzipid=pac.geeb.$(shell date +%m%d%y).$(shell date +%H%M%S)

PRODUCT_BUILD_PROP_OVERRIDES += \
    BUILD_NUMBER=737497 \
    PRODUCT_NAME=occam \
    TARGET_DEVICE=occam \
    TARGET_BUILD_TYPE=user \
    BUILD_VERSION_TAGS=release-keys \
    BUILD_FINGERPRINT="google/occam/geeb:4.3/JWR66V/737497:user/release-keys" \
    PRIVATE_BUILD_DESC="occam-user 4.3 JWR66V 737497 release-keys"

# Enable Torch
PRODUCT_PACKAGES += Torch
