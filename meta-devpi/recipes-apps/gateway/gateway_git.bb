SUMMARY = "MPU Gateway Project"
DESCRIPTION = "C++17 Gateway software with modular managers for protocol translation, data aggregation, cloud connectivity, OTA, and device management"
LICENSE = "CLOSED"

# Fetch from GitHub repo
SRC_URI = "git://github.com/ShayanEram/MPU_Gateway.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake systemd

# Dependencies
DEPENDS += "libgpiod"

# Disable tests in production
EXTRA_OECMAKE = "-DBUILD_TESTS=OFF"

# Install binary
do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 Gateway ${D}${bindir}

    # Install systemd unit
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/mpu-gateway.service ${D}${systemd_system_unitdir}
}

SYSTEMD_SERVICE:${PN} = "gateway.service"