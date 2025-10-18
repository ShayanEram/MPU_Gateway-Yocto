SUMMARY = "MPU Unmanned Aerial Vehicle Project"
DESCRIPTION = "C++17 UAV software with modular architecture, threading, and real-time control"
LICENSE = "CLOSED"

# Fetch from GitHub repo
SRC_URI = "git://github.com/ShayanEram/MPU_Unmanned-Aerial-Vehicle.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake systemd

# Dependencies
DEPENDS += "libgpiod"

# Disable tests in production
EXTRA_OECMAKE = "-DBUILD_TESTS=OFF"

# Install binary
do_install():append() {
    install -d ${D}${bindir}
    install -m 0755 UAV ${D}${bindir}

    # Install systemd unit
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/mpu-uav.service ${D}${systemd_system_unitdir}
}

SYSTEMD_SERVICE:${PN} = "uav.service"
