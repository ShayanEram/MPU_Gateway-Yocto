# Yocto‑Based Embedded Linux for project UAV on Raspberry Pi 5

## Overview
This project builds a **custom Yocto Linux distribution** for the Raspberry Pi 5, tailored for **embedded uav applications**. It integrates a C++17 UAV gateway application (`UAV`) with support for **TCP/IP sockets**, **GPIO**, **I²C**, **SPI**, **UART**, and **PWM**. The image is production‑ready, reproducible, and managed with **systemd** for robust service deployment.

This layer includes a recipe (`uav.bb`) for the [Unmanned-Aerial-Vehicle](https://github.com/ShayanEram/MPU_Unmanned-Aerial-Vehicle) project.

---

## Features
- **Yocto LTS (Scarthgap)** build system  
- **Raspberry Pi 5 hardware enablement**:
  - UART (`/dev/serial0`)
  - I²C (`/dev/i2c-1`)
  - SPI (`/dev/spidev0.*`)
  - GPIO via `libgpiod` (`/dev/gpiochip*`)
  - PWM (`/sys/class/pwm/pwmchip*`)
- **Custom boot**:
  - U-Boot
- **Networking**:
  - Ethernet (default)
  - Wi‑Fi via `connman`
- **C++17 UAV application**:
  - Modular architecture (`Inc/`, `Src/`)
  - Multithreaded TCP/IP server
  - Unit tests (GoogleTest) toggleable via `-DBUILD_TESTS=ON/OFF`
- **Systemd integration**:
  - Auto‑start at boot
  - Restart on failure
  - Logging via `journalctl`

---

## Repository Structure
```
meta-devpi/                 # Custom Yocto layer
  recipes-apps/
    recipes-apps/
        uav.bb              # Recipe for UAV app
        uav.service         # systemd unit
conf/
  local.conf                 # Machine + interface configs
  bblayers.conf              # Layer configuration
```

---

## Build Instructions

### 1. Clone Yocto and Layers
```bash
git clone -b scarthgap https://git.yoctoproject.org/poky
git clone -b scarthgap https://github.com/agherzan/meta-raspberrypi.git
git clone -b scarthgap https://github.com/openembedded/meta-openembedded.git
git submodule add -b scarthgap/u-boot https://github.com/moto-timo/meta-lts-mixins.git meta-lts-mixins

bitbake-layers create-layer meta-devpi
bitbake-layers add-layer ../meta-raspberrypi ../meta-openembedded/meta-oe ../meta-devpi ../meta-lts-mixins
```

### 2. Configure Build
In `conf/local.conf`:
```conf
MACHINE = "raspberrypi5"
INIT_MANAGER = "systemd"

# Enable interfaces
# Networking
# Tools
# UAV app

```

### 3. Build Image
```bash
source oe-init-build-env
bitbake core-image-minimal
```

### 4. Deploy
Flash the generated `.wic` image to an SD card:
```bash
sudo dd if=tmp/deploy/images/raspberrypi5/core-image-minimal-raspberrypi5.wic of=/dev/sdX bs=4M status=progress
```

---

## UAV Application

### Build System
- **CMake** project with `Inc/`, `Src/`, and `Tests/`
- Tests disabled in Yocto via:
  ```bitbake
  EXTRA_OECMAKE = "-DBUILD_TESTS=OFF"
  ```

### Runtime
- Installed to `/usr/bin/UAV`
- Managed by systemd:
  ```bash
  systemctl status mpu-uav.service
  journalctl -u mpu-uav.service
  ```

---

## Validation Checklist
On the target device:
```bash
# UART
ls /dev/serial0

# I²C
i2cdetect -y 1

# SPI
ls /dev/spidev*

# GPIO
gpiodetect
gpioinfo

# PWM
ls /sys/class/pwm/

# Networking
ip addr
ping 8.8.8.8
```

---

## License
This project is licensed under the terms specified in the source repositories. The Yocto recipes are provided under a **CLOSED** license unless otherwise noted.

---

## Author
**Shayan Eram**  
Embedded Systems Engineer | Firmware Specialist | IoT & Edge AI Innovator  
